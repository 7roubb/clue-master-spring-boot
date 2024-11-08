package com.cotede.interns.task.games;
import com.cotede.interns.task.round.RoundResponseDTO;
import com.cotede.interns.task.rooms.RoomResponseDTO;
import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResponseDTO {
    private Long id;
    private RoomResponseDTO room;
    private List<RoundResponseDTO> rounds;
    private Integer currentRound;
    private Map<String, Integer> pointsMap;
}