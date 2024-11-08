package com.cotede.interns.task.games;
import com.cotede.interns.task.cases.*;
import com.cotede.interns.task.chatgpt.AnswerEvaluationService;
import com.cotede.interns.task.config.LRUCache;
import com.cotede.interns.task.exceptions.CustomExceptions;
import com.cotede.interns.task.rooms.Room;
import com.cotede.interns.task.round.*;
import com.cotede.interns.task.users.User;
import com.cotede.interns.task.users.UserRepository;
import com.cotede.interns.task.users.UserRoundResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final RedisTemplate<String, Case> caseRedisTemplate;
    private final CaseService caseService;
    private final SimpMessagingTemplate brokerMessagingTemplate;
    private final UserRepository userRepository;
    private final ConcurrentHashMap<Long, List<User>> submissions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Long> roundStartTime = new ConcurrentHashMap<>();
    private final AnswerEvaluationService answerEvaluationService;
    private final LRUCache<String, Case> caseCache = new LRUCache<>(100);
    private final AnswerRepository answerRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RoundRepository roundRepository;

    @Override
    public Game createGame(Room room, List<User> players, Integer totalRounds) {
        Game game = new Game(room, players, totalRounds);
        gameRepository.save(game);
        initializeRounds(game, totalRounds);
        game.initializePoints(players);
        GameResponseDTO responseDTO = GameMapper.toGameResponseDTO(game);

        messagingTemplate.convertAndSend("/topic/room/" + room.getId(), responseDTO);


        return game;
    }

    private void initializeRounds(Game game, int totalRounds) {
        List<Round> rounds = IntStream.range(1, totalRounds + 1)
                .mapToObj(roundNumber -> caseService.createRound(game, game.getRoom(), roundNumber))
                .toList();
        game.getRounds().addAll(rounds);
    }



    private void startRound(Game game, int roundNumber) {
        Round round = caseService.createRound(game, game.getRoom(), roundNumber);
        brokerMessagingTemplate.convertAndSend("/topic/room/" + game.getRoom().getId(), round);
        roundStartTime.put(game.getId(), System.currentTimeMillis());
        submissions.put(game.getId(), new ArrayList<>());
    }

    @Override
    public Boolean submitAnswer(RoundAnswerRequestDTO answerRequest) {
        User user = getAuthenticatedUser();
        Game game = getLatestGameByRoomId(answerRequest.getRoomId());
        Optional.ofNullable(game.getIsEnded())
                .filter(isEnded -> !isEnded)
                .orElseThrow(() -> new CustomExceptions.GameAlreadyEnded());
        Round currentRound = roundRepository.getRoundByGameIdAndRoundNumber(game.getId(),game.getCurrentRound().longValue()+1);
        Optional.of(answerRepository.existsByUserAndRound(user, currentRound))
                .filter(alreadySubmitted -> !alreadySubmitted)
                .orElseThrow(() -> new CustomExceptions.AnswerAlreadySubmitted());
        Answer newAnswer = createNewAnswer(answerRequest, user, currentRound);
        game.increasePoints(user, newAnswer.getPoints());
        gameRepository.save(game);
        List<User> players = game.getRoom().getPlayers();
        long submittedCount = answerRepository.countByRound(currentRound);
        messagingTemplate.convertAndSend("/topic/room/" + game.getRoom().getId(), "ANSWER_SUBMITED");


        if (submittedCount == players.size()) {
            if (game.getCurrentRound() >= game.getRounds().size() - 1) {
                currentRound.setCompleted(true);
                roundRepository.save(currentRound);
                endGame(game);
                log.info("Sending END_GAME message for game: " + game.getId());
                messagingTemplate.convertAndSend("/topic/room/" + game.getRoom().getId() , "END_GAME");
            } else {
                currentRound.setCompleted(true);
                game.setCurrentRound(game.getCurrentRound() + 1);
                gameRepository.save(game);
                roundRepository.save(currentRound);
                messagingTemplate.convertAndSend("/topic/room/" + game.getRoom().getId() ,"NEXT_ROUND");
                log.info("Sending NEXT_ROUND message for game: " + game.getId());
                showLeaderboard(game);
            }
        }

        return true;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomExceptions.UserNotFound(userName));
    }

    private Game getLatestGameByRoomId(Long roomId) {
        return gameRepository.findFirstByRoomIdOrderByCreatedAtDesc(roomId)
                .orElseThrow(CustomExceptions.GameNotFound::new);
    }

    private Answer createNewAnswer(RoundAnswerRequestDTO answerRequest, User user, Round currentRound) {
        Answer newAnswer = CaseMapper.toAnswerEntity(answerRequest, user);
        newAnswer.setRound(currentRound);
        Case currentRoundCase = caseRedisTemplate.opsForValue().get(currentRound.getCaseRedisId());
        assert currentRoundCase != null;
        Integer points = answerEvaluationService.evaluateAnswer(newAnswer, currentRoundCase.toString());
        newAnswer.setPoints(points);
        answerRepository.save(newAnswer);
        return newAnswer;
    }

    @Scheduled(fixedDelay = 300000)
    public void checkSubmissions() {
        submissions.forEach(this::checkSubmissionTimeout);
    }

    private void checkSubmissionTimeout(Long gameId, List<User> submittedUsers) {
        long SUBMISSION_TIMEOUT = 300000;
        if (System.currentTimeMillis() - roundStartTime.get(gameId) >= SUBMISSION_TIMEOUT) {
            Game game = gameRepository.findById(gameId).orElseThrow();
            List<User> players = game.getRoom().getPlayers();
            players.forEach(player -> {
                if (!submittedUsers.contains(player)) {
                    game.increasePoints(player, 0);
                }
            });
            notifySubmissionTimeout(gameId);
            startNextRound(game);
        }
    }

    private void notifySubmissionTimeout(Long gameId) {
        brokerMessagingTemplate.convertAndSend("/topic/room/" + gameId, "Submission timeout reached. Fetch result.");
    }

    private void startNextRound(Game game) {
        int nextRoundNumber = game.getCurrentRound() + 1;
        if (nextRoundNumber <= game.getRounds().size()) {
            startRound(game, nextRoundNumber);
        } else {
            endGame(game);
        }
    }

    @Override
    public RoundResultResponseDTO getRoundResults(Long roomId) {
        Game game = getLatestGameByRoomId(roomId);
        Round round = game.getRounds().get(game.getCurrentRound());
        List<Answer> answer = round.getAnswers();
        return RoundMapper.toRoundResultResponseDTO(answer) ;
    }

    @Override
    public Case getCase(Long roomId) {
        Game game = getLatestGameByRoomId(roomId);
        return getLatestCaseFromRound(game)
                .orElseThrow(CustomExceptions.CaseNotFound::new);
    }

    private Optional<Case> getLatestCaseFromRound(Game game) {
        return game.getRounds().stream()
                .filter(round -> !round.isCompleted())
                .findFirst()
                .map(round -> getCaseFromRedis(round.getCaseRedisId()));
    }

    private Case getCaseFromRedis(String redisKey) {
        return Optional.ofNullable(caseCache.get(redisKey))
                .or(() -> {
                    Case chatGptCase = caseRedisTemplate.opsForValue().get(redisKey);
                    if (chatGptCase != null) {
                        caseCache.put(redisKey, chatGptCase);
                    }
                    return Optional.ofNullable(chatGptCase);
                })
                .orElse(null);
    }

    @Override
    public List<UserRoundResponseDTO> getRoomLeaderboard(Long roomId) {
        return Optional.ofNullable(gameRepository.findByRoomId(roomId).orElseThrow(
                        ()-> new CustomExceptions.RoomNotFoundException(roomId)))
                .map(Game::getPointsMap)
                .map(this::mapToUserRoundResponseDTO)
                .orElse(Collections.emptyList());
    }

    private List<UserRoundResponseDTO> mapToUserRoundResponseDTO(Map<User, Integer> pointsMap) {
        return pointsMap.entrySet().stream()
                .map(entry -> GameMapper.toUserRoundResponseDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(UserRoundResponseDTO::getScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<CaseResponseDTO> getAllCasesForGame(Long roomId) {
        Game game = getLatestGameByRoomId(roomId);
        return game.getRounds().stream()
                .map(this::fetchCaseFromRound)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<CaseResponseDTO> fetchCaseFromRound(Round round) {
        String redisKey = round.getCaseRedisId();
        if (redisKey == null || redisKey.isEmpty()) {
            return Optional.empty();
        }
        Case chatGptCase = caseRedisTemplate.opsForValue().get(redisKey);
        return Optional.ofNullable(chatGptCase)
                .map(CaseMapper::toCaseResponseDTO);
    }

    @Override
    public GameResponseDTO getGame(Long roomId) {
        Game game =getLatestGameByRoomId(roomId);
        return GameMapper.toGameResponseDTO(game);
    }

    private void showResultsAndLeaderboard(Game game) {
        brokerMessagingTemplate.convertAndSend("/topic/room/" + game.getRoom().getId(), getRoundResults(game.getId()));
        updatePointsFromRoundAnswers(game);
        scheduleLeaderboardDisplay(game);
    }

    private void updatePointsFromRoundAnswers(Game game) {
        Round round = game.getRounds().get(game.getCurrentRound());
        round.getAnswers().forEach(answer -> {
            User user = answer.getUser();
            Integer points = answer.getPoints();
            game.getPointsMap().merge(user, points, Integer::sum);
        });
    }

    private void scheduleLeaderboardDisplay(Game game) {
        long RESULT_DISPLAY_TIME = 10000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                showLeaderboard(game);
            }
        }, RESULT_DISPLAY_TIME);
    }

    private void showLeaderboard(Game game) {
        List<UserRoundResponseDTO> leaderboard = getRoomLeaderboard(game.getId());
        brokerMessagingTemplate.convertAndSend("/topic/room/" + game.getRoom().getId(), leaderboard);
        scheduleNextRoundStart(game);
    }

    private void scheduleNextRoundStart(Game game) {
        long LEADERBOARD_DISPLAY_TIME = 10000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startNextRound(game);
            }
        }, LEADERBOARD_DISPLAY_TIME);
    }

    private void endGame(Game game) {
        game.setIsEnded(true);
        gameRepository.save(game);
        updateUsersStatistics(game);
        Room room = game.getRoom();
        room.setStarted(false);
        gameRepository.save(game);
        GameResponseDTO responseDTO = GameMapper.toGameResponseDTO(game);
        messagingTemplate.convertAndSend("/topic/room/" + game.getRoom().getId(), responseDTO);
    }

    private void updateUsersStatistics(Game game) {
        game.getPointsMap().forEach((user, points) -> {
            user.setTotalGame(user.getTotalGame() + 1);
            user.setTotalPoints(user.getTotalPoints() + points);
            if (isUserWinner(game, user)) {
                user.setTotalWin(user.getTotalWin() + 1);
            }
            user.setWinPercentage();
            userRepository.save(user);

        });
    }

    private boolean isUserWinner(Game game, User user) {
        int maxPoints = game.getPointsMap().values().stream().max(Integer::compare).orElse(0);
        return game.getPointsMap().get(user) == maxPoints;
    }

}
