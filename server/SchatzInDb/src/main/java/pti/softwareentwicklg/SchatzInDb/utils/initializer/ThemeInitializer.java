package pti.softwareentwicklg.SchatzInDb.utils.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.user.Theme;
import pti.softwareentwicklg.SchatzInDb.repository.user.ThemeRepository;

import java.util.List;

@Component
public class ThemeInitializer {

    private final ThemeRepository themeRepository;

    public ThemeInitializer(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @PostConstruct
    public void initThemes() {

        List<Theme> themes = List.of(
                createTheme("default", 0L),
                createTheme("dark", 300L),
                createTheme("neon", 510L),
                createTheme("vintage", 650L)
        );

        for(Theme theme : themes) {
            if (!themeRepository.existsByName(theme.getName())) {
                themeRepository.save(theme);
            }
        }
        themeRepository.saveAll(themes);
    }

    private Theme createTheme(String name, Long cost) {
        Theme theme = new Theme();
        theme.setName(name);
        theme.setCost(cost);
        return theme;
    }
}
