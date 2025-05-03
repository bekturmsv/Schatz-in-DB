package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.*;
import com.prog.datenbankspiel.model.task.*;

import java.util.List;

public interface TaskService {

    // --- Task Creation ---
    TaskQuery createTaskQuery(TaskQueryDto dto);
    TaskTest createTaskTest(TaskTestDto dto);
    TaskDragAndDrop createTaskDragAndDrop(TaskDragAndDropDto dto);

    // --- Task Access / Queries ---
    AbstractTask getTaskById(Long id);
    List<AbstractTask> getAllTasks();
    void deleteTask(Long id);

    // --- Filtering ---
    List<AbstractTask> getTasksByTopic(Long topicId);
    List<AbstractTask> getTasksByDifficulty(String difficulty);
    List<AbstractTask> getTasksByLevel(Long levelId);
    List<AbstractTask> getFinishedTasks(Long userId);

    // --- DTO-based Responses ---
    List<AbstractTaskDto> getLevelTaskQueryAndDragAndDrop(Long levelId);
    List<TaskTestDto> getLevelTests(Long levelId);
}




