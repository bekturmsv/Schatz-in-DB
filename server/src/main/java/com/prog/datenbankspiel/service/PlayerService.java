package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.task.SubmitQueryRequest;
import com.prog.datenbankspiel.dto.task.SubmitTestRequest;

public interface PlayerService {

    boolean submitQuerySolution(SubmitQueryRequest solutionDto, Long playerId);

    boolean submitTestSolution(SubmitTestRequest solutionDto, Long playerId);

    Long getUserIdByUsername(String username);

    boolean isTaskFinished(Long userId, Long taskId);
}
