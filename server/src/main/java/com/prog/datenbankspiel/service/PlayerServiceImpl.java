package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.PlayerDto;
import com.prog.datenbankspiel.mappers.PlayerMapper;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.repository.user.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public PlayerDto getPlayer(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        return playerMapper.toDto(player);
    }

    @Override
    public List<PlayerDto> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(playerMapper::toDto)
                .collect(Collectors.toList());
    }
}
