package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.TaskDto;
import com.prog.datenbankspiel.dto.test.TestAnswer;
import com.prog.datenbankspiel.dto.test.TestDto;
import com.prog.datenbankspiel.dto.test.TestRatingDto;
import com.prog.datenbankspiel.mappers.TaskMapper;
import com.prog.datenbankspiel.mappers.TestMapper;
import com.prog.datenbankspiel.mappers.TestRatingMapper;
import com.prog.datenbankspiel.model.task.PlayerTestAnswer;
import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.Test;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.User;
import com.prog.datenbankspiel.repository.task.PlayerTestAnswerRepository;
import com.prog.datenbankspiel.repository.task.TaskRepository;
import com.prog.datenbankspiel.repository.task.TestRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import com.prog.datenbankspiel.repository.user.PlayerRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final TestMapper testMapper;
    private final TaskMapper taskMapper;
    private final TopicRepository topicRepository;
    private final PlayerTestAnswerRepository playerTestAnswerRepository;
    private final PlayerRepository playerRepository;
    private final TestRatingMapper testRatingMapper;
    private final UserRepository userRepository;

    @Override
    public List<TestRatingDto> getRatings(LevelDifficulty difficulty) {
        List<PlayerTestAnswer> answers = playerTestAnswerRepository.findAllByTest_LevelDifficulty(difficulty);
        return answers.stream().map(answer -> {
            String username = playerRepository.findById(answer.getPlayerId())
                    .map(Player::getUsername)
                    .orElse("Unknown");
            TestRatingDto dto = testRatingMapper.toDto(answer);
            dto.setUsername(username);
            return dto;
        }).toList();
    }

    @Override
    public Map<String, Object> submitTestAnswer(String difficulty, TestAnswer request, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!(user instanceof Player player)) {
            throw new AccessDeniedException("Only players can submit answers.");
        }

        Test test = testRepository.findByLevelDifficulty(LevelDifficulty.valueOf(difficulty.toUpperCase()));
        List<Task> tasks = test.getTestTaskList();

        Map<Long, String> submittedAnswers = request.getAnswers();
        if (submittedAnswers == null || submittedAnswers.isEmpty()) {
            return Map.of(
                    "success", false,
                    "message", "No answers submitted."
            );
        }

        List<Task> notFinishedTasks = new ArrayList<>();

        for (Task task : tasks) {
            String submitted = submittedAnswers.get(task.getId());
            if (submitted == null || !task.getTaskAnswer().trim().equalsIgnoreCase(submitted.trim())) {
                notFinishedTasks.add(task);
            }
        }

        if (!notFinishedTasks.isEmpty()) {
            List<String> numberedTitles = new ArrayList<>();
            for (int i = 0; i < notFinishedTasks.size(); i++) {
                numberedTitles.add((i + 1) + ". " + notFinishedTasks.get(i).getTitle());
            }

            String message = "Test not completed. Not finished tasks: " + String.join(", ", numberedTitles);

            return Map.of(
                    "success", false,
                    "message", message
            );
        }

        PlayerTestAnswer testAnswer = new PlayerTestAnswer();
        testAnswer.setTest(test);
        testAnswer.setPlayerId(player.getId());
        testAnswer.setPointsEarned(test.getPointsEarned());
        testAnswer.setDate(LocalDateTime.now());
        testAnswer.setTimeSpent(request.getTimeSpent());
        playerTestAnswerRepository.save(testAnswer);

        player.setTotal_points(player.getTotal_points() + test.getPointsEarned());
        userRepository.save(player);

        return Map.of(
                "success", true,
                "message", "🎉 Test completed successfully!",
                "pointsEarned", test.getPointsEarned()
        );
    }

    @Transactional
    @Override
    public Test getTestByDifficulty(LevelDifficulty level) {
        Test test = testRepository.findByLevelDifficulty(level);
        if (test == null) {
            throw new EntityNotFoundException("No test found for difficulty: " + level);
        }
        return test;
    }

    @Override
    public Test createTest(TestDto dto) {

        boolean exists = testRepository.existsByLevelDifficulty(dto.getLevelDifficulty());
        if (exists) {
            throw new IllegalArgumentException("A test with difficulty " + dto.getLevelDifficulty() + " already exists.");
        }

        Test test = testMapper.fromDto(dto);

        List<Task> tasks = test.getTestTaskList();
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
                task.setTest(test);

                TaskDto taskDto = findTaskDtoById(dto, task.getId());

                if (taskDto != null) {
                    if (taskDto.getTopicId() != null) {
                        Topic topic = topicRepository.findById(taskDto.getTopicId())
                                .orElseThrow(() -> new IllegalArgumentException("Topic ID not found"));
                        task.setTopic(topic);
                    } else if (taskDto.getTopicName() != null) {
                        Topic topic = topicRepository.findByName(taskDto.getTopicName())
                                .orElseGet(() -> {
                                    Topic newTopic = new Topic();
                                    newTopic.setName(taskDto.getTopicName());
                                    newTopic.setLevelDifficulty(task.getLevelDifficulty());
                                    return topicRepository.save(newTopic);
                                });
                        task.setTopic(topic);
                    }
                }
            }
        }

        test.setTestTaskList(tasks);
        return testRepository.save(test);
    }

    @Override
    public void deleteTestByDifficulty(LevelDifficulty difficulty) {
        Test test = testRepository.findByLevelDifficulty(difficulty);
        if (test == null) {
            throw new EntityNotFoundException("❌ No test found with difficulty: " + difficulty);
        }
        testRepository.delete(test);
    }




    private TaskDto findTaskDtoById(TestDto dto, Long taskId) {
        if (dto.getTestTaskList() == null) return null;
        return dto.getTestTaskList().stream()
                .filter(t -> Objects.equals(t.getId(), taskId))
                .findFirst()
                .orElse(null);
    }



}
