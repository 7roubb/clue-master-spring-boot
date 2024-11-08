package com.cotede.interns.task.security;
import com.cotede.interns.task.common.OnCreate;
import com.cotede.interns.task.exceptions.CustomExceptions;
import com.cotede.interns.task.response.ApiResponse;
import com.cotede.interns.task.security.jwt.LoginRequest;
import com.cotede.interns.task.security.jwt.LoginResponse;
import com.cotede.interns.task.security.services.AuthService;
import com.cotede.interns.task.users.UserRequestDTO;
import com.cotede.interns.task.users.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MessageSource messageSource;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateUser(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authService.authenticateUser(loginRequest);
            response.setHeader("Authorization", "Bearer " + loginResponse.getJwtToken());
            String message = messageSource.getMessage("user.auth.success",
                    new Object[]{loginRequest.getUserName()}, LocaleContextHolder.getLocale());
            return ResponseEntity.ok(ApiResponse.success(loginResponse, HttpStatus.OK, message));
        } catch (CustomExceptions.InvalidCredentialsException e) {
            String message = messageSource.getMessage("user.auth.invalid.credentials",
                    null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(message, HttpStatus.UNAUTHORIZED));
        } catch (Exception e) {
            String message = messageSource.getMessage("user.auth.invalid.credentials",
                    null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
        }
    }
    @PostMapping("/signup")
    public ApiResponse<UserResponseDTO> createUser(@Validated(OnCreate.class) @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponse = authService.createUser(userRequestDTO);
        String message = messageSource.getMessage("user.create.success", new Object[]{userResponse.getUserName()}, LocaleContextHolder.getLocale());
        return ApiResponse.<UserResponseDTO>builder()
                .content(userResponse)
                .status(HttpStatus.CREATED)
                .message(message)
                .build();
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("You have successfully logged out");
    }
}