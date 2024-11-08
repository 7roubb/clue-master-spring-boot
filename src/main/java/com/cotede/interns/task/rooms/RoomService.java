package com.cotede.interns.task.rooms;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {

    RoomResponseDTO createRoom(RoomRequestDTO roomRequest);
    Page<RoomResponseDTO> getAvailableRooms(Pageable pageable);
    RoomResponseDTO joinRoom(Long roomId);
    void startGame(Long roomId);
    void endGame(Long roomId);
    RoomResponseDTO getRoom(Long roomId) ;
}
