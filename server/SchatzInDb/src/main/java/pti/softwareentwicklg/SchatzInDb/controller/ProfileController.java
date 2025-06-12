package pti.softwareentwicklg.SchatzInDb.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pti.softwareentwicklg.SchatzInDb.dto.PlayerProfileUpdateDto;
import pti.softwareentwicklg.SchatzInDb.dto.ProgressDto;
import pti.softwareentwicklg.SchatzInDb.dto.TaskStatDto;
import pti.softwareentwicklg.SchatzInDb.dto.UserLoginDto;
import pti.softwareentwicklg.SchatzInDb.dto.request.ChangePasswordRequest;
import pti.softwareentwicklg.SchatzInDb.dto.response.LoginResponse;
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
import pti.softwareentwicklg.SchatzInDb.service.task.TaskService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSolutionRepository userSolutionRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public ProfileController(UserRepository userRepository, PlayerRepository playerRepository, PasswordEncoder passwordEncoder, UserSolutionRepository userSolutionRepository, TaskRepository taskRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSolutionRepository = userSolutionRepository;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }


    @GetMapping("/player/getAuthorizedUser")
    public ResponseEntity<?> getById(Authentication authentication, @RequestHeader("Authorization") String authHeader) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwt = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        if (user instanceof Player player) {
            UserLoginDto dto = buildUserLoginDto(user);

            LoginResponse response = new LoginResponse();
            response.setToken(jwt);
            response.setUser(dto);
            return ResponseEntity.ok(response);
        }

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setUser(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/player/update/{id}")
    @PreAuthorize("hasRole('PLAYER')")
    public ResponseEntity<?> updatePlayerProfile(@PathVariable Long id,
                                                 @RequestBody PlayerProfileUpdateDto dto,
                                                 Authentication auth) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        if (!player.getUsername().equals(auth.getName())) {
            return ResponseEntity.status(403).body("Cannot edit another player's profile");
        }

        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setUsername(dto.getUsername());
        player.setEmail(dto.getEmail());

        return ResponseEntity.ok(playerRepository.save(player));
    }

    @PutMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(Authentication authentication,
                                                 @RequestBody ChangePasswordRequest request) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Password updated");
    }

    private UserLoginDto buildUserLoginDto(User user) {
        UserLoginDto dto = new UserLoginDto();

        Player player = playerRepository.findPlayerById(user.getId());

        List<UserSolution> solved = userSolutionRepository.findByUserId(user.getId());
        List<Task> total = taskRepository.findByTaskType(TaskType.REGULAR);
        List<Task> allTasks = taskRepository.findAll();

        int totalTasks = total.size();
        int completed = 0;

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
        dto.setEmail(player.getEmail());
        dto.setPoints((long) taskService.recalcAndSaveUserPoints(user.getId()));
        dto.setPurchasedThemes(player.getPurchasedThemes());
        dto.setCurrentTheme(player.getDesign());

        ProgressDto progressDto = new ProgressDto();
        progressDto.setTotalTasks(totalTasks);
        progressDto.setTasksSolved(completed);

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