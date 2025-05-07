package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.task.*;
import com.prog.datenbankspiel.mappers.TaskMapper;
import com.prog.datenbankspiel.model.task.PlayerTaskAnswer;
import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.User;
import com.prog.datenbankspiel.repository.task.PlayerTaskAnswerRepository;
import com.prog.datenbankspiel.repository.task.TaskRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import com.prog.datenbankspiel.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final PlayerTaskAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final TopicRepository topicRepository;

    //  PLAYER ENDPOINTS

    @PostMapping("/submit/{taskId}")
    public ResponseEntity<SubmitResponse> submitTaskAnswer(
            @PathVariable Long taskId,
            @RequestBody AnswerRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow();

        if (!(user instanceof Player player)) {
            throw new AccessDeniedException("Only players can submit answers.");
        }

        Task task = taskRepository.findById(taskId).orElseThrow();
        boolean correct = task.getTaskAnswer().trim().equalsIgnoreCase(request.getAnswer().trim());

        PlayerTaskAnswer answer = new PlayerTaskAnswer();
        answer.setTask(task);
        answer.setPlayerId(player.getId());
        answer.setAnswer(request.getAnswer());
        answer.setPointsEarned(correct ? task.getPoints() : 0);
        answer.setDate(LocalDateTime.now());
        answerRepository.save(answer);

        if (correct) {
            player.setTotal_points(player.getTotal_points() + task.getPoints());
            userRepository.save(player);
        }

        return ResponseEntity.ok(new SubmitResponse(correct, answer.getPointsEarned()));
    }


    @GetMapping("/progress")
    public ResponseEntity<PlayerProgressDto> getPlayerProgress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Player player = (Player) userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(new PlayerProgressDto(player.getUsername(), player.getTotal_points()));
    }

    // ADMIN ENDPOINTS

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto dto) {
        Task task = taskService.createTask(dto);
        return ResponseEntity.ok(taskMapper.toDto(task));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto dto) {
        Task updated = taskService.updateTask(id, dto); // handle inside service
        return ResponseEntity.ok(taskMapper.toDto(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/topic")
    public ResponseEntity<?> createTopic(@RequestBody CreateTopicRequest request) {
        if (topicRepository.findByName(request.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Topic already exists");
        }

        Topic topic = new Topic();
        topic.setName(request.getName());
        topic.setLevelDifficulty(request.getDifficulty());

        topicRepository.save(topic);
        return ResponseEntity.ok(request);
    }
}
