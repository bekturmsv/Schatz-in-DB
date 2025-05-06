package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Hint;
import com.prog.datenbankspiel.model.task.TaskFromTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskFromTeacherRepository extends JpaRepository<TaskFromTeacher, Long> {
}
