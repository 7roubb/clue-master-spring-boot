package com.cotede.interns.task.users;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoundResponseDTO {
    private Long playerId;
    private String playerName;
    private Integer score;
}
