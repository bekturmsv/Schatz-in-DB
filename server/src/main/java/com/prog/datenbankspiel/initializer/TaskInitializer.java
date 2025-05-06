package com.prog.datenbankspiel.initializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.prog.datenbankspiel.dto.task.CreateTaskQueryRequest;
import com.prog.datenbankspiel.dto.task.CreateTaskTestRequest;
import com.prog.datenbankspiel.dto.task.CreateTestAnswerRequest;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskPosition;
import com.prog.datenbankspiel.repository.task.AbstractTaskRepository;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import com.prog.datenbankspiel.service.TaskService;
import jakarta.annotation.PostConstruct;
import lombok.Generated;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class TaskInitializer {
    private final TaskService taskService;
    private final TopicRepository topicRepository;
    private final LevelRepository levelRepository;
    private final AbstractTaskRepository abstractTaskRepository;

    @PostConstruct
    public void init() {
        try {
            this.importTasksFromCsv("src/main/resources/tasks_random.csv");
        } catch (CsvException | IOException e) {
            ((Exception)e).printStackTrace();
        }

    }

    private void importTasksFromCsv(String path) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> rows = reader.readAll();

            for(int i = 1; i < rows.size(); ++i) {
                String[] parts = (String[])rows.get(i);
                if (parts.length < 8) {
                    System.out.println("⚠️ Skipped incomplete row: " + Arrays.toString(parts));
                } else {
                    String title = parts[0].trim();
                    String description = parts[1].trim();
                    String levelStr = parts[2].trim().toUpperCase();
                    String topicName = parts[3].trim();
                    String type = parts[4].trim().toUpperCase();
                    String rightAnswer = parts[5].trim();
                    String answers = parts[6].trim();
                    String correctIndices = parts[7].trim();
                    String setupQuery = parts.length > 8 ? parts[8].trim() : "";
                    String hintText = parts.length > 9 ? parts[9].trim() : null;
                    String position = parts.length > 10 ? parts[10].trim().toUpperCase() : "REGULAR";
                    Level level = this.levelRepository.findByDifficulty(LevelDifficulty.valueOf(levelStr));
                    Topic topic = (Topic)this.topicRepository.findByName(topicName).orElseGet(() -> {
                        Topic newTopic = new Topic();
                        newTopic.setName(topicName);
                        newTopic.setDifficulty(level.getDifficulty());
                        newTopic.setLevel(level);
                        return (Topic)this.topicRepository.save(newTopic);
                    });
                    switch (type) {
                        case "QUERY":
                            TaskQuery createdQuery = this.createQueryTask(title, description, rightAnswer, level, topic, setupQuery, hintText, position);
                            System.out.println("✅ Created QUERY: " + createdQuery.getTitle());
                            break;
                        case "TEST":
                            TaskTest createdTest = this.createTestTask(title, description, answers, correctIndices, level, topic, hintText);
                            System.out.println("✅ Created TEST: " + createdTest.getTitle());
                            break;
                        default:
                            System.out.println("⚠️ Skipped unknown task type at row " + (i + 1));
                    }
                }
            }
        }

    }

    private TaskQuery createQueryTask(String title, String description, String rightAnswer, Level level, Topic topic, String setupQuery, String hintText, String taskPosition) {
        Optional<Task> existing = this.abstractTaskRepository.findByTitle(title);
        if (existing.isPresent()) {
            System.out.println("⚠️ Skipping existing QUERY task: " + title);
            return (TaskQuery)existing.get();
        } else {
            CreateTaskQueryRequest dto = new CreateTaskQueryRequest();
            dto.setTitle(title);
            dto.setDescription(description);
            dto.setSetupQuery(setupQuery);
            dto.setRightAnswer(rightAnswer);
            dto.setPoints(5L);
            dto.setDifficulty(level.getDifficulty());
            dto.setLevelId(level.getId());
            dto.setTopicId(topic.getId());
            dto.setTaskPosition(TaskPosition.valueOf(taskPosition));
            TaskQuery created = this.taskService.createTaskQuery(dto);
            this.addHintIfPresent(hintText, created);
            return created;
        }
    }

    private TaskTest createTestTask(String title, String description, String answersRaw, String correctIndicesRaw, Level level, Topic topic, String hintText) {
        Optional<Task> existing = this.abstractTaskRepository.findByTitle(title);
        if (existing.isPresent()) {
            System.out.println("⚠️ Skipping existing TEST task: " + title);
            return (TaskTest)existing.get();
        } else {
            List<String> answers = Arrays.stream(answersRaw.split(",")).map(String::trim).toList();
            Set<Integer> correctIndices = (Set)Arrays.stream(correctIndicesRaw.split(",")).filter((s) -> !s.isEmpty()).map(String::trim).mapToInt(Integer::parseInt).boxed().collect(Collectors.toSet());
            List<CreateTestAnswerRequest> answerDtos = new ArrayList();

            for(int i = 0; i < answers.size(); ++i) {
                CreateTestAnswerRequest answer = new CreateTestAnswerRequest();
                answer.setAnswerText((String)answers.get(i));
                answer.setCorrect(correctIndices.contains(i));
                answerDtos.add(answer);
            }

            CreateTaskTestRequest dto = new CreateTaskTestRequest();
            dto.setTitle(title);
            dto.setDescription(description);
            dto.setTimeLimit(60L);
            dto.setPoints(5L);
            dto.setDifficulty(level.getDifficulty());
            dto.setLevelId(level.getId());
            dto.setTopicId(topic.getId());
            dto.setAnswers(answerDtos);
            TaskTest created = this.taskService.createTaskTest(dto);
            this.addHintIfPresent(hintText, created);
            return created;
        }
    }

    private void addHintIfPresent(String hintText, Task task) {
        if (hintText != null && !hintText.isEmpty()) {
            Hint hint = new Hint();
            hint.setText(hintText);
            hint.setTask(task);
            task.setHint(hint);
            this.abstractTaskRepository.save(task);
        }

    }

    @Generated
    public TaskInitializer(final TaskService taskService, final TopicRepository topicRepository, final LevelRepository levelRepository, final AbstractTaskRepository abstractTaskRepository) {
        this.taskService = taskService;
        this.topicRepository = topicRepository;
        this.levelRepository = levelRepository;
        this.abstractTaskRepository = abstractTaskRepository;
    }
}

