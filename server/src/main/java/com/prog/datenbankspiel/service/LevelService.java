package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.LevelRequest;
import com.prog.datenbankspiel.model.task.Level;

import java.util.List;

public interface LevelService {
    List<LevelRequest> getAllLevels();
    LevelRequest selectLevel(Long levelId, Long userId);
    LevelRequest finishLevel(Long levelId, Long userId);
    LevelRequest getLevelDtoById(Long id);
    Level getLevelById(Long id);

    Long getLevelIdByProgress(Long userId);

    LevelRequest deleteLevel(Long id);
}


