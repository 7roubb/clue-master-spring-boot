package com.cotede.interns.task.users;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherUserResponseDTO {
    private String userName;
    private Long totalGame;
    private Long totalWin;
    private Long totalPoints;
    private Double winPercentage;
}
