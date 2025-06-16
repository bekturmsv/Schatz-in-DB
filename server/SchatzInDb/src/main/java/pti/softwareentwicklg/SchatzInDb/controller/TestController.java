package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pti.softwareentwicklg.SchatzInDb.dto.TaskWithSolvedDto;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.service.task.TestService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UserRepository userRepository;
    private final TestService testService;

    public TestController(UserRepository userRepository, TestService testService) {
        this.userRepository = userRepository;
        this.testService = testService;
    }

    @GetMapping("/getTestByDifficulty")
    public ResponseEntity<?> getTestForUser(Schwierigkeit schwierigkeitsgrad, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!testService.isTestAvailableForUser(user, schwierigkeitsgrad)) {
            return ResponseEntity
                    .status(403)
                    .body(Map.of("message", "The test is not available. First solve all the usual task."));
        }

        List<TaskWithSolvedDto> testTasks = testService.getTestTasksForUser(schwierigkeitsgrad, user);
        return ResponseEntity.ok(testTasks);
    }
}
