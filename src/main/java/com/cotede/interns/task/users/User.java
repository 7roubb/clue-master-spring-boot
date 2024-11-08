package com.cotede.interns.task.users;
import com.cotede.interns.task.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Validated
@ToString
@Entity
@Table(
        name = "\"user\"",
        indexes = {
                @Index(name = "idx_user_userName", columnList = "userName"),
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phoneNumber", columnList = "phoneNumber")
        }
)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class User  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false,unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String phoneNumber;

    private Long totalGame;

    private Long totalWin;

    private Long totalPoints;

    private Double winPercentage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role ;

    public void setWinPercentage() {
        this.winPercentage = (((double) totalWin / totalGame) * 100.0);
    }
}