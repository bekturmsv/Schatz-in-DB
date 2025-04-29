package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.task.*;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.service.PlayerService;
import com.prog.datenbankspiel.service.TaskService;
import com.prog.datenbankspiel.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TopicService topicService;

    @PostMapping("/create/topic")
    public ResponseEntity<Topic> createTopic(@RequestBody TopicDto topicDto) {
        Topic topic = topicService.createTopic(topicDto);
        return ResponseEntity.ok(topic);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/query")
    public ResponseEntity<TaskQuery> createTaskQuery(@RequestBody TaskQueryDto taskQueryDto) {
        TaskQuery task = taskService.createTaskQuery(taskQueryDto);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/test")
    public ResponseEntity<TaskTest> createTaskTest(@RequestBody TaskTestDto taskTestDto) {
        TaskTest task = taskService.createTaskTest(taskTestDto);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/dragdrop")
    public ResponseEntity<TaskDragAndDrop> createTaskDragAndDrop(@RequestBody TaskDragAndDropDto taskDragAndDropDto) {
        TaskDragAndDrop task = taskService.createTaskDragAndDrop(taskDragAndDropDto);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/submit/query/{playerId}")
    public ResponseEntity<Boolean> submitQueryAnswer(
            @PathVariable Long playerId,
            @RequestBody PlayerQueryAnswerDto dto) {
        return ResponseEntity.ok(playerService.submitQuerySolution(dto, playerId));
    }

    @PostMapping("/submit/dragdrop/{playerId}")
    public ResponseEntity<Boolean> submitDragAndDropAnswer(
            @PathVariable Long playerId,
            @RequestBody PlayerDragAndDropAnswerDto dto) {
        return ResponseEntity.ok(playerService.submitDragAndDropSolution(dto, playerId));
    }

    @PostMapping("/submit/test/{playerId}")
    public ResponseEntity<Boolean> submitTestAnswer(
            @PathVariable Long playerId,
            @RequestBody PlayerTestAnswerDto dto) {
        return ResponseEntity.ok(playerService.submitTestSolution(dto, playerId));
    }


    @GetMapping
    public ResponseEntity<List<AbstractTask>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/topic")
    public ResponseEntity<List<AbstractTask>> getTasksByTopic(@RequestParam Long topicId) {
        return ResponseEntity.ok(taskService.getTasksByTopic());
    }

    @GetMapping("/difficulty")
    public ResponseEntity<List<AbstractTask>> getTasksByDifficulty(@RequestParam String difficulty) {
        return ResponseEntity.ok(taskService.getTasksByDifficulty());
    }

    @GetMapping("/level")
    public ResponseEntity<List<AbstractTask>> getTasksByLevel(@RequestParam Long levelId) {
        return ResponseEntity.ok(taskService.getTasksByLevel());
    }

    @GetMapping("/level/topic")
    public ResponseEntity<List<AbstractTask>> getTasksByLevelAndTopic(@RequestParam Long levelId, @RequestParam Long topicId) {
        return ResponseEntity.ok(taskService.getTasksByLevelAndTopic());
    }

    @GetMapping("/finished")
    public ResponseEntity<List<AbstractTask>> getFinishedTasks(@RequestParam Long userId) {
        return ResponseEntity.ok(taskService.getFinishedTasks());
    }

    @GetMapping("/query/{id}")
    public ResponseEntity<AbstractTask> getTaskQueryById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<AbstractTask> getTaskTestById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/dragdrop/{id}")
    public ResponseEntity<AbstractTask> getTaskDragAndDropById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
