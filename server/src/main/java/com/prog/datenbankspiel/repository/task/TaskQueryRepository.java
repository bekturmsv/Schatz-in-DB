package com.prog.datenbankspiel.repository.task;


import com.prog.datenbankspiel.model.task.TaskQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskQueryRepository extends JpaRepository<TaskQuery, Long> {
}
