package com.cotede.interns.task.rooms;

import com.cotede.interns.task.common.BaseEntity;
import com.cotede.interns.task.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "room_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> players;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;

    @NotNull(message = "Number of rounds is required")
    @Min(value = 2, message = "Minimum number of rounds is 2")
    @Max(value = 5, message = "Maximum number of rounds is 5")
    private Integer numberOfRounds;

    @NotNull(message = "Maximum players is required")
    @Min(value = 2, message = "Minimum players is 2")
    @Max(value = 7, message = "Maximum players is 7")
    private Integer maxPlayers;

    private boolean started = false;

}
