package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.dto.test.TestAnswer;
import com.prog.datenbankspiel.dto.test.TestDto;
import com.prog.datenbankspiel.dto.test.TestRatingDto;
import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.Test;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface TestService {
    List<TestRatingDto> getRatings(LevelDifficulty difficulty);

    Map<String, Object> submitTestAnswer(String difficulty, TestAnswer request, Authentication authentication);

    @Transactional(readOnly = true)
    Test getTestByDifficulty(LevelDifficulty level);

    Test createTest(TestDto dto);

    void deleteTestByDifficulty(LevelDifficulty difficulty);

    Task addTaskToTest(TaskDto dto);
}
