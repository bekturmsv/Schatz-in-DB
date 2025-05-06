package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Hint;
import com.prog.datenbankspiel.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
