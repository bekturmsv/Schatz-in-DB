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
    TaskDragAndDrop createTaskDragAndDrop(CreateTaskDragAndDropRequest dto);

    // --- Task Access / Queries ---
    AbstractTask getTaskById(Long id);

    AbstractTaskRequest getTaskQueryById(Long id);

    List<AbstractTask> getAllTasks();
    void deleteTask(Long id);

    // --- Filtering ---
    List<AbstractTask> getTasksByTopic(Long topicId);
    List<AbstractTask> getTasksByDifficulty(String difficulty);
    List<AbstractTask> getTasksByLevel(Long levelId);
    List<AbstractTask> getFinishedTasks(Long userId);

    // --- DTO-based Responses ---
    List<AbstractTaskRequest> getLevelTaskQueryAndDragAndDrop(Long levelId);
    List<TaskTestRequest> getLevelTests(Long levelId);

}




