package com.cotede.interns.task.round;

import com.cotede.interns.task.cases.Answer;
import com.cotede.interns.task.common.BaseEntity;
import com.cotede.interns.task.games.Game;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Round extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private Game game;

    private String caseRedisId;

    private int roundNumber;

    private boolean completed = false;

    @OneToMany(mappedBy = "round")
    private List<Answer> answers;
}



