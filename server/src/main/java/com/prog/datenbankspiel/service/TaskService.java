package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.TaskDragAndDropDto;
import com.prog.datenbankspiel.dto.task.TaskQueryDto;
import com.prog.datenbankspiel.dto.task.TaskTestDto;
import com.prog.datenbankspiel.dto.task.TopicDto;
import com.prog.datenbankspiel.model.task.*;

import java.util.List;

public interface TaskService {

    TaskQuery createTaskQuery(TaskQueryDto taskQueryDto);

    TaskTest createTaskTest(TaskTestDto taskTestDto);

    TaskDragAndDrop createTaskDragAndDrop(TaskDragAndDropDto taskDragAndDropDto);

    void deleteTask(Long id);

    AbstractTask getTaskById(Long id);

    List<AbstractTask> getAllTasks();

    List<AbstractTask> getTasksByTopic(Long topicId);
    List<AbstractTask> getTasksByDifficulty(String difficulty);
    List<AbstractTask> getTasksByLevel(Long levelId);
    List<AbstractTask> getTasksByLevelAndTopic(Long levelId, Long topicId);
    List<AbstractTask> getFinishedTasks(Long userId);
}



