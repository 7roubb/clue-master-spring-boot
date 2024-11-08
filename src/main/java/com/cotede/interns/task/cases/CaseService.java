package com.cotede.interns.task.cases;

import com.cotede.interns.task.chatgpt.CaseChatGptResponse;
import com.cotede.interns.task.chatgpt.ChatGPTService;
import com.cotede.interns.task.exceptions.CustomExceptions;
import com.cotede.interns.task.games.Game;
import com.cotede.interns.task.rooms.Room;
import com.cotede.interns.task.round.Round;
import com.cotede.interns.task.round.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaseService {
    private final ChatGPTService chatGPTService;
    private final RedisTemplate<String,Case> caseRedisTemplate;
    private final RoundRepository roundRepository;

    public Round createRound(Game game, Room room, int roundNumber) {
        CaseChatGptResponse caseChatGptResponse = chatGPTService.getChatGptResponse(room.getDifficulty().toString());
        Case chatGptCase = new Case();
        chatGptCase.setScenario(caseChatGptResponse.getScenario());
        chatGptCase.setAnswerParagraph(caseChatGptResponse.getAnswerParagraph());
        chatGptCase.setCharacters(caseChatGptResponse.getCharacters().stream()
                .map(c -> new Character(c.getId(), c.getName(), c.getAge(), c.getGender(),
                        c.getOccupation(), c.getIsCriminal(), c.getAlibi(), c.getMotive()))
                .collect(Collectors.toList()));
        String redisKey = "Game:" + game.getId() + ":Round:" + roundNumber;
        caseRedisTemplate.opsForValue().set(redisKey, chatGptCase);
        System.out.println("Case stored in Redis with key: " + redisKey);
        Case savedCase = (Case) caseRedisTemplate.opsForValue().get(redisKey);
        Optional.ofNullable(savedCase)
                .orElseThrow(() -> new CustomExceptions.FaildToSaveInCache(redisKey));
        Round round = Round.builder()
                .game(game)
                .roundNumber(roundNumber)
                .completed(false)
                .caseRedisId(redisKey)
                .build();

        roundRepository.save(round);
        return round;
    }

}
