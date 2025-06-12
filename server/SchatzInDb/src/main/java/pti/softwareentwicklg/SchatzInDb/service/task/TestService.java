package pti.softwareentwicklg.SchatzInDb.service.task;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pti.softwareentwicklg.SchatzInDb.dto.TaskWithSolvedDto;
import pti.softwareentwicklg.SchatzInDb.dto.request.RatingRequest;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.task.TestSolution;
import pti.softwareentwicklg.SchatzInDb.model.task.UserSolution;
import pti.softwareentwicklg.SchatzInDb.model.user.User;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.TestSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.UserSolutionRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;
import pti.softwareentwicklg.SchatzInDb.utils.SqlUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {

    private final TaskRepository taskRepository;
    private final UserSolutionRepository userSolutionRepository;
    private final JdbcTemplate jdbcTemplate;
    private final TestSolutionRepository testSolutionRepository;
    private final UserRepository userRepository;

    public TestService(TaskRepository taskRepository, UserSolutionRepository userSolutionRepository, @Qualifier("taskJdbcTemplate")JdbcTemplate jdbcTemplate, TestSolutionRepository testSolutionRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userSolutionRepository = userSolutionRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.testSolutionRepository = testSolutionRepository;
        this.userRepository = userRepository;
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
        Set<String> allowed = Set.of("verdaechtiger", "fahrzeug", "fall", "fall_fahrzeug", "fall_verdaechtiger", "fall_zeuge", "task", "user_solution", "zeuge");
        return tableName != null && allowed.contains(tableName.toLowerCase());
    }

    public RatingRequest getRating() {
        List<TestSolution> testSolutions = testSolutionRepository.findByCorrectTrue();

        List<RatingRequest.Rating> easy = new ArrayList<>();
        List<RatingRequest.Rating> medium = new ArrayList<>();
        List<RatingRequest.Rating> hard = new ArrayList<>();

        for (TestSolution solution : testSolutions) {
            String username = userRepository.findById(solution.getUserId())
                    .map(User::getUsername)
                    .orElse("Unknown");

            RatingRequest.Rating rating = new RatingRequest.Rating(
                    username,
                    solution.getSpentTimeInSeconds()
            );

            switch (solution.getSchwierigkeitsgrad()) {
                case EASY -> easy.add(rating);
                case MEDIUM -> medium.add(rating);
                case HARD -> hard.add(rating);
            }
        }

        Comparator<RatingRequest.Rating> byTime = Comparator.comparingInt(RatingRequest.Rating::getSpentTimeInSeconds);

        easy.sort(byTime);
        medium.sort(byTime);
        hard.sort(byTime);

        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setRatingForEasy(easy);
        ratingRequest.setRatingForMedium(medium);
        ratingRequest.setRatingForHard(hard);

        return ratingRequest;
    }


}