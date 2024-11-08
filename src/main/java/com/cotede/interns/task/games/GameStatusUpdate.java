package com.cotede.interns.task.games;

import com.cotede.interns.task.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStatusUpdate {
    private Long gameId;
    private Integer currentRound;
    private List<User> players;
    private Map<User, Integer> pointsMap;
    private boolean started;

}
