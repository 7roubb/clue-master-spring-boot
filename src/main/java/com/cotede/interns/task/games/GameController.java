package com.cotede.interns.task.games;

import com.cotede.interns.task.cases.Case;
import com.cotede.interns.task.round.RoundAnswerRequestDTO;
import com.cotede.interns.task.cases.CaseResponseDTO;
import com.cotede.interns.task.round.RoundResultResponseDTO;
import com.cotede.interns.task.users.UserRoundResponseDTO;
import com.cotede.interns.task.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameServiceImpl gameService;
    private final MessageSource messageSource;

    @PostMapping("/round/answer")
    public ApiResponse<Boolean> submitAnswer(@Valid @RequestBody RoundAnswerRequestDTO answerRequest) {
        Boolean roundResult = gameService.submitAnswer(answerRequest);
        String message = messageSource.getMessage("round.submit.success", null, Locale.getDefault());
        return ApiResponse.success(roundResult, HttpStatus.OK, message);
    }

    @GetMapping("/round/results")
    public ApiResponse<RoundResultResponseDTO> getRoundResults(@RequestHeader Long roomId) {
        RoundResultResponseDTO results = gameService.getRoundResults(roomId);
        String message = messageSource.getMessage("round.results.success", null, Locale.getDefault());
        return ApiResponse.success(results, HttpStatus.OK, message);
    }

    @GetMapping("/round/case")
    public ApiResponse<Case> getCase(@RequestHeader Long roomId) {
        Case caseResponse = gameService.getCase(roomId);
        String message = messageSource.getMessage("case.retrieve.success", null, Locale.getDefault());
        return ApiResponse.success(caseResponse, HttpStatus.OK, message);
    }

    @GetMapping("/leaderboard")
    public ApiResponse<List<UserRoundResponseDTO>> getRoomLeaderboard(@RequestHeader Long roomId) {
        List<UserRoundResponseDTO> leaderboard = gameService.getRoomLeaderboard(roomId);
        String message = messageSource.getMessage("leaderboard.fetch.success", null, Locale.getDefault());
        return ApiResponse.success(leaderboard, HttpStatus.OK, message);
    }


    @GetMapping("/cases")
    public ApiResponse<List<CaseResponseDTO>> getAllCasesForGame(@RequestHeader Long roomId) {
        List<CaseResponseDTO> cases = gameService.getAllCasesForGame(roomId);
        String message = messageSource.getMessage("cases.fetch.success", new Object[]{roomId}, Locale.getDefault());
        return ApiResponse.success(cases, HttpStatus.OK, message);
    }

    @GetMapping
    public ApiResponse<GameResponseDTO> getGame(@RequestHeader Long roomId) {
        String message = messageSource.getMessage("game.fetch.success", null, Locale.getDefault());
        return ApiResponse.success(gameService.getGame(roomId), HttpStatus.OK, message);
    }
}
