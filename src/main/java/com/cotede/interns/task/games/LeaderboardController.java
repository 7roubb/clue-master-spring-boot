package com.cotede.interns.task.games;

import com.cotede.interns.task.users.OtherUserResponseDTO;
import com.cotede.interns.task.response.ApiResponse;
import com.cotede.interns.task.users.User;
import com.cotede.interns.task.users.UserMapper;
import com.cotede.interns.task.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // Correct import
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final UserRepository userRepository;

    @GetMapping
    public ApiResponse<Page<OtherUserResponseDTO>> getGlobalLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllByOrderByWinPercentageDesc(pageable);
        Page<OtherUserResponseDTO> leaderboard = UserMapper.toOtherUserResponsePage(userPage);
        return ApiResponse.success(leaderboard, HttpStatus.OK, "leaderboard.fetched.successfully");
    }
}
