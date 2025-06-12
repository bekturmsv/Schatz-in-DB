package pti.softwareentwicklg.SchatzInDb.service.task;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pti.softwareentwicklg.SchatzInDb.dto.TaskWithSolvedDto;
import pti.softwareentwicklg.SchatzInDb.dto.response.TestAvailabilityResponse;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.task.UserSolution;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.UserSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.PlayerRepository;
import pti.softwareentwicklg.SchatzInDb.utils.SqlUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserSolutionRepository userSolutionRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PlayerRepository playerRepository;

    public TaskService(TaskRepository taskRepository, UserSolutionRepository userSolutionRepository, @Qualifier("taskJdbcTemplate") JdbcTemplate jdbcTemplate, PlayerRepository playerRepository) {
        this.taskRepository = taskRepository;
        this.userSolutionRepository = userSolutionRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.playerRepository = playerRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    //IdTask
    public TaskWithSolvedDto getTaskById(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        boolean solved = false;

        UserSolution userSolution = userSolutionRepository.getByUserIdAndTaskCode(user.getId(), task.getTaskCode());

        if(userSolution != null) {
            solved = userSolution.getCorrect();
        }

        String tableName = SqlUtils.extractTableName(task.getSolution());
        List<Map<String, Object>> tableData = Collections.emptyList();
        if (tableName != null && isSafeTable(tableName)) {
            try {
                tableData = jdbcTemplate.queryForList("SELECT * FROM " + tableName);
            } catch (Exception e) {
                System.err.println("Ошибка при загрузке данных из " + tableName + ": " + e.getMessage());
            }
        }

        return new TaskWithSolvedDto(
                task.getId(),
                task.getTaskCode(),
                task.getAufgabe(),
                task.getKategorie(),
                task.getSchwierigkeitsgrad(),
                task.getHint(),
                task.getTaskType(),
                task.getInteractionType(),
                solved,
                tableName,
                tableData
        );
    }

    // Levels
    public TestAvailabilityResponse buildTestAvailabilityResponse(User user) {
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

        List<TestAvailabilityResponse.LevelStatus> levels = Arrays.stream(Schwierigkeit.values())
                .map(level -> {
                    List<Task> levelTasks = allTasks.stream()
                            .filter(t -> t.getSchwierigkeitsgrad() == level)
                            .toList();

                    boolean allSolved = levelTasks.size() > 0 && levelTasks.stream()
                            .allMatch(task -> solved.stream()
                                    .anyMatch(sol -> sol.getTaskCode().equals(task.getTaskCode())));

                    return new TestAvailabilityResponse.LevelStatus(level.name(), allSolved);
                })
                .toList();

        return new TestAvailabilityResponse(completed, totalTasks, levels);
    }

    //Topics
    public List<Map<String, String>> getTaskThemesAndDifficulties(Schwierigkeit schwierigkeit) {
        List<Task> regularTasks = taskRepository.findAll().stream()
                .filter(task -> task.getTaskType() == TaskType.REGULAR && task.getSchwierigkeitsgrad() == schwierigkeit)
                .toList();

        Set<String> uniqueKeys = new HashSet<>();
        List<Map<String, String>> taskSummaries = new ArrayList<>();

        for (Task task : regularTasks) {
            String key = task.getKategorie().name() + ":" + task.getSchwierigkeitsgrad().name();
            if (uniqueKeys.add(key)) {
                Map<String, String> map = new HashMap<>();
                map.put("name", task.getKategorie().name());
                map.put("levelDifficulty", task.getSchwierigkeitsgrad().name());
                taskSummaries.add(map);
            }
        }

        return taskSummaries;
    }

    //AllTask
    public List<TaskWithSolvedDto> getAllTasksWithSolvedStatus(Schwierigkeit schwierigkeit, SqlKategorie sqlKategorie, User user) {
        List<UserSolution> solvedTasks = userSolutionRepository.findByUserId(user.getId());

        Set<String> solvedTaskCodes = solvedTasks.stream()
                .map(UserSolution::getTaskCode)
                .collect(Collectors.toSet());

        List<Task> allTasks = taskRepository.findBySchwierigkeitsgradAndTaskTypeAndKategorie(schwierigkeit,TaskType.REGULAR, sqlKategorie);

        return allTasks.stream()
                .map(task -> {
                    String solutionSql = task.getSolution();
                    String tableName = SqlUtils.extractTableName(solutionSql);
                    List<Map<String, Object>> tableData = new ArrayList<>();

                    if (isSafeTable(tableName)) {
                        try {
                            tableData = jdbcTemplate.queryForList("SELECT * FROM " + tableName);
                        } catch (Exception e) {
                            System.err.println("Ошибка при запросе таблицы '" + tableName + "': " + e.getMessage());
                        }
                    }

                    return new TaskWithSolvedDto(
                            task.getId(),
                            task.getTaskCode(),
                            task.getAufgabe(),
                            task.getKategorie(),
                            task.getSchwierigkeitsgrad(),
                            task.getHint(),
                            task.getTaskType(),
                            task.getInteractionType(),
                            solvedTaskCodes.contains(task.getTaskCode()),
                            tableName,
                            tableData
                    );
                })
                .toList();
    }

    @Transactional
    public int recalcAndSaveUserPoints(Long userId) {
        Integer sum = userSolutionRepository.findByUserId(userId).stream()
                .map(UserSolution::getTaskCode)
                .map(taskRepository::findByTaskCode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(Task::getPoints)
                .sum();
        return sum;
    }

    private boolean isSafeTable(String tableName) {
        Set<String> allowed = Set.of("verdaechtiger", "fahrzeug", "fall", "fall_fahrzeug", "fall_verdaechtiger", "fall_zeuge", "task", "user_solution", "zeuge");
        return tableName != null && allowed.contains(tableName.toLowerCase());
    }
}