package pti.softwareentwicklg.SchatzInDb.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pti.softwareentwicklg.SchatzInDb.dto.ProgressDto;
import pti.softwareentwicklg.SchatzInDb.dto.TaskStatDto;
import pti.softwareentwicklg.SchatzInDb.dto.UserLoginDto;
import pti.softwareentwicklg.SchatzInDb.dto.request.ChangeGroupRequest;
import pti.softwareentwicklg.SchatzInDb.dto.request.CreateGroupRequest;
import pti.softwareentwicklg.SchatzInDb.dto.request.JoinGroupRequest;
import pti.softwareentwicklg.SchatzInDb.dto.response.GroupJoinResponse;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.task.UserSolution;
import pti.softwareentwicklg.SchatzInDb.model.user.Group;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.UserSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.GroupRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.service.user.GroupService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final TaskRepository taskRepository;
    private final UserSolutionRepository userSolutionRepository;

    public GroupController(GroupRepository groupRepository, GroupService groupService, UserRepository userRepository, PlayerRepository playerRepository, TaskRepository taskRepository, UserSolutionRepository userSolutionRepository) {
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.taskRepository = taskRepository;
        this.userSolutionRepository = userSolutionRepository;
    }

    @PostMapping("/createGroup")
    public ResponseEntity<Group> createGroup(
            @RequestBody CreateGroupRequest request,
            UriComponentsBuilder uriBuilder, Authentication auth) {

        Group group = groupService.createGroup(request, auth);

        URI uri = uriBuilder.path("/groups/{id}").buildAndExpand(group.getId()).toUri();
        return ResponseEntity.created(uri).body(group);
    }

    @GetMapping("/getAll")
    public Iterable<Group> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .toList();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable Long id) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        Set<Player> students = playerRepository.findAllByGroupId(group);

        return ResponseEntity.ok(Map.of(
                "group",     group,
                "students",  students
        ));
    }

    @PostMapping("/joinGroup")
    public ResponseEntity<?> joinGroup(@RequestBody JoinGroupRequest request, Authentication authentication) {
        String code = request.getGroupCode();
        Group group = groupRepository.findByCode(code)
                .orElse(null);

        if (group == null) {
            return ResponseEntity.badRequest().body("Invalid group code");
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Player player)) {
            return ResponseEntity.status(403).body("Only players can join groups");
        }

        player.setGroupId(group);
        playerRepository.save(player);

        UserLoginDto dto = buildUserLoginDto(user);

        return ResponseEntity.ok(new GroupJoinResponse(group.getName(), dto));
    }

    @PutMapping("/quitGroup")
    public ResponseEntity<?> quitGroup(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Player player = (Player) user;
        if (player.getGroupId() == null) {
            return ResponseEntity.badRequest().body("Student is not in a group");
        }
        player.setGroupId(null);
        playerRepository.save(player);

        UserLoginDto dto = buildUserLoginDto(user);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/changeGroup")
    public ResponseEntity<String> changeGroup(@RequestBody ChangeGroupRequest request, Authentication authentication) {
        Player player = (Player) userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findByCode(request.getGroupCode())
                .orElse(null);
        if (group == null) return ResponseEntity.badRequest().body("Group not found");

        player.setGroupId(group);
        playerRepository.save(player);

        return ResponseEntity.ok("Group changed successfully");
    }


    private UserLoginDto buildUserLoginDto(User user) {
        UserLoginDto dto = new UserLoginDto();

        Player player = playerRepository.findPlayerById(user.getId());

        List<UserSolution> solved = userSolutionRepository.findByUserId(user.getId());
        List<Task> total = taskRepository.findByTaskType(TaskType.REGULAR);
        List<Task> allTasks = taskRepository.findAll();

        int totalTasks = total.size();
        int completed = 0;

        dto.setRole(user.getRole());

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