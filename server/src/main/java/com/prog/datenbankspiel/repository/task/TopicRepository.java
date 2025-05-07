package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByName(String name);
}
