package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pti.softwareentwicklg.SchatzInDb.dto.ThemeDto;
import pti.softwareentwicklg.SchatzInDb.service.user.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> buyTheme(@RequestParam String name, Authentication auth) {
        try {
            themeService.purchaseTheme(auth.getName(), name);
            return ResponseEntity.ok("Purchased " + name);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/setTheme")
    public ResponseEntity<?> setTheme(@RequestParam String name, Authentication auth) {
        try {
            themeService.setTheme(auth.getName(), name);
            return ResponseEntity.ok("Set theme " + name);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemes());
    }
}