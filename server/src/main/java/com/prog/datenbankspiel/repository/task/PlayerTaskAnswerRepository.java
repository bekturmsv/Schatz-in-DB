package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.PlayerTaskAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerTaskAnswerRepository extends JpaRepository<PlayerTaskAnswer, Long> {
    List<PlayerTaskAnswer> findByPlayerId(Long playerId);
}
