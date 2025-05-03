package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.LevelDto;
import com.prog.datenbankspiel.model.task.Level;

import java.util.List;

public interface LevelService {
    List<LevelDto> getAllLevels();
    LevelDto selectLevel(Long levelId, Long userId);
    LevelDto getLevelDtoById(Long id);
    Level getLevelById(Long id);
    LevelDto deleteLevel(Long id);
}


