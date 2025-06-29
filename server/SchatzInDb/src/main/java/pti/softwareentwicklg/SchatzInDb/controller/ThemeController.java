package pti.softwareentwicklg.SchatzInDb.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pti.softwareentwicklg.SchatzInDb.dto.ProgressDto;
import pti.softwareentwicklg.SchatzInDb.dto.TaskStatDto;
import pti.softwareentwicklg.SchatzInDb.dto.ThemeDto;
import pti.softwareentwicklg.SchatzInDb.dto.UserLoginDto;
import pti.softwareentwicklg.SchatzInDb.dto.response.PurchaseResponse;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.task.UserSolution;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.UserSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.service.user.ThemeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {
    private final ThemeService themeService;
    private final UserSolutionRepository userSolutionRepository;
    private final TaskRepository taskRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    public ThemeController(ThemeService themeService, UserSolutionRepository userSolutionRepository, TaskRepository taskRepository, PlayerRepository playerRepository, UserRepository userRepository) {
        this.themeService = themeService;
        this.userSolutionRepository = userSolutionRepository;
        this.taskRepository = taskRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> buyTheme(@RequestParam String name, Authentication auth) {
        try {
            User user = userRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            themeService.purchaseTheme(auth.getName(), name);


            PurchaseResponse purchaseResponse = new PurchaseResponse();
            purchaseResponse.setMessage("Purchased " + name);

            UserLoginDto dto = buildUserLoginDto(user);

            purchaseResponse.setUser(dto);

            return ResponseEntity.ok(purchaseResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/setTheme")
    public ResponseEntity<?> setTheme(@RequestParam String name, Authentication auth) {
        try {
            themeService.setTheme(auth.getName(), name);
            return ResponseEntity.ok("Set theme " + name);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemes());
    }

    private UserLoginDto buildUserLoginDto(User user) {
        UserLoginDto dto = new UserLoginDto();

        Player player = playerRepository.findPlayerById(user.getId());

        List<UserSolution> solved = userSolutionRepository.findByUserId(user.getId());
        List<Task> total = taskRepository.findByTaskType(TaskType.REGULAR);
        List<Task> allTasks = taskRepository.findAll();

        int totalTasks = total.size();
        int completed = 0;

        dto.setRoles(user.getRole());

        for (UserSolution userSolution : solved) {
            Task task = taskRepository.getTaskByTaskCode(userSolution.getTaskCode());
            if (task.getTaskType().equals(TaskType.REGULAR)) {
                completed++;
            }
        }

        dto.setId(player.getId());
        dto.setFirstname(player.getFirstName());
        dto.setLastname(player.getLastName());
        dto.setUsername(player.getUsername());
        dto.setMatriculationNumber(player.getMatriculation_number());
        dto.setSpecialistGroup(player.getSpecialist_group());
        dto.setEmail(player.getEmail());
        dto.setPoints(player.getTotal_points());
        dto.setPurchasedThemes(player.getPurchasedThemes());
        dto.setCurrentTheme(player.getDesign());
        ProgressDto progressDto = new ProgressDto();
        progressDto.setTotalTasks(totalTasks);
        progressDto.setTasksSolved(completed);
        dto.setGroup(player.getGroupId());

        dto.setProgress(progressDto);

        // Достаем все решенные код-ы задач пользователя
        Set<String> solvedCodes = userSolutionRepository
                .findByUserId(user.getId())
                .stream()
                .map(UserSolution::getTaskCode)
                .collect(Collectors.toSet());

        // теперь последние три решённых задачи:
        List<UserSolution> lastThree = userSolutionRepository
                .findTop3ByUserIdAndCorrectTrueOrderBySubmittedAtDesc(user.getId());

        List<TaskStatDto> lastTasks = lastThree.stream().map(sol -> {
            String code = sol.getTaskCode();
            Task task = taskRepository.findByTaskCode(code)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found: " + code));

            // Берём все REGULAR задачи той же темы и уровня
            List<Task> themeTasks = taskRepository
                    .findBySchwierigkeitsgradAndTaskTypeAndKategorie(
                            task.getSchwierigkeitsgrad(),
                            TaskType.REGULAR,
                            task.getKategorie()
                    );

            // Считаем, сколько из них решено этим пользователем
            long solvedCount = userSolutionRepository
                    .countByUserIdAndCorrectTrueAndTaskCodeIn(
                            user.getId(),
                            themeTasks.stream().map(Task::getTaskCode).toList()
                    );

            return new TaskStatDto(
                    task.getSchwierigkeitsgrad(),
                    task.getKategorie(),
                    code
            );
        }).collect(Collectors.toList());

        dto.setLastTasks(lastTasks);

        // Готовим структуру completedTasks и completedLevels
        Map<String, List<String>> completedTasks = new HashMap<>();
        Map<String, Boolean> completedLevels = new HashMap<>();

        for (Schwierigkeit diff : Schwierigkeit.values()) {
            // 1) берем все REGULAR задачи этого уровня
            List<Task> tasksOfLevel = taskRepository
                    .findBySchwierigkeitsgradAndTaskType(diff, TaskType.REGULAR);

            // 2) вычисляем, какие из них пользователь решил
            List<String> solvedForLevel = tasksOfLevel.stream()
                    .map(Task::getTaskCode)
                    .filter(solvedCodes::contains)
                    .collect(Collectors.toList());

            // добавляем в completedTasks (ключ — lowerCase)
            completedTasks.put(diff.name().toLowerCase(), solvedForLevel);

            // 3) уровень считается пройденным, если есть задачи и все они решены
            boolean levelDone = !tasksOfLevel.isEmpty()
                    && solvedForLevel.size() == tasksOfLevel.size();
            completedLevels.put(diff.name().toLowerCase(), levelDone);
        }

        dto.setCompletedTasks(completedTasks);
        dto.setCompletedLevels(completedLevels);

        return dto;
    }
}