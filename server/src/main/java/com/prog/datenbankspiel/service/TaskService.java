package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.front.AllLevelTasksDto;
import com.prog.datenbankspiel.dto.task.*;
import com.prog.datenbankspiel.model.task.*;

import java.util.List;

public interface TaskService {

    AllLevelTasksDto getAllTasksGrouped();

    // --- Task Creation ---
    TaskQuery createTaskQuery(CreateTaskQueryRequest dto);
    TaskTest createTaskTest(CreateTaskTestRequest dto);

    AbstractTaskRequest getTaskById(Long id);

    AbstractTaskRequest getTaskQueryById(Long id);

    TaskTestRequest getTaskTestById(Long id);

    void deleteTask(Long id);

    List<Task> getAllTasks();

    List<Task> getTasksByTopic(Long topicId);

    List<Task> getTasksByDifficulty(String difficulty);

    List<Task> getTasksByLevel(Long levelId);

    List<Task> getFinishedTasks(Long userId);

    // --- DTO-based Responses ---
    List<AbstractTaskRequest> getLevelTaskQuery(Long levelId);
    List<AbstractTaskRequest> getLevelTests(Long levelId);
}




