package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Test;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface TestRepository extends JpaRepository<Test, Long> {
    Test findByLevelDifficulty(@Param("level") LevelDifficulty difficulty);

    boolean existsByLevelDifficulty(LevelDifficulty levelDifficulty);
}
