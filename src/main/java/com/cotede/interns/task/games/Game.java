package com.cotede.interns.task.games;

import com.cotede.interns.task.common.BaseEntity;
import com.cotede.interns.task.round.Round;
import com.cotede.interns.task.rooms.Room;
import com.cotede.interns.task.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "games")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Game extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

    @OneToMany(mappedBy = "game")
    private List<Round> rounds;

    private Integer currentRound = 0;
    private Boolean isEnded = false;
    @ElementCollection
    @CollectionTable(name = "player_points", joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyJoinColumn(name = "id")
    @Column(name = "points")
    private Map<User, Integer> pointsMap = new HashMap<>();

    public Game(Room room, List<User> players, Integer totalRounds) {
        this.room = room;
        this.rounds = initializeRounds(totalRounds);
        this.currentRound = 0;
        this.pointsMap = new HashMap<>();
        initializePoints(players);
    }

    private List<Round> initializeRounds(Integer totalRounds) {
        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < totalRounds; i++) {
            rounds.add(new Round());
        }
        return rounds;
    }
    public void initializePoints(List<User> players) {
        players.forEach(player -> pointsMap.put(player, 0));
    }

    public void increasePoints(User player, int points) {
        pointsMap.put(player, pointsMap.getOrDefault(player, 0) + points);
    }

    public void nextRound() {
        currentRound++;
    }
    public Round getCurrentRoundObject() {
        return rounds.get(currentRound);
    }
}
