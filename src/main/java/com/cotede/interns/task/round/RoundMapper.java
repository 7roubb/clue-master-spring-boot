package com.cotede.interns.task.round;


import com.cotede.interns.task.cases.Answer;
import com.cotede.interns.task.cases.AnswerResponseDTO;
import com.cotede.interns.task.users.UserMapper;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
public class RoundMapper {

    public static RoundResponseDTO toRoundResponseDTO(Round round) {
        return Optional.ofNullable(round)
                .map(r -> RoundResponseDTO.builder()
                        .id(r.getId())
                        .roundNumber(r.getRoundNumber())
                        .completed(r.isCompleted())
                        .build())
                .orElse(null);
    }
    public static RoundResultResponseDTO toRoundResultResponseDTO(List<Answer> answers) {
        List<AnswerResponseDTO> answerResponseDTOs = answers.stream()
                .map(answer -> new AnswerResponseDTO(
                        UserMapper.toOtherUserResponse(answer.getUser()),
                        answer.getPoints()
                ))
                .collect(Collectors.toList());

        return new RoundResultResponseDTO(answerResponseDTOs);
    }
}