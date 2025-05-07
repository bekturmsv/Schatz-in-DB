package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.model.task.Task;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDto dto);
    Task updateTask(Long id, TaskDto dto);
    void deleteTask(Long id);
    List<Task> getAllTasks();
}
