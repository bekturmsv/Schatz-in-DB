package pti.softwareentwicklg.SchatzInDb.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pti.softwareentwicklg.SchatzInDb.dto.ThemeDto;
import pti.softwareentwicklg.SchatzInDb.model.user.Player;
import pti.softwareentwicklg.SchatzInDb.model.user.Theme;
import pti.softwareentwicklg.SchatzInDb.repository.user.ThemeRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

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
        player.setDesign(theme.getName());
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

    @Transactional
    public void setTheme(String username, String themeName) {
        Player player = (Player) userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        System.out.println(themeName);
        Theme theme = themeRepo.findByName(themeName)
                .orElseThrow(() -> new RuntimeException("Theme not found: " + themeName));

        player.setDesign(theme.getName());
        userRepo.save(player);
    }
}
