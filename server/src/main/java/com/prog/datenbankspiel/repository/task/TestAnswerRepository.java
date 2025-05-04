package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.TestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAnswerRepository extends JpaRepository<TestAnswer, Long> {
}
