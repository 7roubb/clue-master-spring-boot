package com.cotede.interns.task.round;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoundAnswerRequestDTO {
    private Long roomId;
    private String answer;
}
