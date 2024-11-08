package com.cotede.interns.task.users;

import com.cotede.interns.task.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;
    

    @GetMapping
    public ApiResponse<UserResponseDTO> getUserByUserName(@RequestParam String userName) {
        UserResponseDTO userResponse = userServiceImpl.getUserByUserName(userName);
        return ApiResponse.success(userResponse, HttpStatus.OK, "user.get.success");
    }


    @PutMapping
    public ApiResponse<UserResponseDTO> updateUser(@Valid @RequestBody UserRequestDTO userRequest) {
        UserResponseDTO userResponse = userServiceImpl.updateUser(userRequest);
        return ApiResponse.success(userResponse, HttpStatus.OK, "user.update.success");
    }

    @DeleteMapping
    public ApiResponse<Boolean> deleteUser(@RequestParam String username) {
        userServiceImpl.deleteUserByUserName(username);
        return ApiResponse.success(true, HttpStatus.NO_CONTENT, "user.delete.success");
    }

}
