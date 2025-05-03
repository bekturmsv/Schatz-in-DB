package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByLevelDifficulty(LevelDifficulty difficulty);

    boolean existsByNameAndDifficulty(String name, LevelDifficulty difficulty);

    Optional<Topic> findByName(String name);
}
