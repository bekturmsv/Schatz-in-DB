package pti.softwareentwicklg.SchatzInDb.service.task;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pti.softwareentwicklg.SchatzInDb.dto.ProgressDto;
import pti.softwareentwicklg.SchatzInDb.dto.TaskStatDto;
import pti.softwareentwicklg.SchatzInDb.dto.UserLoginDto;
import pti.softwareentwicklg.SchatzInDb.dto.request.TestCheckRequest;
import pti.softwareentwicklg.SchatzInDb.dto.response.SqlCheckResponse;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.task.TestSolution;
import pti.softwareentwicklg.SchatzInDb.model.task.UserSolution;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.TestSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.UserSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SqlCheckService {

    private final JdbcTemplate jdbcTemplate;
    private final TaskRepository taskRepository;
    private final UserSolutionRepository userSolutionRepository;
    private final UserRepository userRepository;
    private final TestSolutionRepository testSolutionRepository;
    private final PlayerRepository playerRepository;


    public SqlCheckService(@Qualifier("taskJdbcTemplate")JdbcTemplate jdbcTemplate, TaskRepository taskRepository, UserSolutionRepository userSolutionRepository, UserRepository userRepository, TestSolutionRepository testSolutionRepository, PlayerRepository playerRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskRepository = taskRepository;
        this.userSolutionRepository = userSolutionRepository;
        this.userRepository = userRepository;
        this.testSolutionRepository = testSolutionRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public SqlCheckResponse validateUserSql(String userSql, String taskCode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isCorrect = false;
        List<Map<String, Object>> userResult = null;
        List<Map<String, Object>> expectedResult = null;

        UserLoginDto dto = buildUserLoginDto(user);

        try {
            Task task = taskRepository.findByTaskCode(taskCode)
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            userResult = jdbcTemplate.queryForList(userSql);
            expectedResult = jdbcTemplate.queryForList(task.getSolution());

            isCorrect = userResult.equals(expectedResult);
            if (!isCorrect) {
                throw new RuntimeException("Неправильный результат запроса.");
            }

            if(user instanceof Player) {
                Long points = ((Player) user).getTotal_points();
                ((Player) user).setTotal_points(points + task.getPoints());
            }

            playerRepository.save((Player) user);
            saveUserResult(taskCode,  userSql, true);

            dto = buildUserLoginDto(user);

            return new SqlCheckResponse(
                    true,
                    null,
                    userResult,
                    dto
            );

        } catch (Exception ex) {
            return new SqlCheckResponse(false, ex.getMessage(), userResult, dto);
        }
    }

    @Transactional
    public SqlCheckResponse validateUserTest(TestCheckRequest request) {
        System.out.println("Vor Saving Test: " + request);

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                throw new IllegalStateException("Authentication failed or user not logged in");
            }

            String username = auth.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Task> tasks = taskRepository.findBySchwierigkeitsgradAndTaskType(
                    request.getSchwierigkeit(), TaskType.TEST);

            if (tasks == null || tasks.isEmpty()) {
                throw new IllegalStateException("No TEST tasks found for difficulty: " + request.getSchwierigkeit());
            }

            List<UserSolution> userSolutions = userSolutionRepository.findByUserId(user.getId());
            if (userSolutions == null) {
                throw new IllegalStateException("User solutions could not be loaded for user ID: " + user.getId());
            }

            Set<String> correctlySolvedCodes = userSolutions.stream()
                    .filter(UserSolution::getCorrect)
                    .map(UserSolution::getTaskCode)
                    .collect(Collectors.toSet());

            Set<String> allTaskCodes = tasks.stream()
                    .map(Task::getTaskCode)
                    .collect(Collectors.toSet());

            Set<String> userSolvedCodes = userSolutions.stream()
                    .map(UserSolution::getTaskCode)
                    .collect(Collectors.toSet());

            if (!userSolvedCodes.containsAll(allTaskCodes)) {
                throw new Exception("Not all test tasks have been solved.");
            }

            boolean isCorrect = allTaskCodes.stream().allMatch(correctlySolvedCodes::contains);

            saveTestResult(request, isCorrect);

            return new SqlCheckResponse(isCorrect, null);

        } catch (Exception e) {
            return new SqlCheckResponse(false, e.getMessage());
        }
    }




    private void saveUserResult(String taskCode, String userSql, boolean isCorrect){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserSolution solution = new UserSolution();
        solution.setTaskCode(taskCode);
        solution.setUserSql(userSql);
        solution.setCorrect(isCorrect);
        solution.setUserId(user.getId());
        userSolutionRepository.save(solution);
    }

    private void saveTestResult(TestCheckRequest request, boolean isCorrect){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TestSolution solution = new TestSolution();
        solution.setSchwierigkeitsgrad(request.getSchwierigkeit());
        solution.setSpentTimeInSeconds(request.getSpentTimeInSeconds());
        solution.setCorrect(isCorrect);
        solution.setUserId(user.getId());
        testSolutionRepository.save(solution);
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