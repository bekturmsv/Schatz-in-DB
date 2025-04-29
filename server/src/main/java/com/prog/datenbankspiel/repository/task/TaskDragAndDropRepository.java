package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.TaskDragAndDrop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDragAndDropRepository extends JpaRepository<TaskDragAndDrop, Long> {
}
