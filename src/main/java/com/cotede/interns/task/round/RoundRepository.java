package com.cotede.interns.task.round;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long> {
    Round getRoundByGameIdAndRoundNumber(Long gameId, Long roundNumber);
}
