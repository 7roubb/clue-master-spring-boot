package com.cotede.interns.task.cases;

import com.cotede.interns.task.round.Round;
import com.cotede.interns.task.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    boolean existsByUserAndRound(User user, Round round);
    long countByRound(Round round);
}
