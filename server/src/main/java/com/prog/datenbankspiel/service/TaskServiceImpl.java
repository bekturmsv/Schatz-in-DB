package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.front.AllLevelTasksDto;
import com.prog.datenbankspiel.dto.front.FinalTestTaskDto;
import com.prog.datenbankspiel.dto.front.FinalTestWrapper;
import com.prog.datenbankspiel.dto.front.TaskGroupDto;
import com.prog.datenbankspiel.dto.task.*;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskPosition;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.repository.task.*;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final AbstractTaskRepository taskRepository;
    private final ProgressRepository progressRepository;
    private final TaskQueryRepository taskQueryRepository;
    private final TaskDragAndDropRepository taskDragAndDropRepository;
    private final TaskTestRepository taskTestRepository;
    private final TopicRepository topicRepository;
    private final LevelRepository levelRepository;

    private final TopicService topicService;
    private final LevelService levelService;
    private final PlayerService playerService;

    @Override
    public AllLevelTasksDto getAllTasksGrouped() {
        Map<String, TaskGroupDto> levelMap = new LinkedHashMap<>();

        for (LevelDifficulty difficulty : LevelDifficulty.values()) {
            // Fetch REGULAR tasks
            List<AbstractTask> regularTasks = taskRepository
                    .findByDifficultyAndTaskTypeAndTaskPosition(difficulty, TaskType.TASK_QUERY, TaskPosition.REGULAR);

            // Fetch TEST tasks
            List<AbstractTask> testTasks = taskRepository
                    .findByDifficultyAndTaskTypeAndTaskPosition(difficulty, TaskType.TASK_QUERY, TaskPosition.TEST);

            // Map REGULAR tasks
            List<FinalTestTaskDto> regularDtos = regularTasks.stream()
                    .map(this::mapToFinalDto)
                    .toList();

            // Map TEST tasks
            List<FinalTestTaskDto> testDtos = testTasks.stream()
                    .map(this::mapToFinalDto)
                    .toList();

            // Wrap in DTOs
            TaskGroupDto group = new TaskGroupDto();
            group.setRegularTasks(regularDtos);

            if (!testDtos.isEmpty()) {
                FinalTestWrapper finalTest = new FinalTestWrapper();
                finalTest.setTasks(testDtos);
                group.setFinalTest(finalTest);
            }

            levelMap.put(difficulty.name().toLowerCase(), group);
        }

        AllLevelTasksDto allTasks = new AllLevelTasksDto();
        allTasks.setTasks(levelMap);
        return allTasks;
    }


    private FinalTestTaskDto mapToFinalDto(AbstractTask task) {
        FinalTestTaskDto dto = new FinalTestTaskDto();
        dto.setId(task.getId());
        dto.setName(task.getTitle());
        dto.setTopic(task.getTopic().getName());
        dto.setPoints(task.getPoints());
        dto.setDescription(task.getDescription());
        if (task.getHint() != null) dto.setHint(task.getHint().getText());

        if (task instanceof TaskQuery query) dto.setCorrectAnswer(query.getRightAnswer());
        return dto;
    }


    // ----------- Create Tasks -----------

    @Override
    public TaskQuery createTaskQuery(CreateTaskQueryRequest dto) {
        TaskQuery task = new TaskQuery();
        fillCommonFields(task, TaskType.TASK_QUERY, dto);
        task.setSetupQuery(dto.getSetupQuery());
        task.setRightAnswer(dto.getRightAnswer());
        addHintIfExists(task, dto.getHint());
        return taskRepository.save(task);
    }

    @Override
    public TaskTest createTaskTest(CreateTaskTestRequest dto) {
        TaskTest task = new TaskTest();
        fillCommonFields(task, TaskType.TEST, dto);
        task.setTimeLimit(dto.getTimeLimit());

        task.setAnswers(dto.getAnswers().stream().map(answerDto -> {
            TestAnswer answer = new TestAnswer();
            answer.setAnswerText(answerDto.getAnswerText());
            answer.setCorrect(answerDto.isCorrect());
            answer.setTaskTest(task);
            return answer;
        }).toList());

        addHintIfExists(task, dto.getHint());
        return taskRepository.save(task);
    }

    @Override
    public TaskDragAndDrop createTaskDragAndDrop(CreateTaskDragAndDropRequest dto) {
        TaskDragAndDrop task = new TaskDragAndDrop();
        fillCommonFields(task, TaskType.DRAG_AND_DROP, dto);
        task.setSetupText(dto.getSetupText());
        task.setCorrectText(dto.getCorrectText());
        task.setWords(dto.getWords());
        addHintIfExists(task, dto.getHint());
        return taskRepository.save(task);
    }

    // ----------- Queries -----------

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public AbstractTask getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    public TaskQueryRequest getTaskQueryById(Long id) {
        AbstractTask task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        if (!(task instanceof TaskQuery query)) {
            throw new IllegalArgumentException("Task with id " + id + " is not a TaskQuery.");
        }
        TaskQueryRequest dto = mapToQueryDto(query);
        return dto;
    }

    @Override
    public List<AbstractTask> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<AbstractTask> getTasksByTopic(Long topicId) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getTopic() != null && task.getTopic().getId().equals(topicId))
                .toList();
    }

    @Override
    public List<AbstractTask> getTasksByDifficulty(String difficulty) {
        LevelDifficulty diff = LevelDifficulty.valueOf(difficulty.toUpperCase());
        return taskRepository.findAll().stream()
                .filter(task -> task.getDifficulty() == diff)
                .toList();
    }

    @Override
    public List<AbstractTask> getTasksByLevel(Long levelId) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getLevel() != null && task.getLevel().getId().equals(levelId))
                .toList();
    }

    @Override
    public List<AbstractTask> getFinishedTasks(Long userId) {
        Progress progress = progressRepository.findByUserId(userId);
        return taskRepository.findAll().stream()
                .filter(task -> progress.getCompletedTaskIds().contains(task.getId()))
                .toList();
    }

    // ----------- DTO Responses -----------

    @Override
    public List<AbstractTaskRequest> getLevelTaskQueryAndDragAndDrop(Long levelId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = playerService.getUserIdByUsername(username);

        List<AbstractTaskRequest> dtos = new ArrayList<>();

        dtos.addAll(
                taskQueryRepository.findByLevel_Id(levelId).stream()
                        .map(task -> {
                            AbstractTaskRequest dto = mapToQueryDto(task);
                            boolean isDone = playerService.isTaskFinished(userId, task.getId());
                            dto.setFinished(isDone);
                            return dto;
                        }).toList()
        );

        dtos.addAll(
                taskDragAndDropRepository.findByLevel_Id(levelId).stream()
                        .map(task -> {
                            AbstractTaskRequest dto = mapToDragDto(task);
                            boolean isDone = playerService.isTaskFinished(userId, task.getId());
                            dto.setFinished(isDone);
                            return dto;
                        }).toList()
        );

        return dtos;
    }


    @Override
    public List<TaskTestRequest> getLevelTests(Long levelId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = playerService.getUserIdByUsername(username);

        return taskTestRepository.findByLevel_Id(levelId).stream()
                .map(task -> {
                    TaskTestRequest dto = mapToTaskTestDto(task);
                    boolean isDone = playerService.isTaskFinished(userId, task.getId());
                    dto.setFinished(isDone);
                    return dto;
                }).toList();
    }


    // ----------- Dto/Mappers -----------

    private void fillCommonFields(AbstractTask task, TaskType type, AbstractTaskRequest dto) {
        task.setTaskType(type);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPoints(dto.getPoints());
        task.setDifficulty(dto.getDifficulty());
        task.setTaskPosition(dto.getTaskPosition());

        // First ensure we have a level
        Level level;
        if (dto.getLevelId() != null) {
            level = levelService.getLevelById(dto.getLevelId());
        } else {
            level = new Level();
            level.setDifficulty(dto.getDifficulty());
            level = levelRepository.save(level);
        }
        task.setLevel(level);

        // Then handle topic, which depends on level
        Topic topic;
        if (dto.getTopicId() != null) {
            topic = topicService.getTopicById(dto.getTopicId());
        } else if (dto.getTopicName() != null && !dto.getTopicName().isEmpty()) {
            Level finalLevel = level;
            topic = topicRepository.findByName(dto.getTopicName())
                    .orElseGet(() -> {
                        Topic newTopic = new Topic();
                        newTopic.setName(dto.getTopicName());
                        newTopic.setDifficulty(dto.getDifficulty());
                        newTopic.setLevel(finalLevel);
                        return topicRepository.save(newTopic);
                    });
    } else {
        throw new IllegalArgumentException("Either topicId or topicName must be provided");
    }
    task.setTopic(topic);
}


    private void addHintIfExists(AbstractTask task, String hintText) {
        if (hintText != null && !hintText.isBlank()) {
            Hint hint = new Hint();
            hint.setText(hintText);
            hint.setTask(task);
            task.setHint(hint);
        }
    }


    private void fillCommonDtoFields(AbstractTaskRequest dto, AbstractTask task) {
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPoints(task.getPoints());
        dto.setDifficulty(task.getDifficulty());
        dto.setTaskType(task.getTaskType());
        dto.setTopicId(task.getTopic().getId());
        dto.setTopicName(task.getTopic().getName());
        if (task.getLevel() != null) dto.setLevelId(task.getLevel().getId());
        if (task.getTopic() != null) dto.setTopicId(task.getTopic().getId());
    }

    private TaskQueryRequest mapToQueryDto(TaskQuery task) {
        TaskQueryRequest dto = new TaskQueryRequest();
        fillCommonDtoFields(dto, task);
        dto.setSetupQuery(task.getSetupQuery());;
        return dto;
    }

    private TaskDragAndDropRequest mapToDragDto(TaskDragAndDrop task) {
        TaskDragAndDropRequest dto = new TaskDragAndDropRequest();
        fillCommonDtoFields(dto, task);
        dto.setSetupText(task.getSetupText());
        dto.setWords(task.getWords());
        return dto;
    }

    private TaskTestRequest mapToTaskTestDto(TaskTest task) {
        TaskTestRequest dto = new TaskTestRequest();
        fillCommonDtoFields(dto, task);
        dto.setTimeLimit(task.getTimeLimit());
        dto.setAnswers(task.getAnswers().stream().map(answer -> {
            TestAnswerRequest a = new TestAnswerRequest();
            a.setAnswerText(answer.getAnswerText());
            return a;
        }).toList());
        return dto;
    }


}