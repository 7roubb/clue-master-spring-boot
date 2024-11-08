package com.cotede.interns.task.rooms;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByStartedIsFalse(Pageable pageable);

    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.players WHERE r.id = :roomId")
    Optional<Room> findRoomWithPlayers(@Param("roomId") Long roomId);
}
