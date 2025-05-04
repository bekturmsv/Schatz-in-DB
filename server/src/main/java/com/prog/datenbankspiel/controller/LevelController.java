package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.task.AbstractTaskRequest;
import com.prog.datenbankspiel.dto.task.CreateTaskTestRequest;
import com.prog.datenbankspiel.dto.task.LevelRequest;
import com.prog.datenbankspiel.dto.task.TaskTestRequest;
import com.prog.datenbankspiel.service.LevelService;
import com.prog.datenbankspiel.service.PlayerService;
import com.prog.datenbankspiel.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/level")
@RequiredArgsConstructor
public class LevelController {

    private final LevelService levelService;
    private final TaskService taskService;
    private final PlayerService playerService;

    @GetMapping("/levels")
    public ResponseEntity<List<LevelRequest>> getAllLevels() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LevelRequest> getLevel(@PathVariable Long id) {
        return ResponseEntity.ok(levelService.getLevelDtoById(id));
    }

    @GetMapping("/current")
    public ResponseEntity<Long> getCurrentLevelId(Authentication authentication) {
        String username = authentication.getName();
        Long userId = playerService.getUserIdByUsername(username);
        return ResponseEntity.ok(levelService.getLevelIdByProgress(userId));
    }


    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<AbstractTaskRequest>> getLevelTaskQueryAndDragAndDrop(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getLevelTaskQueryAndDragAndDrop(id));
    }

    @GetMapping("/{id}/tests")
    public ResponseEntity<List<TaskTestRequest>> getLevelTests(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getLevelTests(id));
    }

    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/select")
    public ResponseEntity<LevelRequest> selectLevel(
            @RequestParam Long levelId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(levelService.selectLevel(levelId, userId));
    }

    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/finish")
    public ResponseEntity<LevelRequest> finishLevel(
            @RequestParam Long levelId,
            Authentication authentication) {

        Long userId = playerService.getUserIdByUsername(authentication.getName());
        return ResponseEntity.ok(levelService.finishLevel(levelId, userId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<LevelRequest> deleteLevel(@PathVariable Long id) {
        return ResponseEntity.ok(levelService.deleteLevel(id));
    }
}
