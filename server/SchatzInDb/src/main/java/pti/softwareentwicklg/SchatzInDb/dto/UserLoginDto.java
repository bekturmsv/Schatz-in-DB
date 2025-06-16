package pti.softwareentwicklg.SchatzInDb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pti.softwareentwicklg.SchatzInDb.model.user.Theme;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private Long points;
    private Set<Theme> purchasedThemes;
    private String currentTheme;
    private ProgressDto progress;
    private List<TaskStatDto> lastTasks;
    private Map<String, List<String>> completedTasks;
    private Map<String, Boolean> completedLevels;
}