package com.cotede.interns.task.round;

import com.cotede.interns.task.cases.AnswerResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoundResultResponseDTO {
    private List<AnswerResponseDTO> results;
}
