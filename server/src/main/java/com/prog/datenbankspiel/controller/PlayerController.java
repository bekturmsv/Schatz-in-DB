package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.PlayerDto;
import com.prog.datenbankspiel.dto.test.TestRatingDto;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.service.TestService;
import lombok.RequiredArgsConstructor;
import com.prog.datenbankspiel.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final TestService testService;

    @GetMapping("/{playerId}")
    public PlayerDto getPlayer(@PathVariable Long playerId){
        return playerService.getPlayer(playerId);
    }

    @GetMapping("/all-players")
    public List<PlayerDto> getAllPlayers(){return playerService.getAllPlayers();
    }

    @GetMapping("/rating/{difficulty}")
    public ResponseEntity<List<TestRatingDto>> getRatings(@PathVariable String difficulty) {
        LevelDifficulty level = LevelDifficulty.valueOf(difficulty.toUpperCase());
        return ResponseEntity.ok(testService.getRatings(level));
    }

}
