package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.PlayerDragAndDropAnswerDto;
import com.prog.datenbankspiel.dto.task.PlayerQueryAnswerDto;
import com.prog.datenbankspiel.dto.task.PlayerTestAnswerDto;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.repository.task.AbstractTaskRepository;
import com.prog.datenbankspiel.repository.user.PlayerRepository;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public boolean submitQuerySolution(PlayerQueryAnswerDto solutionDto, Long playerId) {
        AbstractTask task = taskRepository.findById(solutionDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + solutionDto.getTaskId()));
        if (!(task instanceof TaskQuery)) {
            throw new RuntimeException("Task is not a TaskQuery");
        }
        boolean correct = checkQuerySolution((TaskQuery) task, solutionDto.getQueryAnswer());
        if (correct) {
            updateProgress(playerId, task);

        }
        return correct;
    }

    @Override
    public boolean submitTestSolution(PlayerTestAnswerDto solutionDto, Long playerId) {
        AbstractTask task = taskRepository.findById(solutionDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + solutionDto.getTaskId()));
        if (!(task instanceof TaskTest)) {
            throw new RuntimeException("Task is not a TaskTest");
        }
        boolean correct = checkTestSolution((TaskTest) task, solutionDto.getSelectedAnswers());
        if (correct) {
            updateProgress(playerId, task);
        }
        return correct;
    }

    @Override
    public boolean submitDragAndDropSolution(PlayerDragAndDropAnswerDto solutionDto, Long playerId) {
        AbstractTask task = taskRepository.findById(solutionDto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + solutionDto.getTaskId()));
        if (!(task instanceof TaskDragAndDrop)) {
            throw new RuntimeException("Task is not a TaskDragAndDrop");
        }
        boolean correct = checkDragAndDropSolution((TaskDragAndDrop) task, solutionDto.getDragAndDropAnswer());
        if (correct) {
            updateProgress(playerId, task);
        }
        return correct;
    }

    private boolean checkQuerySolution(TaskQuery taskQuery, String playerSolution) {
        if (playerSolution == null) return false;
        return playerSolution.trim().equalsIgnoreCase(taskQuery.getRightAnswer().trim());
    }

    private boolean checkTestSolution(TaskTest taskTest, List<String> playerAnswers) {
        if (playerAnswers == null || playerAnswers.isEmpty()) return false;
        List<String> correctAnswers = taskTest.getAnswers().stream()
                .filter(TestAnswer::isCorrect)
                .map(TestAnswer::getAnswerText)
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
        List<String> submittedAnswers = playerAnswers.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
        return correctAnswers.containsAll(submittedAnswers) && submittedAnswers.containsAll(correctAnswers);
    }

    private boolean checkDragAndDropSolution(TaskDragAndDrop taskDragAndDrop, String playerSolution) {
        if (playerSolution == null) return false;
        return playerSolution.trim().equalsIgnoreCase(taskDragAndDrop.getCorrectText().trim());
    }

    private void updateProgress(Long playerId, AbstractTask task) {
        Progress progress = progressRepository.findByUserId(playerId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
        progress.getCompletedTaskIds().add(task.getId());
        player.setTotal_points(player.getTotal_points() + task.getPoints());
        progressRepository.save(progress);
        playerRepository.save(player);
    }
}

