package com.cotede.interns.task.security.services;

import com.cotede.interns.task.exceptions.CustomExceptions;
import com.cotede.interns.task.security.jwt.*;
import com.cotede.interns.task.users.UserRequestDTO;
import com.cotede.interns.task.users.UserResponseDTO;
import com.cotede.interns.task.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;


    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        return userService.createUser(userRequestDTO);
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String username = userDetails.getUsername();
            String newToken = jwtUtils.generateTokenFromUsername(userDetails);
            String hashedToken = HashUtils.hash(newToken);
            System.out.println("Hash Done");
            redisTemplate.opsForValue().set(username, hashedToken);
            return new LoginResponse(newToken, username, userDetails.getId());
        } catch(AuthenticationException ex){
            AuthMessageThread.setAuthError("jwt.token.expired");
            return null ;
        }


    }

    public void logout(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromHeader(request);
        Optional.ofNullable(token).filter(jwtUtils::validateJwtToken)
                .map(jwtUtils::getUserNameFromJwtToken)
                .ifPresentOrElse(
                        username -> redisTemplate.delete(username),
                        () -> {throw new CustomExceptions.InvalidCredentialsException("Invalid token");
                        }
                );
    }
}