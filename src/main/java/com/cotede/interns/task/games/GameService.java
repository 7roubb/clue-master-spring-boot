package com.cotede.interns.task.games;

import com.cotede.interns.task.cases.Case;
import com.cotede.interns.task.exceptions.CustomExceptions;
import com.cotede.interns.task.rooms.Room;
import com.cotede.interns.task.round.RoundAnswerRequestDTO;
import com.cotede.interns.task.cases.CaseResponseDTO;
import com.cotede.interns.task.round.RoundResultResponseDTO;
import com.cotede.interns.task.users.User;
import com.cotede.interns.task.users.UserRoundResponseDTO;

import java.util.List;

public interface GameService {
    Boolean submitAnswer(RoundAnswerRequestDTO answerRequest);
    RoundResultResponseDTO getRoundResults(Long roomId);
    Case getCase(Long roomId);
    List<UserRoundResponseDTO> getRoomLeaderboard(Long roomId);
    Game createGame(Room room, List<User> players,Integer totalRounds);
    List<CaseResponseDTO> getAllCasesForGame(Long roomId);
    GameResponseDTO getGame(Long roomId);


}
