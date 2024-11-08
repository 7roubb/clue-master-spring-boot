package com.cotede.interns.task.cases;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalLeaderboardResponseDTO {
    private List<PlayerScoreDTO> globalPlayerScores;
}
