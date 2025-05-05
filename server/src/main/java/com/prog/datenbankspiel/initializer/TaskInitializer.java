package com.prog.datenbankspiel.initializer;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.prog.datenbankspiel.dto.task.CreateTaskQueryRequest;
import com.prog.datenbankspiel.dto.task.CreateTaskTestRequest;
import com.prog.datenbankspiel.dto.task.CreateTestAnswerRequest;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import com.prog.datenbankspiel.service.TaskService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskInitializer {

    private final TaskService taskService;
    private final TopicRepository topicRepository;
    private final LevelRepository levelRepository;
    private final com.prog.datenbankspiel.repository.task.AbstractTaskRepository abstractTaskRepository;

    @PostConstruct
    public void init() {
        try {
            importTasksFromCsv("src/main/resources/tasks_random.csv");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private void importTasksFromCsv(String path) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) { // skip header
                String[] parts = rows.get(i);

                String title = parts[0].trim();
                String description = parts[1].trim();
                String setupQuery = parts.length > 8 ? parts[8].trim() : "";
                String levelStr = parts[2].trim().toUpperCase();
                String topicName = parts[3].trim();
                String type = parts[4].trim().toUpperCase();
                String rightAnswer = parts[5].trim();
                String answers = parts[6].trim();
                String correctIndices = parts[7].trim();

                Level level = levelRepository.findByDifficulty(LevelDifficulty.valueOf(levelStr));
                Topic topic = topicRepository.findByName(topicName)
                        .orElseGet(() -> {
                            Topic newTopic = new Topic();
                            newTopic.setName(topicName);
                            newTopic.setDifficulty(level.getDifficulty());
                            newTopic.setLevel(level);
                            return topicRepository.save(newTopic);
                        });


                if ("QUERY".equals(type)) {
                    TaskQuery created = createQueryTask(title, description, rightAnswer, level, topic, setupQuery);
                    System.out.println("Created QUERY: " + created.getTitle());
                } else if ("TEST".equals(type)) {
                    TaskTest created = createTestTask(title, description, answers, correctIndices, level, topic);
                    System.out.println("Created TEST: " + created.getTitle());
                } else {
                    System.out.println("⚠️ Skipped unknown task type at row " + (i + 1));
                }
            }
        }
    }


    private TaskQuery createQueryTask(String title, String description, String rightAnswer, Level level, Topic topic, String setupQuery) {
        Optional<AbstractTask> existing = abstractTaskRepository.findByTitle(title);
        if (existing.isPresent()) {
            System.out.println("⚠️ Skipping existing QUERY task: " + title);
            return (TaskQuery) existing.get();
        }

        CreateTaskQueryRequest dto = new CreateTaskQueryRequest();
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setSetupQuery(setupQuery);
        dto.setRightAnswer(rightAnswer);
        dto.setPoints(5L);
        dto.setDifficulty(level.getDifficulty());
        dto.setLevelId(level.getId());
        dto.setTopicId(topic.getId());
        return taskService.createTaskQuery(dto);
    }



    private TaskTest createTestTask(String title, String description, String answersRaw, String correctIndicesRaw,
                                    Level level, Topic topic) {
        Optional<AbstractTask> existing = abstractTaskRepository.findByTitle(title);
        if (existing.isPresent()) {
            System.out.println("⚠️ Skipping existing TEST task: " + title);
            return (TaskTest) existing.get();
        }

        List<String> answers = Arrays.stream(answersRaw.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        Set<Integer> correctIndices = Arrays.stream(correctIndicesRaw.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toSet());

        List<CreateTestAnswerRequest> answerDtos = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            CreateTestAnswerRequest answer = new CreateTestAnswerRequest();
            answer.setAnswerText(answers.get(i));
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

        return taskService.createTaskTest(dto);
    }
;
}

