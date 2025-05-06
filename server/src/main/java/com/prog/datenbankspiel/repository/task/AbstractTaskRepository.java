package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskPosition;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AbstractTaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByLevel(Level level);

    List<Task> findByTopic(Topic topic);

    Collection<Object> findByLevel_Id(Long levelId);

    Optional<Task> findByTitle(String title);

    List<Task> findByDifficulty(LevelDifficulty difficulty);

    List<Task> findByDifficultyAndTaskType(LevelDifficulty difficulty, TaskType taskType);

    List<Task> findByDifficultyAndTaskTypeAndTaskPosition(LevelDifficulty difficulty, TaskType taskType, TaskPosition taskPosition);
}
