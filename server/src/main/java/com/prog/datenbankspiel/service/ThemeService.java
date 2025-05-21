package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.ThemeDto;
import com.prog.datenbankspiel.model.user.Player;
import com.prog.datenbankspiel.model.user.Theme;
import com.prog.datenbankspiel.repository.user.ThemeRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final UserRepository userRepo;
    private final ThemeRepository themeRepo;

    @Transactional
    public void purchaseTheme(String username, String themeName) {
        Player player = (Player) userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Theme theme = themeRepo.findByName(themeName)
                .orElseThrow(() -> new RuntimeException("Theme not found: " + themeName));

        if (player.getPurchasedThemes().contains(theme)) {
            throw new RuntimeException("Theme already purchased: " + themeName);
        }
        if (player.getTotal_points() < theme.getCost()) {
            throw new RuntimeException("Not enough points to purchase: " + themeName);
        }

        player.setTotal_points(player.getTotal_points() - theme.getCost());
        player.getPurchasedThemes().add(theme);
        userRepo.save(player);
    }

    public List<ThemeDto> getAllThemes() {
        return themeRepo.findAll().stream()
                .map(theme -> {
                    ThemeDto dto = new ThemeDto();
                    dto.setName(theme.getName());
                    dto.setCost(theme.getCost());
                    return dto;
                }).toList();
    }
}

