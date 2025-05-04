package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.AbstractTask;
import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.model.task.TaskQuery;
import com.prog.datenbankspiel.model.task.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AbstractTaskRepository extends JpaRepository<AbstractTask, Long> {

    List<AbstractTask> findByLevel(Level level);

    List<AbstractTask> findByTopic(Topic topic);

    Collection<Object> findByLevel_Id(Long levelId);

    Optional<AbstractTask> findByTitle(String title);
}
