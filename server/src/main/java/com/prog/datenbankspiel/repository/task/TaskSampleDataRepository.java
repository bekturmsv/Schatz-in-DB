package com.prog.datenbankspiel.repository.task;


import com.prog.datenbankspiel.model.task.TaskSampleData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskSampleDataRepository extends JpaRepository<TaskSampleData, Long> {
}
