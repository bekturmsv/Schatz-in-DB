package com.prog.datenbankspiel.repository.task;


import com.prog.datenbankspiel.model.task.TaskTest;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TaskTestRepository extends JpaRepository<TaskTest, Long> {
    List<TaskTest> findByLevel_Id(Long levelId);

    List<TaskTest> findByDifficulty(LevelDifficulty difficulty);
}
