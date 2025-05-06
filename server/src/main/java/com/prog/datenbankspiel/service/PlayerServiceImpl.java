package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.SubmitQueryRequest;
import com.prog.datenbankspiel.dto.task.SubmitTestRequest;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.repository.task.AbstractTaskRepository;
import com.prog.datenbankspiel.repository.user.PlayerRepository;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private AbstractTaskRepository taskRepository;
    @Autowired
    private ProgressRepository progressRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public boolean submitQuerySolution(SubmitQueryRequest dto, Long playerId) {
        TaskQuery task = (TaskQuery) taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String submitted = dto.getAnswer().replaceAll("\\s+", " ").trim().replaceAll(";$", "");
        String correct = task.getRightAnswer().replaceAll("\\s+", " ").trim().replaceAll(";$", "");

        boolean isCorrect = submitted.equalsIgnoreCase(correct);

        if (isCorrect) {
            updateProgress(playerId, task);
        }

        return isCorrect;
    }

    @Override
    public boolean submitTestSolution(SubmitTestRequest solutionDto, Long playerId) {
        Task task = taskRepository.findById(solutionDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + solutionDto.getTaskId()));
        if (!(task instanceof TaskTest)) {
            throw new RuntimeException("Task is not a TaskTest");
        }
        boolean correct = checkTestSolution((TaskTest) task, solutionDto.getSelectedAnswersId());
        if (correct) {
            updateProgress(playerId, task);
        }
        return correct;
    }

    @Override
    public Long getUserIdByUsername(String username) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Player not found: " + username));
        return player.getId();
    }

    @Override
    public boolean isTaskFinished(Long userId, Long taskId) {
        return progressRepository.existsByUser_IdAndCompletedTaskIdsContains(userId, taskId);
    }

    private boolean checkQuerySolution(TaskQuery taskQuery, String playerSolution) {
        if (playerSolution == null) return false;
        return playerSolution.trim().equalsIgnoreCase(taskQuery.getRightAnswer().trim());
    }

    private boolean checkTestSolution(TaskTest taskTest, List<Long> playerAnswers) {
        if (playerAnswers == null || playerAnswers.isEmpty()) return false;
        List<String> correctAnswers = taskTest.getAnswers().stream()
                .filter(TestAnswer::isCorrect)
                .map(TestAnswer::getAnswerText)
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
        List<String> submittedAnswers = playerAnswers.stream()
                .map(String::valueOf)
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
        return correctAnswers.containsAll(submittedAnswers) && submittedAnswers.containsAll(correctAnswers);
    }

    private boolean checkDragAndDropSolution(TaskDragAndDrop taskDragAndDrop, String playerSolution) {
        if (playerSolution == null) return false;
        return playerSolution.trim().equalsIgnoreCase(taskDragAndDrop.getCorrectText().trim());
    }

    private void updateProgress(Long playerId, Task task) {
        Progress progress = progressRepository.findByUserId(playerId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
        progress.getCompletedTaskIds().add(task.getId());
        player.setTotal_points(player.getTotal_points() + task.getPoints());
        progressRepository.save(progress);
        playerRepository.save(player);
    }



}

