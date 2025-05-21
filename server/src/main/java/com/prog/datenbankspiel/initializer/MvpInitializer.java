package com.prog.datenbankspiel.initializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prog.datenbankspiel.model.task.enums.LevelDifficulty;
import com.prog.datenbankspiel.model.task.enums.TaskType;
import com.prog.datenbankspiel.model.task.Task;
import com.prog.datenbankspiel.model.task.Topic;
import com.prog.datenbankspiel.repository.task.TaskRepository;
import com.prog.datenbankspiel.repository.task.TopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MvpInitializer {

    @Bean
    public CommandLineRunner init(TaskRepository taskRepo, TopicRepository topicRepo) {
        return args -> {

            // === WHERE ===
            Topic where = saveTopicIfNotExists(topicRepo, "WHERE", LevelDifficulty.EASY);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode sampleData1 = mapper.readTree("""
                {
                  "verdaechtiger": [
                    { "id": 1, "name": "Markus Müller", "haarfarbe": "braun", "geschlecht": "m", "schuhgroesse": 44, "alter": 34, "wohnort": "Zwickau" },
                    { "id": 2, "name": "Thomas Becker", "haarfarbe": "braun", "geschlecht": "m", "schuhgroesse": 42, "alter": 47, "wohnort": "Leipzig" },
                    { "id": 3, "name": "Sabine Keller", "haarfarbe": "schwarz", "geschlecht": "w", "schuhgroesse": 39, "alter": 29, "wohnort": "Dresden" },
                    { "id": 4, "name": "Maria Weber", "haarfarbe": "blond", "geschlecht": "w", "schuhgroesse": 37, "alter": 65, "wohnort": "Zwickau" },
                    { "id": 5, "name": "Peter Schneider", "haarfarbe": "schwarz", "geschlecht": "m", "schuhgroesse": 44, "alter": 72, "wohnort": "Chemnitz" },
                    { "id": 6, "name": "Uwe Richter", "haarfarbe": "braun", "geschlecht": "m", "schuhgroesse": 45, "alter": 70, "wohnort": "Zwickau" }
                  ]
                }
            """);
            JsonNode sampleData2 = mapper.readTree("""
                {
                  "fall": [
                    { "fall_id": 101, "anzahl_gegenstaende": 2, "schaden": 4500 },
                    { "fall_id": 102, "anzahl_gegenstaende": 4, "schaden": 9200 }
                  ]
                }
            """);
            createTaskIfNotExists(taskRepo, where,
                    "Braunes Haar am Tatort",
                    "Ein Ermittler fand am Tatort einige Spuren von kurzem braunem Haar. Ein anderer fand einen Fußabdruck. Dem Aussehen schlussfolgernd handelt es sich um einen Männerschuh der Größe 44. Erstelle eine Liste Verdächtiger die dieser Beschreibung passen",
                    """
                            ["Markus Müller","Thomas Becker","Uwe Richter"]""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, where,
                    "Fußabdruck gefunden",
                    "Ein Ermittler fand am Tatort einige Spuren von kurzem braunem Haar. Ein anderer fand einen Fußabdruck. Dem Aussehen schlussfolgernd handelt es sich um einen Männerschuh der Größe 44. Erstelle eine Liste Verdächtiger die dieser Beschreibung passen",
                    """
                            ["Markus Müller","Peter Schneider"]""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, where,
                    "Täter ist männlich",
                    "Ein Ermittler fand am Tatort einige Spuren von kurzem braunem Haar. Ein anderer fand einen Fußabdruck. Dem Aussehen schlussfolgernd handelt es sich um einen Männerschuh der Größe 44. Erstelle eine Liste Verdächtiger die dieser Beschreibung passen",
                    """
                            ["Markus Müller","Thomas Becker","Peter Schneider","Uwe Richter"]""",
                    sampleData1);

            // === SELECT ===
            Topic select = saveTopicIfNotExists(topicRepo, "SELECT", LevelDifficulty.EASY);

            createTaskIfNotExists(taskRepo, select,
                    "Liste von Verdächtigen",
                    "Es wurde eine  Liste von Verdächtigen zusammengestellt. Diese aufgerufen werden.",
                    """
                            ["Markus Müller","Thomas Becker","Sabine Keller","Maria Weber","Peter Schneider","Uwe Richter"]""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, select,
                    "Männliche Verdächtige",
                    "Ein Passant sah einen verdächtigen Mann am Tatort, er war aber nicht in der Lage ein Gesicht oder andere Merkmale zu erkennen. Durchsuche die Datenbank nach allen männlichen verdächtigen",
                    """
                            [{"name":"Markus Müller","geschlecht":"m"},{"name":"Thomas Becker","geschlecht":"m"},{"name":"Peter Schneider","geschlecht":"m"},{"name":"Uwe Richter","geschlecht":"m"}]""",
                    sampleData1);

            // === ORDER BY ===
            Topic orderBy = saveTopicIfNotExists(topicRepo, "ORDER BY", LevelDifficulty.EASY);

            createTaskIfNotExists(taskRepo, orderBy,
                    "Sortieren nach Alter",
                    "Um die Suche zu erleichtern, sortiere die Verdächtigen nach Alter",
                    """
                            [{"name":"Sabine Keller","alter":29},{"name":"Markus Müller","alter":34},{"name":"Thomas Becker","alter":47},{"name":"Maria Weber","alter":65},{"name":"Uwe Richter","alter":70},{"name":"Peter Schneider","alter":72}]""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, orderBy,
                    "Sortieren nach Name",
                    "Um die Suche zu erleichtern, sortiere die Verdächtigen aufsteigend nach Name.",
                    """
                            ["Maria Weber","Markus Müller","Peter Schneider","Sabine Keller","Thomas Becker","Uwe Richter"]""",
                    sampleData1);

            // === AND, OR, NOT ===
            Topic logic = saveTopicIfNotExists(topicRepo, "AND, OR, NOT", LevelDifficulty.EASY);

            createTaskIfNotExists(taskRepo, logic,
                    "Haarfarbe Braun und männlich",
                    "Es wurde ein Mann mit braunen Haaren am Tatort gesichtet, der sich auffällig verhielt. Gib eine Liste von Verdächtigen aus, die auf die Beschreibung passen.",
                    """
                            ["Markus Müller","Thomas Becker","Uwe Richter"]""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, logic,
                    "Braun oder Schwarz",
                    "Es wurde eine Person mit dunklen Haaren am Tatort gesichtet, die sich auffällig verhielt. Gib eine Liste von Verdächtigen aus, die auf die Beschreibung passen.",
                    """
                            ["Markus Müller","Thomas Becker","Sabine Keller","Peter Schneider","Uwe Richter"]""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, logic,
                    "Nicht älter als 70",
                    "Es wurde ein Mann am Tatort gesichtet, der sich auffällig verhielt. Es wurde aber nur festgestellt, dass dieser nicht älter als 70 sein kann.  Gib eine Liste von Verdächtigen aus, die auf die Beschreibung passen.",
                    """
                            [{"name":"Sabine Keller","alter":29},{"name":"Markus Müller","alter":34},{"name":"Thomas Becker","alter":47},{"name":"Maria Weber","alter":65},{"name":"Uwe Richter","alter":70}]""",
                    sampleData1);

            // === DISTINCT ===
            Topic distinct = saveTopicIfNotExists(topicRepo, "DISTINCT", LevelDifficulty.EASY);

            createTaskIfNotExists(taskRepo, distinct,
                    "Wohnorte der Verdächtigen",
                    "Es wird für einen Bericht verlangt, dass die Wohnorte der Verdächtigen festgehalten werden. Gib eine Liste an, die alle auftretenden Wohnorte der Verdächtigen enthält.",
                    """
                            ["Zwickau","Leipzig","Dresden","Chemnitz"]""",
                    sampleData1);

            // === COUNT, SUM, AVG ===
            Topic aggregate = saveTopicIfNotExists(topicRepo, "COUNT, SUM, AVG", LevelDifficulty.MEDIUM);

            createTaskIfNotExists(taskRepo, aggregate,
                    "Durchschnittsalter",
                    "Es wird für einen Bericht verlangt, dass das Durchschnittsalter der Verdächtigen festgehalten wird. Ermittle das Durchschnittsalter der Verdächtigen.",
                    """
                            {"durchschnittsalter":52.83}""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, aggregate,
                    "Anzahl in Zwickau",
                    "Es wird für einen Bericht verlangt, dass die Anzahl der Verdächtigen, die aus Zwickau kommen festgestellt werden. Ermittle das Anzahr der Verdächtigen die aus Zwickau kommen.",
                    """
                            {"anzahl":3}""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, aggregate,
                    "Anzahl gestohlener Gegenstände",
                    "Es wird für einen Bericht verlangt, dass die Anzahl der gestohlenen Gegenstände festgehalten wird. Ermittle die Gesamtanzahl der gestohlenen Gegenstände des Falles.",
                    """
                            {"gesamtanzahl":6}""",
                    sampleData2);

            createTaskIfNotExists(taskRepo, aggregate,
                    "Gesamtschaden",
                    "Es wird für einen Bericht verlangt, dass die Gesamtsumme des festgestellten Schadens festgehalten wird. Ermittle die Gesamtsumme des Schadens des Falles.",
                    """
                            {"gesamtschaden":13700}""",
                    sampleData2);

            // === GROUP BY ===
            Topic groupBy = saveTopicIfNotExists(topicRepo, "GROUP BY", LevelDifficulty.MEDIUM);

            createTaskIfNotExists(taskRepo, groupBy,
                    "Gruppierung nach Wohnort",
                    "Zur besseren Ermittlung sollen die Verdächtigen nach ihrem Wohnort zusammengefasst werden.",
                    """
                            [{"wohnort":"Zwickau","anzahl":3},{"wohnort":"Leipzig","anzahl":1},{"wohnort":"Dresden","anzahl":1},{"wohnort":"Chemnitz","anzahl":1}]""",
                    sampleData1);

            createTaskIfNotExists(taskRepo, groupBy,
                    "Gruppierung nach Haarfarbe",
                    "Zur besseren Ermittlung sollen die Verdächtigen nach ihrer Haarfarbe zusammengefasst werden.",
                    """
                    [{"haarfarbe":"braun","anzahl":3},{"haarfarbe":"schwarz","anzahl":2},{"haarfarbe":"blond","anzahl":1}]""",
                    sampleData1);

            // === HAVING ===
            Topic having = saveTopicIfNotExists(topicRepo, "HAVING", LevelDifficulty.MEDIUM);

            createTaskIfNotExists(taskRepo, having,
                    "Wohnorte mit ≥ 3 Verdächtigen",
                    "Es wird für einen Bericht benötigt, in welchen Wohnorten es mehr als min. 3 Verdächtige gibt.",
                    """
                    {"wohnort":"Zwickau","anzahl":3}""",
                    sampleData1);
        };
    }

    private void createTaskIfNotExists(TaskRepository taskRepo, Topic topic, String title, String description, String sql, JsonNode sampleData) {
        if (!taskRepo.existsByTitle(title)) {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTaskAnswer(sql);
            task.setTopic(topic);
            task.setLevelDifficulty(topic.getLevelDifficulty());
            task.setTaskType(TaskType.FULL_INPUT);
            task.setPoints(10L);
            task.setSampleData(sampleData);
            taskRepo.save(task);
        }
    }

    private Topic saveTopicIfNotExists(TopicRepository topicRepo, String name, LevelDifficulty difficulty) {
        return topicRepo.findByName(name).orElseGet(() -> {
            Topic topic = new Topic();
            topic.setName(name);
            topic.setLevelDifficulty(difficulty);
            return topicRepo.save(topic);
        });
    }
}



