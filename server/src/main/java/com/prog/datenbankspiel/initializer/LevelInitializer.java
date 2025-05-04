package com.prog.datenbankspiel.initializer;

import com.prog.datenbankspiel.model.task.Level;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.repository.task.LevelRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LevelInitializer {

    @Autowired
    private LevelRepository levelRepository;

    @PostConstruct
    public void initializeLevels() {
        createLevelIfNotExists(LevelDifficulty.EASY);
        createLevelIfNotExists(LevelDifficulty.MEDIUM);
        createLevelIfNotExists(LevelDifficulty.HARD);
    }

    private void createLevelIfNotExists(LevelDifficulty difficulty) {
        boolean exists = levelRepository.existsByDifficulty(difficulty);
        if (!exists) {
            Level level = new Level();
            level.setDifficulty(difficulty);
            levelRepository.save(level);
        }
    }
}

