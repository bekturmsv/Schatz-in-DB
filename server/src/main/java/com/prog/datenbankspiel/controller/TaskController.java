package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.task.*;
import com.prog.datenbankspiel.mappers.PlayerAnwerMapper;
import com.prog.datenbankspiel.mappers.TaskMapper;
import com.prog.datenbankspiel.mappers.TopicMapper;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.User;
import com.prog.datenbankspiel.repository.task.*;
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
import java.util.Map;


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
    private final PlayerTestAnswerRepository playerTestAnswerRepository;
    private final PlayerTaskAnswerRepository playerTaskAnswerRepository;
    private final PlayerAnwerMapper playerAnwerMapper;
    private final TopicMapper topicMapper;

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

        if (correct) {
            PlayerTaskAnswer answer = new PlayerTaskAnswer();
            answer.setTask(task);
            answer.setPlayerId(player.getId());
            answer.setAnswer(request.getAnswer());
            answer.setPointsEarned(correct ? task.getPoints() : 0);
            answer.setDate(LocalDateTime.now());
            answerRepository.save(answer);
            player.setTotal_points(player.getTotal_points() + task.getPoints());
            userRepository.save(player);
            return ResponseEntity.ok(new SubmitResponse(correct, answer.getPointsEarned()));
        }

        return ResponseEntity.ok(new SubmitResponse(correct, 0L));
    }


    @GetMapping("/progress")
    public ResponseEntity<PlayerProgressDto> getPlayerProgress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Player player = (Player) userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(new PlayerProgressDto(player.getUsername(), player.getTotal_points()));
    }

    @GetMapping("/{level}/topics")
    public ResponseEntity<?> getTopics(@PathVariable LevelDifficulty  level){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = (Player) userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
        if (!hasAccess(player.getId(), level)) {
            return ResponseEntity.status(403)
                    .body("You don't have access to this level!");
        }

        List<TopicDto> topics = topicRepository.findAllByLevelDifficulty(level)
                .stream()
                .map(topicMapper::toDto)
                .toList();
        List<PlayerTaskAnswerDto> taskAnswers = playerTaskAnswerRepository
                .findAllByPlayerId(player.getId())
                .stream()
                .map(playerAnwerMapper::taskToDto)
                .toList();

        List<PlayerTestAnswerDto> testAnswers = playerTestAnswerRepository
                .findAllByPlayerId(player.getId())
                .stream()
                .map(playerAnwerMapper::testToDto)
                .toList();
        return ResponseEntity.ok(Map.of(
                "tasks",        topics,
                "taskAnswers",  taskAnswers,
                "testAnswers",  testAnswers
        ));
    }

    @GetMapping("/{difficulty}/{topicName}/tasks")
    public ResponseEntity<?> getAllTasks(@PathVariable String difficulty,
                                         @PathVariable String topicName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = (Player) userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
        LevelDifficulty level = LevelDifficulty.valueOf(difficulty.toUpperCase());
        if (!hasAccess(player.getId(), level)) {
            return ResponseEntity.status(403)
                    .body("You don't have access to this level!");
        }
        Topic topic = topicRepository.findByName(topicName.trim()).orElseThrow();

        List<TaskDto> tasks = taskRepository.findByLevelDifficultyAndTopic(level, topic)
                .stream()
                .map(taskMapper::toDto)
                .toList();

        List<PlayerTaskAnswerDto> taskAnswers = playerTaskAnswerRepository
                .findAllByPlayerId(player.getId())
                .stream()
                .map(playerAnwerMapper::taskToDto)
                .toList();

        List<PlayerTestAnswerDto> testAnswers = playerTestAnswerRepository
                .findAllByPlayerId(player.getId())
                .stream()
                .map(playerAnwerMapper::testToDto)
                .toList();
        return ResponseEntity.ok(Map.of(
                "tasks",        tasks,
                "taskAnswers",  taskAnswers,
                "testAnswers",  testAnswers
        ));
    }


    @GetMapping("/{difficulty}/{topicName}/tasks/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String difficulty,
                                         @PathVariable String topicName,
                                         @PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Player player = (Player) userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
        LevelDifficulty level = LevelDifficulty.valueOf(difficulty.toUpperCase());
        if (!hasAccess(player.getId(), level)) {
            return ResponseEntity.status(403)
                    .body("You don't have access to this level!");
        }
        Task task = taskRepository.findById(Long.valueOf(id)).orElse(null);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskMapper.toDto(task));
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


    private boolean hasAccess(Long playerId, LevelDifficulty requested) {

        return switch (requested) {
            case EASY -> true;
            case MEDIUM ->
                    playerTestAnswerRepository
                            .existsByTest_LevelDifficultyAndPlayerId(requested, playerId);
            case HARD   ->
                    playerTestAnswerRepository
                            .existsByTest_LevelDifficultyAndPlayerId(requested, playerId);
        };
    }
}
