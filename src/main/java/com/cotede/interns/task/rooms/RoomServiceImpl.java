package com.cotede.interns.task.rooms;

import com.cotede.interns.task.exceptions.CustomExceptions;
import com.cotede.interns.task.games.GameService;
import com.cotede.interns.task.users.User;
import com.cotede.interns.task.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public RoomResponseDTO createRoom(RoomRequestDTO roomRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User host = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        Room room = RoomMapper.toRoomEntity(roomRequest, host);
        room.setCreatedBy(userName);
        roomRepository.save(room);

        RoomResponseDTO responseDTO = RoomMapper.toRoomResponseDTO(room);
        messagingTemplate.convertAndSend("/topic/rooms", responseDTO);

        return responseDTO;
    }

    @Override
    public Page<RoomResponseDTO> getAvailableRooms(Pageable pageable) {
        Page<Room> roomPage = roomRepository.findByStartedIsFalse(pageable);
        return roomPage.map(RoomMapper::toRoomResponseDTO);
    }


    @Override
    public RoomResponseDTO joinRoom(Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User player = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomExceptions.RoomNotFoundException(roomId));


        Optional.of(room)
                .filter(r -> !r.isStarted())
                .orElseThrow(() -> new CustomExceptions.GameAlreadyStartedException(roomId.toString()));
        Optional.of(room)
                .filter(r -> r.getPlayers().size() < r.getMaxPlayers())
                .orElseThrow(() -> new CustomExceptions.RoomFullException(roomId.toString()));

        room.getPlayers().add(player);
        roomRepository.save(room);
        RoomResponseDTO responseDTO = RoomMapper.toRoomResponseDTO(room);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, responseDTO);

        return responseDTO;
    }

    @Transactional
    @Override
    public void startGame(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomExceptions.RoomNotFoundException(roomId));

        Optional.of(room)
                .filter(r -> r.getPlayers().size() >= 2)
                .ifPresentOrElse(
                        r -> r.setStarted(true),
                        () -> { throw new CustomExceptions.PlayerNotEnough(); }
                );

        int totalRound = room.getNumberOfRounds();
        roomRepository.save(room);
        gameService.createGame(room, room.getPlayers(), totalRound);

        RoomResponseDTO responseDTO = RoomMapper.toRoomResponseDTO(room);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, "GAME_START");
    }

    @Override
    public void endGame(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomExceptions.RoomNotFoundException(roomId));

        room.setStarted(false);
        roomRepository.save(room);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, "USER_JOIN");
    }

    @Override
    public RoomResponseDTO getRoom(Long roomId) {
        Room room = roomRepository.findRoomWithPlayers(roomId)
                .orElseThrow(() -> new CustomExceptions.RoomNotFoundException(roomId));
        return RoomMapper.toRoomResponseDTO(room);
    }



}
