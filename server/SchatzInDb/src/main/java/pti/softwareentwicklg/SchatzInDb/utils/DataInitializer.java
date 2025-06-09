package pti.softwareentwicklg.SchatzInDb.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskInteractionType;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.model.user.Theme;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.ThemeRepository;

import java.util.List;

@Component
public class DataInitializer {
    private final TaskRepository taskRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ThemeRepository themeRepository;

    public DataInitializer(TaskRepository taskRepository, @Qualifier("taskJdbcTemplate")JdbcTemplate jdbcTemplate, ThemeRepository themeRepository) {
        this.taskRepository = taskRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.themeRepository = themeRepository;
    }

    @PostConstruct
    public void initData() {
        insertVerdaechtiger();
        insertZeugen();
        insertFaelle();
        insertFahrzeuge();
        insertFallVerdaechtiger();
        insertFallZeuge();
        insertFallFahrzeug();
    }

    private void insertVerdaechtiger() {
        jdbcTemplate.update("INSERT INTO verdaechtiger (name, alter, wohnort, haarfarbe, schuhgroesse, groesse, geschlecht, vorstrafen) VALUES " +
                "('Max Mustermann', 30, 'Berlin', 'braun', 42, 180, 'M', 1)," +
                "('Erika Musterfrau', 25, 'Hamburg', 'blond', 38, 170, 'W', 0)," +
                "('Tom Schwarz', 40, 'München', 'schwarz', 44, 185, 'M', 3)," +
                "('Anna Weiß', 22, 'Köln', 'rot', 36, 165, 'W', 0);");
    }

    private void insertZeugen() {
        jdbcTemplate.update("INSERT INTO zeuge (name, alter, wohnort) VALUES " +
                "('Lena Zeugin', 34, 'Berlin')," +
                "('Tim Beobachter', 45, 'Hamburg')," +
                "('Sophia Hinweisgeberin', 29, 'München');");
    }

    private void insertFaelle() {
        jdbcTemplate.update("INSERT INTO fall (tatort, schadenssumme) VALUES " +
                "('Supermarkt Berlin', 1200.50)," +
                "('Bank Hamburg', 100000.00)," +
                "('Juwelier München', 55000.75);");
    }

    private void insertFahrzeuge() {
        jdbcTemplate.update("INSERT INTO fahrzeug (kennzeichen) VALUES " +
                "('B-MX1234')," +
                "('HH-TZ9876')," +
                "('M-AB4567');");
    }

    private void insertFallVerdaechtiger() {
        jdbcTemplate.update("INSERT INTO fall_verdaechtiger (fall_id, verdaechtiger_id) VALUES " +
                "(1, 1), (1, 2), (2, 3), (3, 4);");
    }

    private void insertFallZeuge() {
        jdbcTemplate.update("INSERT INTO fall_zeuge (fall_id, zeuge_id) VALUES " +
                "(1, 1), (2, 2), (3, 3);");
    }

    private void insertFallFahrzeug() {
        jdbcTemplate.update("INSERT INTO fall_fahrzeug (fall_id, fahrzeug_id) VALUES " +
                "(1, 1), (2, 2), (3, 3);");
    }

    String testSQL = "SELECT name FROM verdaechtiger WHERE haarfarbe LIKE 'braun'";

    @PostConstruct
    public void initTasks() {
        if (taskRepository.count() > 0) return;

        List<Task> tasks = List.of(
                createTask("T001", "Wähle alle Namen von Verdächtigen mit braunen Haaren.",
                        "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun'", SqlKategorie.WHERE,
                        Schwierigkeit.EASY, "Benutze WHERE mit haarfarbe.", TaskType.REGULAR, TaskInteractionType.SQL_INPUT, 10),

                createTask("T002", "Zähle die Anzahl der Zeugen in Berlin.",
                        "SELECT COUNT(*) FROM zeuge WHERE wohnort = 'Berlin'", SqlKategorie.COUNT,
                        Schwierigkeit.MEDIUM, "COUNT zusammen mit WHERE.", TaskType.REGULAR, TaskInteractionType.SQL_INPUT, 12),

                createTask("T003", "Gib alle Fahrzeuge mit dem Kennzeichen 'B-AB 123' aus.",
                        "SELECT * FROM fahrzeug WHERE kennzeichen = 'B-AB 123'", SqlKategorie.WHERE,
                        Schwierigkeit.EASY, "Kennzeichen vergleichen.", TaskType.REGULAR, TaskInteractionType.SQL_INPUT, 13),

                createTask("T004", "Zeige die Summe der Schadenssummen aller Fälle.",
                        "SELECT SUM(schadenssumme) FROM fall", SqlKategorie.SUM,
                        Schwierigkeit.MEDIUM, "SUM auf Schadenssumme.", TaskType.TEST, TaskInteractionType.SQL_INPUT,14),

                createTask("T005", "Liste alle Verdächtigen mit mehr als 2 Vorstrafen auf.",
                        "SELECT name FROM verdaechtiger WHERE vorstrafen > 2", SqlKategorie.WHERE,
                        Schwierigkeit.HARD, "Vergleiche die Anzahl der Vorstrafen.", TaskType.TEST, TaskInteractionType.SQL_INPUT, 15)
        );

        taskRepository.saveAll(tasks);

        if (themeRepository.count() > 0) return;
        List<Theme> theme = List.of(
                createTheme("default", 0L),
                createTheme("dark", 100L),
                createTheme("neon", 200L),
                createTheme("vintage", 150L)
        );

        themeRepository.saveAll(theme);
    }

    private Task createTask(String code, String aufgabe, String solution, SqlKategorie kategorie,
                            Schwierigkeit schwierigkeitsgrad, String hint, TaskType type, TaskInteractionType interactionType, Integer point) {
        Task task = new Task();
        task.setTaskCode(code);
        task.setAufgabe(aufgabe);
        task.setSolution(solution);
        task.setKategorie(kategorie);
        task.setSchwierigkeitsgrad(schwierigkeitsgrad);
        task.setHint(hint);
        task.setTaskType(type);
        task.setInteractionType(interactionType);
        task.setPoints(point);
        return task;
    }

    private Theme createTheme(String name, Long cost) {
        Theme theme = new Theme();
        theme.setName(name);
        theme.setCost(cost);

        return theme;
    }
}