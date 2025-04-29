package com.prog.datenbankspiel.service;


import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

    private final LevelRepository levelRepository;

    @Override
    public Level createLevel(Level level) {
        return levelRepository.save(level);
    }

    @Override
    public Level getLevelById(Long id) {
        return levelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Level not found with id: " + id));
    }

    @Override
    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    @Override
    public Level updateLevel(Long id, Level updatedLevel) {
        Level level = getLevelById(id);
        level.setDifficulty(updatedLevel.getDifficulty());
        return levelRepository.save(level);
    }

    @Override
    public void deleteLevel(Long id) {
        levelRepository.deleteById(id);
    }
}
