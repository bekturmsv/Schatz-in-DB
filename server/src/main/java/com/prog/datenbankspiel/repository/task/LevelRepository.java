package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, Long> {
}
