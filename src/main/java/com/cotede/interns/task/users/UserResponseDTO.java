package com.cotede.interns.task.users;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String userName;
    private String email;
    private String phoneNumber;
    private Long totalGame;
    private Long totalWin;
    private Long totalPoints;
    private Double winPercentage;
    private RoleType role;
}
