package com.cotede.interns.task.rooms;

import com.cotede.interns.task.users.OtherUserResponseDTO;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomPlayerListResponseDTO {
    private Long roomId;
    private List<OtherUserResponseDTO> players;
    private String roomStatus;
}
