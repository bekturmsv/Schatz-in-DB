package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.model.task.Task;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDto dto);
    Task updateTask(Long id, TaskDto dto);

    Task updateHint(Long id, String hint);

    void deleteTask(Long id);
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    List<Task> getTasksByDifficulty(String difficulty);
}
