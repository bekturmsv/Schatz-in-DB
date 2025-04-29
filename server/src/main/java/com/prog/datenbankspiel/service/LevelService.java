package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.model.task.Level;

import java.util.List;

public interface LevelService {

    Level createLevel(Level level);

    Level getLevelById(Long id);

    List<Level> getAllLevels();

    Level updateLevel(Long id, Level updatedLevel);

    void deleteLevel(Long id);

}
