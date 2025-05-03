package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.*;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.repository.task.AbstractTaskRepository;
import com.prog.datenbankspiel.repository.task.TaskDragAndDropRepository;
import com.prog.datenbankspiel.repository.task.TaskQueryRepository;
import com.prog.datenbankspiel.repository.task.TaskTestRepository;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final AbstractTaskRepository taskRepository;
    private final ProgressRepository progressRepository;
    private final TaskQueryRepository taskQueryRepository;
    private final TaskDragAndDropRepository taskDragAndDropRepository;
    private final TaskTestRepository taskTestRepository;

    private final TopicService topicService;
    private final LevelService levelService;

    // ----------- Create Tasks -----------

    @Override
    public TaskQuery createTaskQuery(CreateTaskQueryRequest dto) {
        TaskQuery task = new TaskQuery();
        fillCommonFields(task, TaskType.TASK_QUERY, dto);
        task.setSetupSql(dto.getSetupSql());
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
        List<AbstractTaskRequest> dtos = new ArrayList<>();
        dtos.addAll(taskQueryRepository.findByLevel_Id(levelId).stream().map(this::mapToQueryDto).toList());
        dtos.addAll(taskDragAndDropRepository.findByLevel_Id(levelId).stream().map(this::mapToDragDto).toList());
        return dtos;
    }

    @Override
    public List<CreateTaskTestRequest> getLevelTests(Long levelId) {
        return taskTestRepository.findByLevel_Id(levelId).stream()
                .map(this::mapToTaskTestDto)
                .toList();
    }

    // ----------- Dto/Mappers -----------

    private void fillCommonFields(AbstractTask task, TaskType type, AbstractTaskRequest dto) {
        task.setTaskType(type);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPoints(dto.getPoints());
        task.setDifficulty(dto.getDifficulty());
        task.setTopic(topicService.getTopicById(dto.getTopicId()));
        task.setLevel(levelService.getLevelById(dto.getLevelId()));
    }

    private void addHintIfExists(AbstractTask task, HintRequest hintRequest) {
        if (hintRequest != null && hintRequest.getText() != null && !hintRequest.getText().isBlank()) {
            Hint hint = new Hint();
            hint.setText(hintRequest.getText());
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
        if (task.getLevel() != null) dto.setLevelId(task.getLevel().getId());
        if (task.getTopic() != null) dto.setTopicId(task.getTopic().getId());
        if (task.getHint() != null) {
            HintRequest hintRequest = new HintRequest();
            hintRequest.setId(task.getHint().getId());
            hintRequest.setText(task.getHint().getText());
            dto.setHint(hintRequest);
        }
    }

    private CreateTaskQueryRequest mapToQueryDto(TaskQuery task) {
        CreateTaskQueryRequest dto = new CreateTaskQueryRequest();
        fillCommonDtoFields(dto, task);
        dto.setSetupSql(task.getSetupSql());
        dto.setRightAnswer(task.getRightAnswer());
        return dto;
    }

    private CreateTaskDragAndDropRequest mapToDragDto(TaskDragAndDrop task) {
        CreateTaskDragAndDropRequest dto = new CreateTaskDragAndDropRequest();
        fillCommonDtoFields(dto, task);
        dto.setSetupText(task.getSetupText());
        dto.setCorrectText(task.getCorrectText());
        dto.setWords(task.getWords());
        return dto;
    }

    private CreateTaskTestRequest mapToTaskTestDto(TaskTest task) {
        CreateTaskTestRequest dto = new CreateTaskTestRequest();
        fillCommonDtoFields(dto, task);
        dto.setTimeLimit(task.getTimeLimit());
        dto.setAnswers(task.getAnswers().stream().map(answer -> {
            CreateTestAnswerRequest a = new CreateTestAnswerRequest();
            a.setAnswerText(answer.getAnswerText());
            a.setCorrect(answer.isCorrect());
            return a;
        }).toList());
        return dto;
    }
}
