package com.cotede.interns.task.rooms;

import com.cotede.interns.task.users.OtherUserResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO {
    private Long id;
    private Integer currentPlayers;
    private Integer maxPlayers;
    private DifficultyLevel difficultyLevel;
    private Integer numberOfRounds;
    private List<OtherUserResponseDTO> players ;
    private Boolean isGameStarted ;
}
