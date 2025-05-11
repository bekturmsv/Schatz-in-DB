package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.PlayerDto;
import lombok.RequiredArgsConstructor;
import com.prog.datenbankspiel.service.PlayerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/{playerId}")
    public PlayerDto getPlayer(@PathVariable Long playerId){
        return playerService.getPlayer(playerId);
    }

    @GetMapping("/all-players")
    public List<PlayerDto> getAllPlayers(){return playerService.getAllPlayers();
    }
}
