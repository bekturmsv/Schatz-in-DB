package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.task.LevelRequest;
import com.prog.datenbankspiel.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/level")
@RequiredArgsConstructor
public class LevelController {

    private final LevelService levelService;

    @GetMapping("/levels")
    public ResponseEntity<List<LevelRequest>> getAllLevels() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LevelRequest> getLevel(@PathVariable Long id) {
        return ResponseEntity.ok(levelService.getLevelDtoById(id));
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
            @RequestParam Long userId) {
        return ResponseEntity.ok(levelService.finishLevel(levelId, userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<LevelRequest> deleteLevel(@PathVariable Long id) {
        return ResponseEntity.ok(levelService.deleteLevel(id));
    }
}
