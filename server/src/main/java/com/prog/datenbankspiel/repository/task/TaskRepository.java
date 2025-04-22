package com.prog.datenbankspiel.repository.task;


import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
