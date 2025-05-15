package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.mappers.TaskMapper;
import com.prog.datenbankspiel.model.task.Hint;
import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.repository.task.TaskRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TopicRepository topicRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task createTask(TaskDto dto) {
        Task task = taskMapper.fromDto(dto);

        // Set topic manually
        Topic topic = resolveTopic(dto);
        task.setTopic(topic);

        // Set Hint manually
        if (dto.getHint() != null && !dto.getHint().isBlank()) {
            Hint hint = new Hint();
            hint.setDescription(dto.getHint());
            hint.setTask(task);
            task.setHint(hint);
        }

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskDto dto) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskMapper.updateTaskFromDto(dto, existing);
        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    public List<Task> getTasksByDifficulty(String difficulty) {
        LevelDifficulty diff;
        try {
            diff = LevelDifficulty.valueOf(difficulty.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid difficulty: " + difficulty);
        }

        return taskRepository.findAllByLevelDifficulty(diff);
    }

    private Topic resolveTopic(TaskDto dto) {
        if (dto.getTopicId() != null) {
            return topicRepository.findById(dto.getTopicId())
                    .orElseThrow(() -> new RuntimeException("Topic not found with ID " + dto.getTopicId()));
        } else if (dto.getTopicName() != null && !dto.getTopicName().isBlank()) {
            return topicRepository.findByName(dto.getTopicName())
                    .orElseThrow(() -> new RuntimeException("Topic with name not found"));
        } else {
            throw new IllegalArgumentException("Topic information is missing");
        }
    }
}
