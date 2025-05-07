package com.prog.datenbankspiel.initializer;

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

            createTaskIfNotExists(taskRepo, where,
                    "Braunes Haar am Tatort",
                    "Ein Ermittler fand am Tatort einige Spuren von kurzem braunem Haar. Ein anderer fand einen Fußabdruck. Dem Aussehen schlussfolgernd handelt es sich um einen Männerschuh der Größe 44. Erstelle eine Liste Verdächtiger die dieser Beschreibung passen",
                    "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun';");

            createTaskIfNotExists(taskRepo, where,
                    "Fußabdruck gefunden",
                    "Ein Ermittler fand am Tatort einige Spuren von kurzem braunem Haar. Ein anderer fand einen Fußabdruck. Dem Aussehen schlussfolgernd handelt es sich um einen Männerschuh der Größe 44. Erstelle eine Liste Verdächtiger die dieser Beschreibung passen",
                    "SELECT name FROM verdaechtiger WHERE schuhgroesse = 44;");

            createTaskIfNotExists(taskRepo, where,
                    "Täter ist männlich",
                    "Ein Ermittler fand am Tatort einige Spuren von kurzem braunem Haar. Ein anderer fand einen Fußabdruck. Dem Aussehen schlussfolgernd handelt es sich um einen Männerschuh der Größe 44. Erstelle eine Liste Verdächtiger die dieser Beschreibung passen",
                    "SELECT name FROM verdaechtiger WHERE geschlecht = 'm';");

            // === SELECT ===
            Topic select = saveTopicIfNotExists(topicRepo, "SELECT", LevelDifficulty.EASY);

            createTaskIfNotExists(taskRepo, select,
                    "Liste von Verdächtigen",
                    "Es wurde eine  Liste von Verdächtigen zusammengestellt. Diese aufgerufen werden.",
                    "SELECT name FROM verdaechtiger;");

            createTaskIfNotExists(taskRepo, select,
                    "Männliche Verdächtige",
                    "Ein Passant sah einen verdächtigen Mann am Tatort, er war aber nicht in der Lage ein Gesicht oder andere Merkmale zu erkennen. Durchsuche die Datenbank nach allen männlichen verdächtigen",
                    "SELECT name, geschlecht FROM verdaechtiger;");

            // === ORDER BY ===
            Topic orderBy = saveTopicIfNotExists(topicRepo, "ORDER BY", LevelDifficulty.EASY);

            createTaskIfNotExists(taskRepo, orderBy,
                    "Sortieren nach Alter",
                    "Um die Suche zu erleichtern, sortiere die Verdächtigen nach Alter",
                    "SELECT name, alter FROM verdaechtiger ORDER BY alter;");

            createTaskIfNotExists(taskRepo, orderBy,
                    "Sortieren nach Name",
                    "Um die Suche zu erleichtern, sortiere die Verdächtigen aufsteigend nach Name.",
                    "SELECT name FROM verdaechtiger ORDER BY name ASC;");

            // === AND, OR, NOT ===
            Topic logic = saveTopicIfNotExists(topicRepo, "AND, OR, NOT", LevelDifficulty.EASY);

            createTaskIfNotExists(taskRepo, logic,
                    "Haarfarbe Braun und männlich",
                    "Es wurde ein Mann mit braunen Haaren am Tatort gesichtet, der sich auffällig verhielt. Gib eine Liste von Verdächtigen aus, die auf die Beschreibung passen.",
                    "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun' AND geschlecht = 'm';");

            createTaskIfNotExists(taskRepo, logic,
                    "Braun oder Schwarz",
                    "Es wurde eine Person mit dunklen Haaren am Tatort gesichtet, die sich auffällig verhielt. Gib eine Liste von Verdächtigen aus, die auf die Beschreibung passen.",
                    "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun' OR haarfarbe = 'schwarz';");

            createTaskIfNotExists(taskRepo, logic,
                    "Nicht älter als 70",
                    "Es wurde ein Mann am Tatort gesichtet, der sich auffällig verhielt. Es wurde aber nur festgestellt, dass dieser nicht älter als 70 sein kann.  Gib eine Liste von Verdächtigen aus, die auf die Beschreibung passen.",
                    "SELECT name, alter FROM verdaechtiger WHERE NOT alter <= 70;");

            // === DISTINCT ===
            Topic distinct = saveTopicIfNotExists(topicRepo, "DISTINCT", LevelDifficulty.EASY);

            createTaskIfNotExists(taskRepo, distinct,
                    "Wohnorte der Verdächtigen",
                    "Es wird für einen Bericht verlangt, dass die Wohnorte der Verdächtigen festgehalten werden. Gib eine Liste an, die alle auftretenden Wohnorte der Verdächtigen enthält.",
                    "SELECT DISTINCT wohnort FROM verdaechtiger;");

            // === COUNT, SUM, AVG ===
            Topic aggregate = saveTopicIfNotExists(topicRepo, "COUNT, SUM, AVG", LevelDifficulty.MEDIUM);

            createTaskIfNotExists(taskRepo, aggregate,
                    "Durchschnittsalter",
                    "Es wird für einen Bericht verlangt, dass das Durchschnittsalter der Verdächtigen festgehalten wird. Ermittle das Durchschnittsalter der Verdächtigen.",
                    "SELECT AVG(alter) AS durchschnittsalter FROM verdaechtiger;");

            createTaskIfNotExists(taskRepo, aggregate,
                    "Anzahl in Zwickau",
                    "Es wird für einen Bericht verlangt, dass die Anzahl der Verdächtigen, die aus Zwickau kommen festgestellt werden. Ermittle das Anzahr der Verdächtigen die aus Zwickau kommen.",
                    "SELECT COUNT(*) AS anzahl FROM verdaechtiger WHERE wohnort = 'Zwickau';");

            createTaskIfNotExists(taskRepo, aggregate,
                    "Anzahl gestohlener Gegenstände",
                    "Es wird für einen Bericht verlangt, dass die Anzahl der gestohlenen Gegenstände festgehalten wird. Ermittle die Gesamtanzahl der gestohlenen Gegenstände des Falles.",
                    "SELECT SUM(anzahl_gegenstaende) AS gesamtanzahl FROM fall;");

            createTaskIfNotExists(taskRepo, aggregate,
                    "Gesamtschaden",
                    "Es wird für einen Bericht verlangt, dass die Gesamtsumme des festgestellten Schadens festgehalten wird. Ermittle die Gesamtsumme des Schadens des Falles.",
                    "SELECT SUM(schaden) AS gesamtschaden FROM fall;");

            // === GROUP BY ===
            Topic groupBy = saveTopicIfNotExists(topicRepo, "GROUP BY", LevelDifficulty.MEDIUM);

            createTaskIfNotExists(taskRepo, groupBy,
                    "Gruppierung nach Wohnort",
                    "Zur besseren Ermittlung sollen die Verdächtigen nach ihrem Wohnort zusammengefasst werden.",
                    "SELECT wohnort, COUNT(*) AS anzahl FROM verdaechtiger GROUP BY wohnort;");

            createTaskIfNotExists(taskRepo, groupBy,
                    "Gruppierung nach Haarfarbe",
                    "Zur besseren Ermittlung sollen die Verdächtigen nach ihrer Haarfarbe zusammengefasst werden.",
                    "SELECT haarfarbe, COUNT(*) AS anzahl FROM verdaechtiger GROUP BY haarfarbe;");

            // === HAVING ===
            Topic having = saveTopicIfNotExists(topicRepo, "HAVING", LevelDifficulty.MEDIUM);

            createTaskIfNotExists(taskRepo, having,
                    "Wohnorte mit ≥ 3 Verdächtigen",
                    "Es wird für einen Bericht benötigt, in welchen Wohnorten es mehr als min. 3 Verdächtige gibt.",
                    "SELECT wohnort, COUNT(*) AS anzahl FROM verdaechtiger GROUP BY wohnort HAVING COUNT(*) >= 3;");
        };
    }

    private void createTaskIfNotExists(TaskRepository taskRepo, Topic topic, String title, String description, String sql) {
        if (!taskRepo.existsByTitle(title)) {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTaskAnswer(sql);
            task.setTopic(topic);
            task.setLevelDifficulty(topic.getLevelDifficulty());
            task.setTaskType(TaskType.FULL_INPUT);
            task.setPoints(10L);
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



