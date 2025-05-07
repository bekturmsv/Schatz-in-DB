package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
