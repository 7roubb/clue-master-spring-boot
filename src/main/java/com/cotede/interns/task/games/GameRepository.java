package com.cotede.interns.task.games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByRoomId(Long roomId);
    Optional<Game> findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);

}
