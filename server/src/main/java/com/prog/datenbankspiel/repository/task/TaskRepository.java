package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByTitle(String title);
    List<Task> findAllByLevelDifficulty(LevelDifficulty levelDifficulty);
}
