package com.cotede.interns.task.rooms;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequestDTO {

    @NotNull(message = "{room.difficulty.notnull}")
    @Enumerated
    private DifficultyLevel difficulty;

    @NotNull(message = "{room.maxPlayers.notnull}")
    @Min(value = 2, message = "{room.maxPlayers.min}")
    @Max(value = 7, message = "{room.maxPlayers.max}")
    private Integer maxPlayers;

    @NotNull(message = "{room.numberOfRounds.notnull}")
    @Min(value = 2, message = "{room.numberOfRounds.min}")
    @Max(value = 5, message = "{room.numberOfRounds.max}")
    private Integer numberOfRounds;
}
