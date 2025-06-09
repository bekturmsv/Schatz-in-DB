package pti.softwareentwicklg.SchatzInDb.service.task;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pti.softwareentwicklg.SchatzInDb.dto.TaskWithSolvedDto;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.task.UserSolution;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.UserSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.utils.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestService {

    private final TaskRepository taskRepository;
    private final UserSolutionRepository userSolutionRepository;
    private final JdbcTemplate jdbcTemplate;

    public TestService(TaskRepository taskRepository, UserSolutionRepository userSolutionRepository, @Qualifier("taskJdbcTemplate")JdbcTemplate jdbcTemplate) {
        this.taskRepository = taskRepository;
        this.userSolutionRepository = userSolutionRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TaskWithSolvedDto> getTestTasksForUser(Schwierigkeit schwierigkeit, User user) {
        List<Task> tasks =  taskRepository.findBySchwierigkeitsgradAndTaskType(schwierigkeit, TaskType.TEST);

        List<UserSolution> solvedTasks = userSolutionRepository.findByUserId(user.getId());

        Set<String> solvedTaskCodes = solvedTasks.stream()
                .map(UserSolution::getTaskCode)
                .collect(Collectors.toSet());

        List<Task> allTasks = taskRepository.findBySchwierigkeitsgradAndTaskType(schwierigkeit,TaskType.TEST);

        return allTasks.stream()
                .map(task -> {
                    String solutionSql = task.getSolution();
                    String tableName = SqlUtils.extractTableName(solutionSql);
                    List<Map<String, Object>> tableData = new ArrayList<>();

                    if (isSafeTable(tableName)) {
                        try {
                            tableData = jdbcTemplate.queryForList("SELECT * FROM " + tableName);
                        } catch (Exception e) {
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

    public boolean isTestAvailableForUser(User user, Schwierigkeit schwierigkeit) {
        List<Task> tasks = taskRepository.findBySchwierigkeitsgradAndTaskType(schwierigkeit, TaskType.REGULAR);

        List<UserSolution> userSolutions = userSolutionRepository.findByUserId(user.getId());

        Set<String> solvedTaskCodes = userSolutions.stream()
                .map(us -> us.getTaskCode())
                .collect(Collectors.toSet());

        return tasks.stream()
                .map(Task::getTaskCode)
                .allMatch(solvedTaskCodes::contains);
    }

    private boolean isSafeTable(String tableName) {
        Set<String> allowed = Set.of("verdaechtiger", "zeugnis", "student", "kurs"); // добавь свои таблицы
        return tableName != null && allowed.contains(tableName.toLowerCase());
    }
}
