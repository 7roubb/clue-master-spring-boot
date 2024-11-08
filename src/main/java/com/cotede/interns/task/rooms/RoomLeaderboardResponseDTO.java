package com.cotede.interns.task.rooms;

import com.cotede.interns.task.cases.PlayerScoreDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomLeaderboardResponseDTO {
    private Long roomId;
    private List<PlayerScoreDTO> playerScores;
}
