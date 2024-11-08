package com.cotede.interns.task.cases;

import com.cotede.interns.task.users.OtherUserResponseDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScoreDTO {
    private OtherUserResponseDTO player;
    private Integer totalPoints;
    private Integer roundsWon;
}
