package com.prog.datenbankspiel.repository.task;


import com.prog.datenbankspiel.model.task.TaskQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskQuery, Long> {
}
