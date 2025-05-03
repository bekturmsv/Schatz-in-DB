package com.prog.datenbankspiel.repository.task;


import com.prog.datenbankspiel.model.task.TaskQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskQueryRepository extends JpaRepository<TaskQuery, Long> {
    List<TaskQuery> findByLevel_Id(Long levelId);
}
