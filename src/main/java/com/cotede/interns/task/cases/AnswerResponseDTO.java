package com.cotede.interns.task.cases;

import com.cotede.interns.task.users.OtherUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDTO {
    OtherUserResponseDTO user;
    Integer score;
}
