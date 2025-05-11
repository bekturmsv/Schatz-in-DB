package com.prog.datenbankspiel.security;

import com.prog.datenbankspiel.dto.LoginResponse;
import com.prog.datenbankspiel.dto.PlayerLoginDto;
import com.prog.datenbankspiel.dto.PlayerLoginResponse;
import com.prog.datenbankspiel.mappers.UserMapper;
import com.prog.datenbankspiel.model.task.PlayerTaskAnswer;
import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.Teacher;
import com.prog.datenbankspiel.model.user.User;
import com.prog.datenbankspiel.model.user.enums.Roles;
import com.prog.datenbankspiel.repository.task.PlayerTaskAnswerRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import com.prog.datenbankspiel.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final PlayerTaskAnswerRepository playerTaskAnswerRepository;
    private final TaskService taskService;
    private final TopicRepository topicRepository;


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

        // PLAYER LOGIN
        if (user instanceof Player player) {
            List<Task> allTasks = taskService.getAllTasks();
            List<PlayerTaskAnswer> answers = playerTaskAnswerRepository.findByPlayerId(player.getId());

            Set<Long> completedTaskIds = answers.stream()
                    .map(answer -> answer.getTask().getId())
                    .collect(Collectors.toSet());


            Topic currentTopic = topicRepository.findById(player.getLevel_id())
                    .orElseThrow(() -> new RuntimeException("Topic not found for level_id"));

            LevelDifficulty currentDifficulty = currentTopic.getLevelDifficulty();


            Map<String, Object> progressMap = Map.of(
                    "difficulty", currentDifficulty.name(),
                    "topic", currentTopic.getName(),
                    "tasksSolved", completedTaskIds.size(),
                    "totalTasks", allTasks.size()
            );

            // Grouped Tasks
            Map<String, Map<String, Long[]>> stats = new HashMap<>();
            for (Task task : allTasks) {
                String diff = task.getLevelDifficulty().name();
                String theme = task.getTopic().getName();

                stats.computeIfAbsent(diff, k -> new HashMap<>())
                        .computeIfAbsent(theme, k -> new Long[]{0L, 0L});
                stats.get(diff).get(theme)[1]++;
                if (completedTaskIds.contains(task.getId())) {
                    stats.get(diff).get(theme)[0]++;
                }
            }

            List<Map<String, Object>> groupedTasks = new ArrayList<>();
            for (var diffEntry : stats.entrySet()) {
                for (var themeEntry : diffEntry.getValue().entrySet()) {
                    groupedTasks.add(Map.of(
                            "type", diffEntry.getKey(),
                            "theme", themeEntry.getKey(),
                            "completed", themeEntry.getValue()[0],
                            "total", themeEntry.getValue()[1]
                    ));
                }
            }

            // Completed Tasks by Difficulty
            Map<String, List<Long>> completedTasksByDifficulty = new HashMap<>();
            for (LevelDifficulty diff : LevelDifficulty.values()) {
                completedTasksByDifficulty.put(diff.name().toLowerCase(), new ArrayList<>());
            }

            for (Task task : allTasks) {
                if (completedTaskIds.contains(task.getId())) {
                    completedTasksByDifficulty.get(task.getLevelDifficulty().name().toLowerCase())
                            .add(task.getId());
                }
            }

            // Completed Levels
            Map<String, Boolean> completedLevels = new HashMap<>();
            for (LevelDifficulty diff : LevelDifficulty.values()) {
                List<Task> tasksOfDiff = allTasks.stream()
                        .filter(task -> task.getLevelDifficulty() == diff)
                        .toList();
                boolean allCompleted = !tasksOfDiff.isEmpty() &&
                        tasksOfDiff.stream().map(Task::getId).allMatch(completedTaskIds::contains);
                completedLevels.put(diff.name().toLowerCase(), allCompleted);
            }

            PlayerLoginDto playerDto = new PlayerLoginDto();
            playerDto.setId(player.getId());
            playerDto.setUsername(player.getUsername());
            playerDto.setEmail(player.getEmail());
            playerDto.setFirstname(player.getFirstName());
            playerDto.setLastname(player.getLastName());
            playerDto.setName(player.getFirstName());
            playerDto.setNickname(player.getUsername());
            playerDto.setPoints(player.getTotal_points());
            playerDto.setCurrentTheme(player.getDesign());
            playerDto.setPurchasedThemes(player.getPurchasedThemes());

            playerDto.setProgress(progressMap);
            playerDto.setTasks(groupedTasks);
            playerDto.setCompletedTasks(completedTasksByDifficulty);
            playerDto.setCompletedLevels(completedLevels);

            PlayerLoginResponse playerLoginResponse = new PlayerLoginResponse();
            playerLoginResponse.setToken(jwt);
            playerLoginResponse.setUser(playerDto);


            return ResponseEntity.ok(playerLoginResponse);
        }

        // TEACHER / ADMIN
        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setUser(userMapper.toDto(user));
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        if (request.getRole().equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Cannot register as ADMIN");
        }

        Roles role = Roles.valueOf(request.getRole().toUpperCase());

        if (role == Roles.PLAYER) {
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
            player.setPurchasedThemes(List.of("default"));

            userRepository.save(player);
            return ResponseEntity.ok(userMapper.toPlayerLoginDto(player));

        } else if (role == Roles.TEACHER) {
            Teacher teacher = new Teacher();
            teacher.setUsername(request.getUsername());
            teacher.setPassword(passwordEncoder.encode(request.getPassword()));
            teacher.setEmail(request.getEmail());
            teacher.setFirstName(request.getFirstName());
            teacher.setLastName(request.getLastName());
            teacher.setRole(Roles.TEACHER);
            teacher.setSubject(null);

            userRepository.save(teacher);
            return ResponseEntity.ok(userMapper.toDto(teacher));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
    }

}

