package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.dto.task.PlayerTaskAnswerDto;
import com.prog.datenbankspiel.dto.task.PlayerTestAnswerDto;
import com.prog.datenbankspiel.model.task.PlayerTestAnswer;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerTestAnswerRepository extends JpaRepository<PlayerTestAnswer, Long> {
    boolean existsByPlayerId(Long playerId);
    List<PlayerTestAnswer> findAllByPlayerId(Long playerId);
}
