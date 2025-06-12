package pti.softwareentwicklg.SchatzInDb.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pti.softwareentwicklg.SchatzInDb.dto.ProgressDto;
import pti.softwareentwicklg.SchatzInDb.dto.TaskStatDto;
import pti.softwareentwicklg.SchatzInDb.dto.UserLoginDto;
import pti.softwareentwicklg.SchatzInDb.dto.request.AuthRequest;
import pti.softwareentwicklg.SchatzInDb.dto.request.RegisterRequest;
import pti.softwareentwicklg.SchatzInDb.dto.response.LoginResponse;
import pti.softwareentwicklg.SchatzInDb.dto.response.PlayerRegisterResponse;
import pti.softwareentwicklg.SchatzInDb.model.enums.Roles;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.task.UserSolution;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.Theme;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.UserSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.ThemeRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.service.JwtService;
import pti.softwareentwicklg.SchatzInDb.service.UserDetailsServiceImpl;
import pti.softwareentwicklg.SchatzInDb.service.task.TaskService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final ThemeRepository themeRepository;
    private final UserSolutionRepository userSolutionRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String jwt = jwtService.generateToken(userDetails);

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        Player player = new Player();
        player.setUsername(request.getUsername());
        player.setPassword(passwordEncoder.encode(request.getPassword()));
        player.setEmail(request.getEmail());
        player.setFirstName(request.getFirstName());
        player.setLastName(request.getLastName());
        player.setRole(Roles.PLAYER);
        player.setTotal_points(0L);
        player.setLevel_id(1L);
        player.setDesign("default");
        Theme defaultTheme = themeRepository.findByName("default")
                .orElseThrow(() -> new RuntimeException("Default theme not found"));
        player.setPurchasedThemes(Set.of(defaultTheme));

        player.setMatriculation_number(request.getMatriculationNumber());
        player.setSpecialist_group(request.getSpecialistGroup());

        userRepository.save(player);

        PlayerRegisterResponse dto = new PlayerRegisterResponse();
        dto.setId(player.getId());
        dto.setUsername(player.getUsername());
        dto.setEmail(player.getEmail());
        dto.setFirstName(player.getFirstName());
        dto.setLastName(player.getLastName());
        dto.setNickname(player.getUsername());
        dto.setRole(player.getRole().name());

        return ResponseEntity.ok(dto);
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