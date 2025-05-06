package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Hint;
import com.prog.datenbankspiel.model.task.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
