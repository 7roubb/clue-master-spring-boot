package com.cotede.interns.task.users;

import com.cotede.interns.task.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        userRepository.findByUserName(userRequestDTO.getUserName())
                .ifPresent(user -> {
                    throw new CustomExceptions.UserAlreadyExistsException(userRequestDTO.getUserName());
                });
        userRepository.findByEmail(userRequestDTO.getEmail())
                .ifPresent(user -> {
                    throw new CustomExceptions.EmailAlreadyExistsException(userRequestDTO.getEmail());
                });

        userRepository.findByPhoneNumber(userRequestDTO.getPhoneNumber())
                .ifPresent(user -> {
                    throw new CustomExceptions.PhoneNumberAlreadyExistsException(userRequestDTO.getPhoneNumber());
                });
        User user = UserMapper.toUserEntity(userRequestDTO);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setTotalGame(0L);
        user.setTotalPoints(0L);
        user.setTotalWin(0L);
        user.setWinPercentage(0.0);

        User savedUser = userRepository.save(user);
        return UserMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO) {
        return userRepository.findById(userRequestDTO.getId())
                .map(existingUser -> {
                    Optional.ofNullable(userRequestDTO.getUserName()).ifPresent(existingUser::setUserName);
                    Optional.ofNullable(userRequestDTO.getPassword()).map(passwordEncoder::encode).ifPresent(existingUser::setPassword);
                    Optional.ofNullable(userRequestDTO.getEmail()).ifPresent(existingUser::setEmail);
                    Optional.ofNullable(userRequestDTO.getRole()).ifPresent(existingUser::setRole);
                    Optional.ofNullable(userRequestDTO.getPhoneNumber()).ifPresent(existingUser::setPhoneNumber);
                    existingUser.setUpdatedAt(LocalDateTime.now());
                    userRepository.save(existingUser);
                    return UserMapper.toUserResponse(existingUser);
                })
                .orElseThrow(() -> new CustomExceptions.UserNotFound("Id " + userRequestDTO.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userName")
    public UserResponseDTO getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomExceptions.UserNotFound("Username " + userName));
        return UserMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public Boolean deleteUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomExceptions.UserNotFound("Username " + userName));
        userRepository.delete(user);
        return true;
    }
    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomExceptions.UserNotFound(userName));
    }


}
