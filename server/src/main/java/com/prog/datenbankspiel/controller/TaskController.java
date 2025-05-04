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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TopicService topicService;

    /**
     * Create a new topic.
     * Requires ADMIN role.
     *
     * @param createTopicRequest DTO containing topic details.
     * @return Created topic.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/topic")
    public ResponseEntity<CreateTopicRequest> createTopic(@RequestBody CreateTopicRequest createTopicRequest) {
        Topic topic = topicService.createTopic(createTopicRequest);
        return ResponseEntity.ok(createTopicRequest);
    }

    /**
     * Create a new TaskQuery.
     * Requires ADMIN role.
     *
     * @param createTaskQueryRequest DTO containing task query details.
     * @return Created TaskQuery.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/query")
    public ResponseEntity<TaskQuery> createTaskQuery(@RequestBody CreateTaskQueryRequest createTaskQueryRequest) {
        TaskQuery task = taskService.createTaskQuery(createTaskQueryRequest);
        return ResponseEntity.ok(task);
    }

    /**
     * Create a new TaskTest.
     * Requires ADMIN role.
     *
     * @param createTaskTestRequest DTO containing task test details.
     * @return Created TaskTest.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/test")
    public ResponseEntity<TaskTest> createTaskTest(@RequestBody CreateTaskTestRequest createTaskTestRequest) {
        TaskTest task = taskService.createTaskTest(createTaskTestRequest);
        return ResponseEntity.ok(task);
    }

    /**
     * Create a new TaskDragAndDrop.
     * Requires ADMIN role.
     *
     * @param createTaskDragAndDropRequest DTO containing drag-and-drop task details.
     * @return Created TaskDragAndDrop.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/dragdrop")
    public ResponseEntity<TaskDragAndDrop> createTaskDragAndDrop(@RequestBody CreateTaskDragAndDropRequest createTaskDragAndDropRequest) {
        TaskDragAndDrop task = taskService.createTaskDragAndDrop(createTaskDragAndDropRequest);
        return ResponseEntity.ok(task);
    }

    /**
     * Submit a solution for a query task.
     *
     * @param playerId Player's ID.
     * @param dto Player's answer for query task.
     * @return True if the answer is correct, otherwise false.
     */
    @PostMapping("/submit/query/{playerId}")
    public ResponseEntity<Boolean> submitQueryAnswer(
            @PathVariable Long playerId,
            @RequestBody SubmitQueryRequest dto) {
        return ResponseEntity.ok(playerService.submitQuerySolution(dto, playerId));
    }

    /**
     * Submit a solution for a drag-and-drop task.
     *
     * @param playerId Player's ID.
     * @param dto Player's answer for drag-and-drop task.
     * @return True if the answer is correct, otherwise false.
     */
    @PostMapping("/submit/dragdrop/{playerId}")
    public ResponseEntity<Boolean> submitDragAndDropAnswer(
            @PathVariable Long playerId,
            @RequestBody SubmitDragAndDropRequest dto) {
        return ResponseEntity.ok(playerService.submitDragAndDropSolution(dto, playerId));
    }

    /**
     * Submit a solution for a test task.
     *
     * @param playerId Player's ID.
     * @param dto Player's answer for test task.
     * @return True if the answer is correct, otherwise false.
     */
    @PostMapping("/submit/test/{playerId}")
    public ResponseEntity<Boolean> submitTestAnswer(
            @PathVariable Long playerId,
            @RequestBody SubmitTestRequest dto) {
        return ResponseEntity.ok(playerService.submitTestSolution(dto, playerId));
    }

    /**
     * Retrieve all available tasks.
     *
     * @return List of all tasks.
     */
    @GetMapping
    public ResponseEntity<List<AbstractTask>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * Retrieve tasks by topic ID.
     *
     * @param topicId ID of the topic.
     * @return List of tasks related to the topic.
     */
    @GetMapping("/topic")
    public ResponseEntity<List<AbstractTask>> getTasksByTopic(@RequestParam Long topicId) {
        return ResponseEntity.ok(taskService.getTasksByTopic(topicId));
    }

    /**
     * Retrieve tasks by difficulty level.
     *
     * @param difficulty Difficulty level (e.g., EASY, MEDIUM, HARD).
     * @return List of tasks matching the difficulty.
     */
    @GetMapping("/difficulty")
    public ResponseEntity<List<AbstractTask>> getTasksByDifficulty(@RequestParam String difficulty) {
        return ResponseEntity.ok(taskService.getTasksByDifficulty(difficulty));
    }

    /**
     * Retrieve tasks by level ID.
     *
     * @param levelId ID of the level.
     * @return List of tasks for the level.
     */
    @GetMapping("/level")
    public ResponseEntity<List<AbstractTask>> getTasksByLevel(@RequestParam Long levelId) {
        return ResponseEntity.ok(taskService.getTasksByLevel(levelId));
    }

    /**
     * Retrieve finished tasks for a user.
     *
     * @param userId User's ID.
     * @return List of finished tasks.
     */
    @GetMapping("/finished")
    public ResponseEntity<List<AbstractTask>> getFinishedTasks(@RequestParam Long userId) {
        return ResponseEntity.ok(taskService.getFinishedTasks(userId));
    }

    /**
     * Retrieve a query task by its ID.
     *
     * @param id Task ID.
     * @return Task details.
     */
    @GetMapping("/query/{id}")
    public ResponseEntity<AbstractTaskRequest> getTaskQueryById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskQueryById(id));
    }

    /**
     * Retrieve a test task by its ID.
     *
     * @param id Task ID.
     * @return Task details.
     */
    @GetMapping("/test/{id}")
    public ResponseEntity<AbstractTask> getTaskTestById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * Retrieve a drag-and-drop task by its ID.
     *
     * @param id Task ID.
     * @return Task details.
     */
    @GetMapping("/dragdrop/{id}")
    public ResponseEntity<AbstractTask> getTaskDragAndDropById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * Retrieve all topics.
     *
     * @return Topics
     */
    @GetMapping("/all-topics")
    public ResponseEntity<List<TopicRequest>> getAllTopics() {
        List<TopicRequest> topics = topicService.getAllTopics().stream().map(topic -> {
            TopicRequest dto = new TopicRequest();
            dto.setTopicId(topic.getId());
            dto.setName(topic.getName());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(topics);
    }

    /**
     * Delete a task by ID.
     * Requires ADMIN role.
     *
     * @param id Task ID.
     * @return No content response.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
