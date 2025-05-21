package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.dto.ThemeDto;
import com.prog.datenbankspiel.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;

    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/purchase")
    public ResponseEntity<?> buyTheme(@RequestParam String name, Authentication auth) {
        try {
            themeService.purchaseTheme(auth.getName(), name);
            return ResponseEntity.ok("Purchased " + name);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemes());
    }
}
