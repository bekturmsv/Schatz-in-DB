package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pti.softwareentwicklg.SchatzInDb.dto.BaseTaskDto;
import pti.softwareentwicklg.SchatzInDb.dto.TaskWithSolvedDto;
import pti.softwareentwicklg.SchatzInDb.dto.response.TestAvailabilityResponse;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.service.task.TaskService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @GetMapping("/getByDifficult")
    public ResponseEntity<?> getAllTasksWithSolvedStatus(Schwierigkeit schwierigkeit, SqlKategorie sqlKategorie, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(taskService.getAllTasksWithSolvedStatus(schwierigkeit, sqlKategorie, user));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BaseTaskDto dto = taskService.getTaskById(id, user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getLevels")
    public ResponseEntity<?> getTestForUser(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TestAvailabilityResponse response = taskService.buildTestAvailabilityResponse(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTopics")
    public ResponseEntity<?> getTaskThemesAndDifficulties(Schwierigkeit schwierigkeit) {

        List<Map<String, String>> taskSummaries = taskService.getTaskThemesAndDifficulties(schwierigkeit);

        Map<String, Object> response = new HashMap<>();
        response.put("tasks", taskSummaries);

        return ResponseEntity.ok(response);
    }
}