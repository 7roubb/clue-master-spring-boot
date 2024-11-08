package com.cotede.interns.task.games;

import com.cotede.interns.task.rooms.RoomMapper;
import com.cotede.interns.task.round.RoundMapper;
import com.cotede.interns.task.users.UserRoundResponseDTO;
import com.cotede.interns.task.users.User;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameMapper {

    public static UserRoundResponseDTO toUserRoundResponseDTO(User user, Integer points) {

        return Optional.ofNullable(user)
                .map(u -> UserRoundResponseDTO.builder()
                        .playerId(user.getUserId())
                        .playerName(user.getUserName())
                        .score(Optional.ofNullable(points).orElse(0))
                        .build())
                .orElse(null);
    }
    public static GameResponseDTO toGameResponseDTO(Game game) {
        return Optional.ofNullable(game)
                .map(g -> GameResponseDTO.builder()
                        .id(g.getId())
                        .room(RoomMapper.toRoomResponseDTO(g.getRoom()))
                        .currentRound(g.getCurrentRound())
                        .rounds(g.getRounds().stream()
                                .map(RoundMapper::toRoundResponseDTO)
                                .collect(Collectors.toList()))
                        .pointsMap(mapUserPoints(g.getPointsMap()))
                        .build())
                .orElse(null);
    }

    private static Map<String, Integer> mapUserPoints(Map<User, Integer> pointsMap) {
        return pointsMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getUserName(), entry -> entry.getValue())); // Map user IDs to their points
    }

}
