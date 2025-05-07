package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.TestQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestQuestionsRepository extends JpaRepository<TestQuestions, Long> {
}
