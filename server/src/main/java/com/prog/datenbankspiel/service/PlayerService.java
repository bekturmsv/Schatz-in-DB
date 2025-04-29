package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.PlayerDragAndDropAnswerDto;
import com.prog.datenbankspiel.dto.task.PlayerQueryAnswerDto;
import com.prog.datenbankspiel.dto.task.PlayerTestAnswerDto;

public interface PlayerService {

    boolean submitQuerySolution(PlayerQueryAnswerDto solutionDto, Long playerId);

    boolean submitTestSolution(PlayerTestAnswerDto solutionDto, Long playerId);

    boolean submitDragAndDropSolution(PlayerDragAndDropAnswerDto solutionDto, Long playerId);

}
