package com.prog.datenbankspiel.security;

import com.prog.datenbankspiel.dto.LoginResponse;
import com.prog.datenbankspiel.mappers.UserMapper;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.user.*;
import com.prog.datenbankspiel.model.user.enums.Roles;
import com.prog.datenbankspiel.repository.user.UserRepository;
import com.prog.datenbankspiel.service.LevelService;
import com.prog.datenbankspiel.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private final LevelService levelService;
    private final TaskService taskService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String jwt = jwtService.generateToken(userDetails);

        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow();

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setUser(userMapper.toDto(user));

        if (user instanceof Player player) {
            response.setUser(userMapper.toDto(player));
            Progress progress = player.getProgress();

            Map<String, Object> progressMap = new HashMap<>();
            LevelDifficulty currentDifficulty = levelService
                    .getLevelById(progress.getCurrentLevelId())
                    .getDifficulty();
            String currentTopic = taskService
                    .getTaskById(progress.getCompletedTaskIds().stream().findFirst().orElse(1L)) // пример
                    .getTopic()
                    .getName();

            progressMap.put("difficulty", currentDifficulty);
            progressMap.put("topic", currentTopic);
            progressMap.put("tasksSolved", progress.getCompletedTaskIds().size());
            progressMap.put("totalTasks", taskService.getAllTasks().size());

            response.setProgress(progressMap);

            List<Map<String, Object>> groupedTasks = new ArrayList<>();
            var allTasks = taskService.getAllTasks();
            var completed = progress.getCompletedTaskIds();

            Map<String, Map<String, Long[]>> stats = new HashMap<>();
            for (var task : allTasks) {
                String type = task.getDifficulty().name();
                String theme = task.getTopic().getName();

                stats.putIfAbsent(type, new HashMap<>());
                stats.get(type).putIfAbsent(theme, new Long[]{0L, 0L}); // {completed, total}
                stats.get(type).get(theme)[1]++; // total++

                if (completed.contains(task.getId())) {
                    stats.get(type).get(theme)[0]++; // completed++
                }
            }

            for (var typeEntry : stats.entrySet()) {
                String type = typeEntry.getKey();
                for (var themeEntry : typeEntry.getValue().entrySet()) {
                    String theme = themeEntry.getKey();
                    Long completedCount = themeEntry.getValue()[0];
                    Long totalCount = themeEntry.getValue()[1];

                    Map<String, Object> taskGroup = new HashMap<>();
                    taskGroup.put("type", type);
                    taskGroup.put("theme", theme);
                    taskGroup.put("completed", completedCount);
                    taskGroup.put("total", totalCount);
                    groupedTasks.add(taskGroup);
                }
            }

            response.setTasks(groupedTasks);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
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
            player.setCurrentTheme("default");
            player.setPurchasedThemes(List.of("default"));

            Progress progress = new Progress();
            progress.setUser(player);
            progress.setCurrentLevelId(1L);
            progress.setCompletedTaskIds(new HashSet<>());
            player.setProgress(progress);

            userRepository.save(player);
            return ResponseEntity.ok(userMapper.toPlayerDto(player));
        } else if (role == Roles.TEACHER) {
            Teacher teacher = new Teacher();
            teacher.setUsername(request.getUsername());
            teacher.setPassword(passwordEncoder.encode(request.getPassword()));
            teacher.setEmail(request.getEmail());
            teacher.setFirstName(request.getFirstName());
            teacher.setLastName(request.getLastName());
            teacher.setRole(Roles.TEACHER);

            userRepository.save(teacher);
            return ResponseEntity.ok(userMapper.toDto(teacher));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid role");
    }
}
