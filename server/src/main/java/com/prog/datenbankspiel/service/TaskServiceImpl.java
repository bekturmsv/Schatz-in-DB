package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.*;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.repository.task.AbstractTaskRepository;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private AbstractTaskRepository taskRepository;
    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private TopicService topicService;
    @Autowired
    private LevelService levelService;

    @Override
    public TaskQuery createTaskQuery(TaskQueryDto dto) {
        TaskQuery task = new TaskQuery();
        fillCommonFields(task, TaskType.TASK_QUERY ,dto.getTitle(), dto.getDescription(), dto.getPoints(), dto.getDifficulty(), dto.getTopicId(), dto.getLevelId());
        task.setSetupSql(dto.getSetupSql());
        task.setRightAnswer(dto.getRightAnswer());
        return taskRepository.save(task);
    }

    @Override
    public TaskTest createTaskTest(TaskTestDto dto) {
        TaskTest task = new TaskTest();
        fillCommonFields(task, TaskType.TEST, dto.getTitle(), dto.getDescription(), dto.getPoints(), dto.getDifficulty(), dto.getTopicId(), dto.getLevelId());
        task.setQuestion(dto.getQuestion());
        task.setTimeLimit(dto.getTimeLimit());

        List<TestAnswer> answers = new ArrayList<>();
        for (TestAnswerDto answerDto : dto.getAnswers()) {
            TestAnswer answer = new TestAnswer();
            answer.setAnswerText(answerDto.getAnswerText());
            answer.setCorrect(answerDto.isCorrect());
            answer.setTaskTest(task);
            answers.add(answer);
        }
        task.setAnswers(answers);
        return taskRepository.save(task);
    }


    @Override
    public TaskDragAndDrop createTaskDragAndDrop(TaskDragAndDropDto dto) {
        TaskDragAndDrop task = new TaskDragAndDrop();
        fillCommonFields(task, TaskType.DRAG_AND_DROP, dto.getTitle(), dto.getDescription(), dto.getPoints(), dto.getDifficulty(), dto.getTopicId(), dto.getLevelId());
        task.setSetupText(dto.getSetupText());
        task.setCorrectText(dto.getCorrectText());
        task.setWords(dto.getWords());
        return taskRepository.save(task);
    }


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
    public List<AbstractTask> getTasksByLevelAndTopic(Long levelId, Long topicId) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getLevel() != null && task.getLevel().getId().equals(levelId))
                .filter(task -> task.getTopic() != null && task.getTopic().getId().equals(topicId))
                .toList();
    }

    @Override
    public List<AbstractTask> getFinishedTasks(Long userId) {
        Progress progress = progressRepository.findByUserId(userId);
        Set<Long> completedIds = progress.getCompletedTaskIds();
        return taskRepository.findAll().stream()
                .filter(task -> completedIds.contains(task.getId()))
                .toList();
    }



    private void fillCommonFields(AbstractTask task, TaskType taskType, String title, String description, Long points, LevelDifficulty difficulty, Long topicId, Long levelId) {
        task.setTaskType(taskType);
        task.setTitle(title);
        task.setDescription(description);
        task.setPoints(points);
        task.setDifficulty(difficulty);
        task.setTopic(topicService.getTopicById(topicId));
        task.setLevel(levelService.getLevelById(levelId));
    }
}

