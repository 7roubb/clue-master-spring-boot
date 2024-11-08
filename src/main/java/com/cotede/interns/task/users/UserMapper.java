package com.cotede.interns.task.users;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static User toUserEntity(UserRequestDTO userRequest) {
        return User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .phoneNumber(userRequest.getPhoneNumber())
                .role(userRequest.getRole())
                .userName(userRequest.getUserName())
                .build();
    }

    public static UserResponseDTO toUserResponse(User user) {
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .userName(user.getUserName())
                .totalGame(user.getTotalGame())
                .totalPoints(user.getTotalPoints())
                .totalWin(user.getTotalWin())
                .winPercentage(user.getWinPercentage())
                .role(user.getRole())
                .build();
    }

    public static OtherUserResponseDTO toOtherUserResponse(User user) {
        return OtherUserResponseDTO.builder()
                .userName(user.getUserName())
                .totalGame(user.getTotalGame())
                .totalPoints(user.getTotalPoints())
                .totalWin(user.getTotalWin())
                .winPercentage(user.getWinPercentage())
                .build();
    }
    public static Page<OtherUserResponseDTO> toOtherUserResponsePage(Page<User> userPage) {
        return userPage.map(UserMapper::toOtherUserResponse);
    }
}
