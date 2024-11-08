package com.cotede.interns.task.rooms;

import com.cotede.interns.task.users.User;
import com.cotede.interns.task.users.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomMapper {
    public static Room toRoomEntity(RoomRequestDTO roomRequest, User host) {

        List<User> players = new ArrayList<>();
        players.add(host);

        return Room.builder()
                .difficulty(roomRequest.getDifficulty())
                .maxPlayers(roomRequest.getMaxPlayers())
                .numberOfRounds(roomRequest.getNumberOfRounds())
                .players(players)
                .build();
    }

    public static RoomResponseDTO toRoomResponseDTO(Room room) {
        return RoomResponseDTO.builder()
                .id(room.getId())
                .difficultyLevel(room.getDifficulty())
                .currentPlayers(room.getPlayers().size())
                .numberOfRounds(room.getNumberOfRounds())
                .maxPlayers(room.getMaxPlayers())
                .players(room.getPlayers().stream()
                        .map(UserMapper::toOtherUserResponse)
                        .collect(Collectors.toList()))
                .isGameStarted(room.isStarted())
                .build();
    }
}
