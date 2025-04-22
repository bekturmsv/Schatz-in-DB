package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.Hint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HintRepository extends JpaRepository<Hint, Long> {
}
