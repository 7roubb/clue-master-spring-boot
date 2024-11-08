package com.cotede.interns.task.exceptions;

import com.cotede.interns.task.security.jwt.AuthEntryPointJwt;
import org.springframework.security.core.AuthenticationException;

public class CustomExceptions {

    public static class GameNotFoundException extends RuntimeException {
        public GameNotFoundException(String message) {
            super(message);
        }
    }

    public static class NotEnoughPlayersException extends RuntimeException {
        public NotEnoughPlayersException(String message) {
            super(message);
        }
    }

    public static class RoomFullException extends RuntimeException {
        public RoomFullException(String message) {
            super(message);
        }
    }

    public static class GameAlreadyStartedException extends RuntimeException {
        public GameAlreadyStartedException(String message) {
            super(message);
        }
    }

    public static class UserNotFound extends RuntimeException {
        private final String message;
        public UserNotFound(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        private final String username;
        public UserAlreadyExistsException(String username) {
            this.username = username;
        }
        public String getUsername() {
            return username;
        }
    }

    public static class RoomNotFoundException extends RuntimeException {
        private final Long roomId;
        public RoomNotFoundException(Long roomId) {
            this.roomId = roomId;
        }
        public Long getRoomId() {
            return roomId;
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException {
        private final String email;
        public EmailAlreadyExistsException(String email) {
            super();
            this.email = email;
        }
        public String getEmail() {
            return email;
        }
    }

    public static class PhoneNumberAlreadyExistsException extends RuntimeException {
        private final String phoneNumber;
        public PhoneNumberAlreadyExistsException(String phoneNumber) {
            super();
            this.phoneNumber = phoneNumber;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }
    }


    public static class InvalidCredentialsException extends AuthenticationException  {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class ExpiredJwtTokenException extends AuthenticationException {
        public ExpiredJwtTokenException(String message) {
            super(message);
        }
    }
    public static  class PhoneOrEmailRequiredException extends RuntimeException {
        public PhoneOrEmailRequiredException() {
            super();
        }
    }

    public static class PlayerAlreadyInRoomException extends RuntimeException {
        public PlayerAlreadyInRoomException(String message) {
            super(message);
        }
    }
    public static class GameNotFound extends RuntimeException {
        public GameNotFound( ) {
            super();
        }
    }
    public static class ChatGptResponseException extends RuntimeException {
        public ChatGptResponseException() {
            super();
        }
    }
    public static class PlayerNotEnough extends RuntimeException {
        public PlayerNotEnough() {
            super();
        }
    }
    public static class FaildToSaveInCache extends RuntimeException {
        public FaildToSaveInCache(String message) {
            super(message);
        }
    }
    public static class FaildToRetrieveCase extends RuntimeException {
        public FaildToRetrieveCase(String message) {
            super(message);
        }
    }
    public static class RoundNotFound extends RuntimeException {
        public RoundNotFound( ) {
            super();
        }
    }
    public static class RoundAlreadyCompleted extends RuntimeException {
        public RoundAlreadyCompleted( ) {
            super();
        }
    }
    public static class AnswerAlreadySubmitted extends RuntimeException {
        public AnswerAlreadySubmitted( ) {
            super();
        }
    }
    public static class CaseNotFound extends RuntimeException {
        public CaseNotFound() {
            super();
        }
    }
    public static class GameAlreadyEnded extends RuntimeException {
        public GameAlreadyEnded() {
            super();
        }
    }

}
