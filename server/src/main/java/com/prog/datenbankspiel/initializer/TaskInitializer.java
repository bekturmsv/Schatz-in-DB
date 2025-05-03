package com.prog.datenbankspiel.initializer;

import com.prog.datenbankspiel.dto.task.CreateTaskDragAndDropRequest;
import com.prog.datenbankspiel.dto.task.CreateTaskQueryRequest;
import com.prog.datenbankspiel.dto.task.CreateTaskTestRequest;
import com.prog.datenbankspiel.dto.task.CreateTestAnswerRequest;
import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.repository.task.AbstractTaskRepository;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import com.prog.datenbankspiel.service.TaskService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskInitializer {

    private final TaskService taskService;
    private final TopicRepository topicRepository;
    private final LevelRepository levelRepository;
    private final AbstractTaskRepository abstractTaskRepository;

    @PostConstruct
    public void init() {
        Level easy = levelRepository.findById(1L).orElseThrow();
        Level medium = levelRepository.findById(2L).orElseThrow();
        Level hard = levelRepository.findById(3L).orElseThrow();

        Topic topic1 = saveTopicIfMissing("Basics", easy);
        Topic topic2 = saveTopicIfMissing("Joins", medium);
        Topic topic3 = saveTopicIfMissing("Subqueries", hard);

        // Add tasks if not already created
        createQueryTask("Select all", topic1, easy, "SELECT * FROM users;");
        createQueryTask("Select name", topic1, easy, "SELECT name FROM users;");
        createQueryTask("Select age", topic2, medium, "SELECT age FROM users;");
        createQueryTask("Select with WHERE", topic3, hard, "SELECT * FROM users WHERE age > 18;");

        createTestTask("SQL Filter", topic1, easy,
                "Which keyword filters rows?",
                List.of("WHERE", "FROM", "SELECT"), List.of(0));

        createTestTask("Join Type", topic2, medium,
                "Which is a valid join?",
                List.of("LEFT JOIN", "MIDDLE JOIN", "CROSS JOIN"), List.of(0, 2));

        createTestTask("Subquery Place", topic3, hard,
                "Where can you use subqueries?",
                List.of("SELECT", "WHERE", "JOIN"), List.of(1));

        createDragDropTask("SELECT basic", topic1, easy,
                "____ * ____ users;", "SELECT * FROM users;",
                List.of("SELECT", "FROM", "*", "users", "WHERE"));

        createDragDropTask("JOIN drag", topic2, medium,
                "SELECT * ____ users ____ orders ____ users.id = orders.user_id;",
                "SELECT * FROM users JOIN orders ON users.id = orders.user_id;",
                List.of("FROM", "JOIN", "ON", "WHERE"));

        createDragDropTask("Subquery drag", topic3, hard,
                "SELECT name FROM users WHERE age > (____ age FROM users);",
                "SELECT name FROM users WHERE age > (SELECT age FROM users);",
                List.of("SELECT", "FROM", "WHERE", "JOIN", "GROUP"));
    }

    private Topic saveTopicIfMissing(String name, Level level) {
        return topicRepository.findByName(name)
                .orElseGet(() -> {
                    Topic topic = new Topic();
                    topic.setName(name);
                    topic.setDifficulty(level.getDifficulty());
                    topic.setLevel(level);
                    return topicRepository.save(topic);
                });
    }

    private boolean taskExists(String title) {
        return abstractTaskRepository.findAll().stream()
                .anyMatch(t -> t.getTitle().equalsIgnoreCase(title));
    }

    private void createQueryTask(String title, Topic topic, Level level, String rightAnswer) {
        if (taskExists(title)) return;

        CreateTaskQueryRequest dto = new CreateTaskQueryRequest();
        dto.setTitle(title);
        dto.setDescription("Query task: " + title);
        dto.setSetupSql("CREATE TABLE users (id INT, name VARCHAR, age INT);");
        dto.setRightAnswer(rightAnswer);
        dto.setPoints(5L);
        dto.setDifficulty(level.getDifficulty());
        dto.setLevelId(level.getId());
        dto.setTopicId(topic.getId());
        taskService.createTaskQuery(dto);
    }

    private void createTestTask(String title, Topic topic, Level level, String question,
                                List<String> answers, List<Integer> correctIndices) {
        if (taskExists(title)) return;

        CreateTaskTestRequest dto = new CreateTaskTestRequest();
        dto.setTitle(title);
        dto.setDescription("Test task: " + title);
        dto.setQuestion(question);
        dto.setTimeLimit(60L);
        dto.setPoints(5L);
        dto.setDifficulty(level.getDifficulty());
        dto.setLevelId(level.getId());
        dto.setTopicId(topic.getId());

        List<CreateTestAnswerRequest> answerDtos = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            CreateTestAnswerRequest answer = new CreateTestAnswerRequest();
            answer.setAnswerText(answers.get(i));
            answer.setCorrect(correctIndices.contains(i));
            answerDtos.add(answer);
        }
        dto.setAnswers(answerDtos);
        taskService.createTaskTest(dto);
    }

    private void createDragDropTask(String title, Topic topic, Level level,
                                    String setupText, String correctText, List<String> words) {
        if (taskExists(title)) return;

        CreateTaskDragAndDropRequest dto = new CreateTaskDragAndDropRequest();
        dto.setTitle(title);
        dto.setDescription("Drag task: " + title);
        dto.setSetupText(setupText);
        dto.setCorrectText(correctText);
        dto.setWords(words);
        dto.setPoints(5L);
        dto.setDifficulty(level.getDifficulty());
        dto.setLevelId(level.getId());
        dto.setTopicId(topic.getId());
        taskService.createTaskDragAndDrop(dto);
    }
}
