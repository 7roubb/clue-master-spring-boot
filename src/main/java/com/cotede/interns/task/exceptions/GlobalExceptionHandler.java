package com.cotede.interns.task.exceptions;

import com.cotede.interns.task.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(CustomExceptions.UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(CustomExceptions.UserAlreadyExistsException ex) {
        String message = messageSource.getMessage("exception.userAlreadyExists", new Object[]{ex.getUsername()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(message, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(CustomExceptions.RoomNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRoomNotFoundException(CustomExceptions.RoomNotFoundException ex) {
        String message = messageSource.getMessage("exception.roomNotFound", new Object[]{ex.getRoomId()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(message, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CustomExceptions.GameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleGameNotFoundException(CustomExceptions.GameNotFoundException ex) {
        String message = messageSource.getMessage("exception.gameNotFound", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(message, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CustomExceptions.NotEnoughPlayersException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotEnoughPlayersException(CustomExceptions.NotEnoughPlayersException ex) {
        String message = messageSource.getMessage("exception.notEnoughPlayers", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(CustomExceptions.RoomFullException.class)
    public ResponseEntity<ApiResponse<Void>> handleRoomFullException(CustomExceptions.RoomFullException ex) {
        String message = messageSource.getMessage("exception.roomFull", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(message, HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(CustomExceptions.GameAlreadyStartedException.class)
    public ResponseEntity<ApiResponse<Void>> handleGameAlreadyStartedException(CustomExceptions.GameAlreadyStartedException ex) {
        String message = messageSource.getMessage("exception.gameAlreadyStarted", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(message, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
        String message = messageSource.getMessage("exception.entityNotFound", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(message, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CustomExceptions.EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExists(CustomExceptions.EmailAlreadyExistsException ex) {
        String message = messageSource.getMessage("error.email.exists", new Object[]{ex.getEmail()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(message, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(CustomExceptions.PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handlePhoneNumberAlreadyExists(CustomExceptions.PhoneNumberAlreadyExistsException ex) {
        String message = messageSource.getMessage("error.phone.exists", new Object[]{ex.getPhoneNumber()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(message, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(CustomExceptions.UserNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(CustomExceptions.UserNotFound ex) {
        String message = messageSource.getMessage("user.not.found", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(message, HttpStatus.NOT_FOUND));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Invalid user data")
                .content(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExceptions.PhoneOrEmailRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handlePhoneOrEmailRequiredException(CustomExceptions.PhoneOrEmailRequiredException ex) {
        String message = messageSource.getMessage("error.phoneoremail.required", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
    }
    @ExceptionHandler(CustomExceptions.PlayerAlreadyInRoomException.class)
    public ResponseEntity<ApiResponse<Void>> handlePlayerAlreadyInRoomException(CustomExceptions.PlayerAlreadyInRoomException ex) {
        String message = messageSource.getMessage("room.alreadyJoined", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
    }
    @ExceptionHandler(CustomExceptions.GameNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleGameNotFound(CustomExceptions.GameNotFound ex) {
        String message = messageSource.getMessage("game.not.found", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
    }
    @ExceptionHandler(CustomExceptions.PlayerNotEnough.class)
    public ResponseEntity<ApiResponse<Void>> handlePlayerNotEnough(CustomExceptions.PlayerNotEnough ex) {
        String message = messageSource.getMessage("room.maxPlayers.min", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
    }
    @ExceptionHandler(CustomExceptions.FaildToSaveInCache.class)
    public ResponseEntity<ApiResponse<Void>> handleFaildSaveInCahce (CustomExceptions.FaildToSaveInCache ex) {
        String message = messageSource.getMessage("fail.save.incache", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR));
    }
    @ExceptionHandler(CustomExceptions.ChatGptResponseException.class)
    public ResponseEntity<ApiResponse<Void>> handleFaildSaveInCahce (CustomExceptions.ChatGptResponseException ex) {
        String message = messageSource.getMessage("chat.gpt.fail", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(CustomExceptions.AnswerAlreadySubmitted.class)
    public ResponseEntity<ApiResponse<Void>> handleAnswerAlreadySubmit (CustomExceptions.AnswerAlreadySubmitted ex) {
        String message = messageSource.getMessage("answer.already.submit", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
    }
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
//        String message = messageSource.getMessage("url.not.found", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
//        String message = messageSource.getMessage("url.not.found", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResponse.error(message, HttpStatus.INTERNAL_SERVER_ERROR));
//    }

    @ExceptionHandler(CustomExceptions.CaseNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleCaseNotFound(CustomExceptions.CaseNotFound ex) {
        String message = messageSource.getMessage("case.not.found", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(message, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CustomExceptions.GameAlreadyEnded.class)
    public ResponseEntity<ApiResponse<Void>> handleCaseNotFound(CustomExceptions.GameAlreadyEnded ex) {
        String message = messageSource.getMessage("game.end.success", new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, HttpStatus.BAD_REQUEST));
    }
}
