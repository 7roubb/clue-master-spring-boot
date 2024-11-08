package com.cotede.interns.task.users;

public interface UserService {
    User getAuthenticatedUser();
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(UserRequestDTO userRequestDTO);
    UserResponseDTO getUserByUserName(String userName);
    Boolean deleteUserByUserName(String userName);
}
