package com.prog.datenbankspiel.service;


import com.prog.datenbankspiel.dto.task.LevelRequest;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.repository.task.AbstractTaskRepository;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository levelRepository;
    private final ProgressRepository progressRepository;
    private final AbstractTaskRepository abstractTaskRepository;

    @Override
    public List<LevelRequest> getAllLevels() {
        return levelRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public LevelRequest selectLevel(Long levelId, Long userId) {
        Progress progress = progressRepository.findByUserId(userId);
        progress.setCurrentLevelId(levelId);
        progressRepository.save(progress);

        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Level not found"));
        return toDto(level);
    }

    @Override
    public LevelRequest finishLevel(Long levelId, Long userId) {
        Progress progress = progressRepository.findByUserId(userId);
        if (progress == null) {
            throw new RuntimeException("Progress not found for user " + userId);
        }
        Set<Long> completed = progress.getCompletedTaskIds();
        if (completed == null) completed = new HashSet<>();
        // Get all tasks for the level
        List<Long> levelTaskIds = abstractTaskRepository.findByLevel_Id(levelId)
                .stream()
                .map(task -> ((Task) task).getId())
                .toList();
        // Check if user has completed all
        if (!completed.containsAll(levelTaskIds)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not All Tasks Finished"
            );
        }
        // Try to find next level
        Optional<Level> nextLevelOpt = levelRepository.findById(levelId + 1);
        if (nextLevelOpt.isPresent()) {
            progress.setCurrentLevelId(nextLevelOpt.get().getId());
            progressRepository.save(progress);
        } else {
            // No next level, leave current level as is
        }

        Level currentLevel = levelRepository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Level not found"));

        return toDto(currentLevel);
    }

    @Override
    public LevelRequest getLevelDtoById(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found with id: " + id));
        return toDto(level);
    }

    @Override
    public Level getLevelById(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found with id: " + id));
        return level;
    }

    @Override
    public Long getLevelIdByProgress(Long userId) {
        Progress progress = progressRepository.findByUserId(userId);
        if (progress == null || progress.getCurrentLevelId() == null) {
            throw new RuntimeException("No progress or level found for user ID: " + userId);
        }
        return progress.getCurrentLevelId();
    }


    @Override
    public LevelRequest deleteLevel(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found with id: " + id));
        levelRepository.delete(level);
        return toDto(level);
    }

    private LevelRequest toDto(Level level) {
        LevelRequest dto = new LevelRequest();
        dto.setId(level.getId());
        dto.setDifficulty(level.getDifficulty());
        return dto;
    }
}
