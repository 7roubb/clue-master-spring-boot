package com.cotede.interns.task.round;

import com.cotede.interns.task.exceptions.CustomExceptions;
import com.cotede.interns.task.games.Game;
import com.cotede.interns.task.games.GameRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

    private final RoundRepository roundRepository;
    private final GameRepository gameRepository;

    @Transactional
    @Override
    public Round createRound(RoundRequestDTO roundRequest) {
        Game game = fetchGame(roundRequest.getGameId());
        Round round = buildRound(game, roundRequest.getRoundNumber());
        return roundRepository.save(round);
    }

    private Game fetchGame(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomExceptions.RoomNotFoundException(gameId));
    }

    private Round buildRound(Game game, int roundNumber) {
        return Round.builder()
                .game(game)
                .roundNumber(roundNumber)
                .completed(false)
                .build();
    }
}