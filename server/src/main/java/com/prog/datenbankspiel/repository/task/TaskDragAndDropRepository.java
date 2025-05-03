package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.TaskDragAndDrop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskDragAndDropRepository extends JpaRepository<TaskDragAndDrop, Long> {
    List<TaskDragAndDrop> findByLevel_Id(Long levelId);
}
