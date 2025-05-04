package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LevelRepository extends JpaRepository<Level, Long> {

    Level findByDifficulty(LevelDifficulty difficulty);

    boolean existsByDifficulty(LevelDifficulty difficulty);
}
