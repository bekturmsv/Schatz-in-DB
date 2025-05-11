package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.PlayerDto;

import java.util.List;

public interface PlayerService {
    PlayerDto getPlayer(Long playerId);
    List<PlayerDto> getAllPlayers();
}
