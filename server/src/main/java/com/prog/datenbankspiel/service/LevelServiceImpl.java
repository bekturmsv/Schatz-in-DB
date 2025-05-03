package com.prog.datenbankspiel.service;


import com.prog.datenbankspiel.dto.task.LevelDto;
import com.prog.datenbankspiel.model.task.*;
import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import com.prog.datenbankspiel.repository.user.ProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository levelRepository;
    private final ProgressRepository progressRepository;

    @Override
    public List<LevelDto> getAllLevels() {
        return levelRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public LevelDto selectLevel(Long levelId, Long userId) {
        Progress progress = progressRepository.findByUserId(userId);
        progress.setCurrentLevelId(levelId);
        progressRepository.save(progress);

        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Level not found"));
        return toDto(level);
    }

    @Override
    public LevelDto getLevelDtoById(Long id) {
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
    public LevelDto deleteLevel(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found with id: " + id));
        levelRepository.delete(level);
        return toDto(level);
    }

    private LevelDto toDto(Level level) {
        LevelDto dto = new LevelDto();
        dto.setId(level.getId());
        dto.setDifficulty(level.getDifficulty());
        return dto;
    }
}
