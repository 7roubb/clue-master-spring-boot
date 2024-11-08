package com.cotede.interns.task.rooms;

import com.cotede.interns.task.games.Game;
import com.cotede.interns.task.games.GameRepository;
import com.cotede.interns.task.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final MessageSource messageSource;
    private final GameRepository gameRepository;

    @PostMapping
    public ApiResponse<RoomResponseDTO> createRoom(@Valid @RequestBody RoomRequestDTO roomRequest) {
        RoomResponseDTO createdRoom = roomService.createRoom(roomRequest);
        return ApiResponse.success(createdRoom, HttpStatus.CREATED, "room.created.success");
    }

    @GetMapping("/available")
    public ApiResponse<Page<RoomResponseDTO>> getAvailableRooms(@PageableDefault(size = 10) Pageable pageable) {
        Page<RoomResponseDTO> availableRooms = roomService.getAvailableRooms(pageable);
        return ApiResponse.success(availableRooms, HttpStatus.OK, "room.found.success");
    }

    @PostMapping("/join")
    public ApiResponse<RoomResponseDTO> joinRoom(@RequestHeader Long roomId) {
        RoomResponseDTO room = roomService.joinRoom(roomId);
        return ApiResponse.success(room, HttpStatus.OK, "player.added.success");
    }

    @PostMapping("/start-game")
    public ApiResponse<Void> startGame(@RequestHeader Long roomId) {
        String message = messageSource.getMessage("game.started.success", new Object[]{roomId}, LocaleContextHolder.getLocale());
        roomService.startGame(roomId);
        return ApiResponse.success(null, HttpStatus.OK, message);
    }

    @PostMapping("/end-game")
    public ApiResponse<Void> endGame(@RequestHeader Long roomId) {
        Optional<Game> game = gameRepository.findByRoomId(roomId);
        if (game.isEmpty()) {
            return ApiResponse.error( "game.not.found", HttpStatus.NOT_FOUND);
        }
        String message = messageSource.getMessage("game.end.success", new Object[]{game.get().getId(), roomId}, LocaleContextHolder.getLocale());
        roomService.endGame(roomId);
        return ApiResponse.success(null, HttpStatus.OK, message);
    }
    @GetMapping
    public ApiResponse<RoomResponseDTO> getRoom(@RequestHeader Long roomId) {
        RoomResponseDTO roomDTO = roomService.getRoom(roomId);
        return  ApiResponse.success(roomDTO, HttpStatus.OK, "room.get.success");
    }
}
