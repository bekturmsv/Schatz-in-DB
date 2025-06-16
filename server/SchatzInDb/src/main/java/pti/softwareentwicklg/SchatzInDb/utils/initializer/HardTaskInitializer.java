package pti.softwareentwicklg.SchatzInDb.utils.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskInteractionType;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;
import pti.softwareentwicklg.SchatzInDb.model.task.Task;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;

import java.util.List;

@Component
public class HardTaskInitializer {

    private final TaskRepository taskRepository;

    public HardTaskInitializer(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @PostConstruct
    public void initHardTasks() {
//        if (taskRepository.count() > 0) {
//            return;
//        }
        List<Task> hardTasks = List.of(
                createTask(
                        "T054",
                        "Alle Verdächtigen Schwerverbrecher\n" +
                                "> Von der Art, wie “Kleiner” über seine Kollegen sprach, lässt sich feststellen, dass dies geübte Übeltäter sind mit Vorerfahrung.\n\n" +
                                "Aufgabe: Liste aller registrierten Verbrecher auf mit min. 3 Vorstrafen.",
                        "SELECT name FROM verdaechtiger WHERE vorstrafen >= 3",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE mit Vergleichsoperator ≥.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T055",
                        "Alle die “Ring” sein können\n" +
                                "> “Kleiner” erzählte, dass wenn sie sich treffen, sie immer eine Verkleidung tragen und sich immer an einem kurz zuvor bestimmten Ort der Treffpunkt sei. So lässt sich schwer erkennen wer wer ist, aber er erkannte, dass das Mitglied “Ring” ein min. 1,80 m großer Mann war, der blond ist.\n\n" +
                                "Aufgabe: Liste aller männlich Verbrecher, die min. 180 cm groß und blond sind.",
                        "SELECT name FROM verdaechtiger WHERE geschlecht = 'm' AND groesse >= 180 AND haarfarbe = 'blond'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE mit mehreren Bedingungen und AND.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T056",
                        "Alle die “Zeiger” sein können\n" +
                                "> Das Mitglied “Zeiger” wurde beschrieben als ein eher junger Mann Anfang 20, der nicht über die 170 cm hinauskommt.\n\n" +
                                "Aufgabe: Liste alle männlich Verbrecher, die maximal 170 cm groß und blond sind.",
                        "SELECT name FROM verdaechtiger WHERE geschlecht = 'm' AND groesse <= 170 AND haarfarbe = 'blond'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE mit ≤ und AND.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T057",
                        "Alle die “Daumen” sein könnten\n" +
                                "> “Daumen” ist nach Beschreibung von “Kleiner” eine Frau in ihren 30ern, die anscheinend nicht aus Table-Town kommt.\n\n" +
                                "Aufgabe: Liste alle weiblichen Verbrecher, die nicht aus Table-Town sind.",
                        "SELECT name FROM verdaechtiger WHERE geschlecht = 'w' AND wohnort <> 'Table-Town'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE mit <> (NOT EQUAL).",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T058",
                        "Wohnorte mit mehreren Vorstrafen\n" +
                                "> Nach weiteren Analysen fiel Ihnen auf, dass einige Wohnorte besonders häufig durch Vorstrafen auffallen. Vielleicht sind diese Orte ein guter Ansatzpunkt für weitere Ermittlungen.\n\n" +
                                "Aufgabe: Liste alle Wohnorte mit durchschnittlich mehr als 2 Vorstrafen.",
                        "SELECT wohnort, AVG(vorstrafen) FROM verdaechtiger GROUP BY wohnort HAVING AVG(vorstrafen) > 2",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY + HAVING mit AVG.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T059",
                        "Zeugen ohne Aussagen\n" +
                                "> Bei der Überprüfung der Fallakten stellen Sie fest, dass einige Zeugen zwar registriert sind, aber bisher keine Aussage gemacht haben. Diese müssen noch befragt werden.\n\n" +
                                "Aufgabe: Liste alle Zeugen, die keinem Fall zugeordnet sind.",
                        "SELECT zeuge.name FROM zeuge LEFT JOIN fall_zeuge ON zeuge.id = fall_zeuge.zeuge_id WHERE fall_zeuge.fall_id IS NULL",
                        SqlKategorie.JOIN,
                        Schwierigkeit.HARD,
                        "Verwende LEFT JOIN und filtere WHERE … IS NULL.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T060",
                        "Häufigste Haarfarben\n" +
                                "> Während Sie Täterprofile erstellen, fällt Ihnen auf, dass Haarfarbenmuster bei Serienverbrechen eine Rolle spielen könnten.\n\n" +
                                "Aufgabe: Zeige Haarfarben, die mindestens 3-mal vorkommen.",
                        "SELECT haarfarbe, COUNT(*) FROM verdaechtiger GROUP BY haarfarbe HAVING COUNT(*) >= 3",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY + HAVING mit COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T061",
                        "Verdächtige aus Table-Town\n" +
                                "> Sie konzentrieren sich auf lokale Täter, da diese sich in der Umgebung gut auskennen und schwerer zu fassen sind.\n\n" +
                                "Aufgabe: Liste alle Verdächtigen aus Table-Town.",
                        "SELECT name FROM verdaechtiger WHERE wohnort = 'Table-Town'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE mit Gleichheitsvergleich.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T062",
                        "Durchschnittsalter erfassen\n" +
                                "> Statistische Analysen können helfen, Täterprofile zu verbessern. Alter ist oft ein entscheidender Faktor.\n\n" +
                                "Aufgabe: Ermittle das Durchschnittsalter aller Verdächtigen.",
                        "SELECT AVG(alter) FROM verdaechtiger",
                        SqlKategorie.AVG,
                        Schwierigkeit.HARD,
                        "Verwende AVG, um den Mittelwert zu berechnen.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T063",
                        "Zeugen mit gleichem Wohnort wie Verdächtige\n" +
                                "> Manche Zeugen könnten die Verdächtigen kennen, weil sie aus dem gleichen Wohnort stammen.\n\n" +
                                "Aufgabe: Liste Zeugen, die aus dem gleichen Wohnort wie ein Verdächtiger kommen.",
                        "SELECT DISTINCT zeuge.name FROM zeuge INNER JOIN verdaechtiger ON zeuge.wohnort = verdaechtiger.wohnort",
                        SqlKategorie.JOIN,
                        Schwierigkeit.HARD,
                        "Verwende INNER JOIN mit ON und DISTINCT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T064",
                        "Fälle mit hohem Schaden\n" +
                                "> Je höher der entstandene Schaden, desto lukrativer war der Fall für die Täter. Solche Fälle müssen priorisiert werden.\n\n" +
                                "Aufgabe: Liste alle Tatorte mit Schäden über 10.000 Euro.",
                        "SELECT tatort FROM fall WHERE schadenssumme > 10000",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE mit Vergleichsoperator >.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T065",
                        "Ermittler mit vielen Fällen\n" +
                                "> Es interessiert Sie, welcher Ermittler besonders viele Fälle betreut – vielleicht gibt es hier interessante Auffälligkeiten.\n\n" +
                                "Aufgabe: Liste alle Ermittler und die Anzahl ihrer Fälle, absteigend sortiert.",
                        "SELECT ermittler, COUNT(*) FROM fall GROUP BY ermittler ORDER BY COUNT(*) DESC",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY und ORDER BY mit COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T066",
                        "Tatorte pro Verdächtiger\n" +
                                "> Manche Verdächtige sind besonders aktiv. Finden Sie heraus, wie viele Tatorte ein Verdächtiger durchschnittlich hat.\n\n" +
                                "Aufgabe: Zähle für jeden Verdächtigen die Anzahl der zugeordneten Tatorte.",
                        "SELECT verdaechtiger.name, COUNT(fall_id) FROM fall_verdaechtiger INNER JOIN verdaechtiger ON verdaechtiger.id = fall_verdaechtiger.verdaechtiger_id GROUP BY verdaechtiger.name",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY mit JOIN und COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T067",
                        "Tatorte ohne Zeugen\n" +
                                "> Fälle ohne Zeugen sind deutlich schwerer aufzuklären. Finden Sie diese, um gezielt nach Spuren zu suchen.\n\n" +
                                "Aufgabe: Liste alle Tatorte ohne Zeugen.",
                        "SELECT fall.tatort FROM fall LEFT JOIN fall_zeuge ON fall.id = fall_zeuge.fall_id WHERE fall_zeuge.zeuge_id IS NULL",
                        SqlKategorie.JOIN,
                        Schwierigkeit.HARD,
                        "Verwende LEFT JOIN und filtere WHERE … IS NULL.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T068",
                        "Verdächtige ohne Vorstrafen\n" +
                                "> Einige Verdächtige sind noch ohne Vorstrafen — mögliche Ersttäter? Diese könnten besonders riskant sein.\n\n" +
                                "Aufgabe: Liste alle Verdächtigen ohne Vorstrafen.",
                        "SELECT name FROM verdaechtiger WHERE vorstrafen = 0",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE mit Gleichheitsvergleich.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T069",
                        "Zeugen mit mehreren Aussagen\n" +
                                "> Zeugen, die in mehreren Fällen aussagen, sind besonders wertvoll. Finden Sie diese Schlüsselfiguren.\n\n" +
                                "Aufgabe: Liste alle Zeugen, die bei mehr als einem Fall aussagten.",
                        "SELECT zeuge.name, COUNT(*) FROM fall_zeuge INNER JOIN zeuge ON zeuge.id = fall_zeuge.zeuge_id GROUP BY zeuge.name HAVING COUNT(*) > 1",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY und HAVING с COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T070",
                        "Fälle mit vielen Verdächtigen\n" +
                                "> Fälle mit vielen Verdächtigen deuten auf organisierte Kriminalität hin. Finden Sie diese besonders комплексные случаи.\n\n" +
                                "Aufgabe: Liste Fälle mit mindestens 3 Verdächtigen.",
                        "SELECT fall_id, COUNT(*) FROM fall_verdaechtiger GROUP BY fall_id HAVING COUNT(*) >= 3",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY и HAVING с COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T071",
                        "Fahrzeuge mehrfach benutzt\n" +
                                "> Manche Fluchtfahrzeuge werden bei mehreren Taten genutzt. Solche Fahrzeuge könnten wichtige Beweise liefern.\n\n" +
                                "Aufgabe: Liste Fahrzeuge, die bei mehr als einem Fall verwendet wurden.",
                        "SELECT fahrzeug_id, COUNT(*) FROM fall_fahrzeug GROUP BY fahrzeug_id HAVING COUNT(*) > 1",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY и HAVING с COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T072",
                        "Auswärtige Verdächtige\n" +
                                "> Verdächtige, die nicht aus Table-Town stammen, könnten sich schwerer in der Stadt zurechtfinden.\n\n" +
                                "Aufgabe: Liste alle Verdächtigen, die nicht aus Table-Town stammen.",
                        "SELECT name FROM verdaechtiger WHERE wohnort <> 'Table-Town'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE с Ungleichheit (<>).",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T073",
                        "Verdächtige mit identischer Haarfarbe\n" +
                                "> Ähnliche Haarfarben bei Verdächtigen könnten auf gemeinsame Herkunft или Gruppenzugehörigkeit hindeuten.\n\n" +
                                "Aufgabe: Liste Haarfarben, die mindestens zweimal bei Verdächtigen vorkommen.",
                        "SELECT haarfarbe, COUNT(*) FROM verdaechtiger GROUP BY haarfarbe HAVING COUNT(*) >= 2",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY и HAVING с COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T074",
                        "Ermittler mit hohem Durchschnittsschaden\n" +
                                "> Manche Ermittler sind на besonders lukrative Fälle spezialisiert. Finden Sie diese Ermittler.\n\n" +
                                "Aufgabe: Liste Ermittler mit durchschnittlichem Schadenswert über 8.000 Euro.",
                        "SELECT ermittler, AVG(schadenssummme) FROM fall GROUP BY ermittler HAVING AVG(schadenssumme) > 8000",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY и HAVING с AVG.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "T075",
                        "Verdächtige mit genau einer Vorstrafe\n" +
                                "> Ersttäter haben oft ein anderes Täterprofil als Wiederholungstäter. Finden Sie alle Verdächtigen mit genau einer Vorstrafe.\n\n" +
                                "Aufgabe: Liste alle Verdächtigen mit genau einer Vorstrafe.",
                        "SELECT name FROM verdaechtiger WHERE vorstrafen = 1",
                        SqlKategorie.WHERE,
                        Schwierigkeit.HARD,
                        "Verwende WHERE mit Gleichheitsvergleich.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "TEST007",
                        "Test-Aufgabe 1: Die gefährlichsten Mitglieder (GROUP BY + HAVING)\n" +
                                "> In den Akten finden sich viele Verdächtige mit Vorstrafen. Sie vermuten, dass Wohnorte mit besonders vielen vorbestraften Personen Hauptstützpunkte der Shadowhand sein könnten.\n\n" +
                                "Aufgabe: Finde alle Wohnorte, in denen mehr als 2 Verdächtige wohnen, die mindestens 3 Vorstrafen haben.",
                        "SELECT wohnort, COUNT(*) FROM verdaechtiger WHERE vorstrafen >= 3 GROUP BY wohnort HAVING COUNT(*) > 2",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende GROUP BY mit HAVING COUNT(*) > 2.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "TEST008",
                        "Test-Aufgabe 2: Fahrzeuge aufspüren (INNER JOIN + GROUP BY + HAVING)\n" +
                                "> Einige Fluchtfahrzeuge wurden mehrmals bei Verbrechen verwendet, ein klares Zeichen für organisierte Kriminalität.\n\n" +
                                "Aufgabe: Finde alle Kennzeichen von Fahrzeugen, die in mindestens 3 verschiedenen Fällen benutzt wurden.",
                        "SELECT fahrzeug.kennzeichen, COUNT(*) FROM fahrzeug INNER JOIN fall_fahrzeug ON fahrzeug.id = fall_fahrzeug.fahrzeug_id GROUP BY fahrzeug.kennzeichen HAVING COUNT(*) >= 3",
                        SqlKategorie.HAVING,
                        Schwierigkeit.HARD,
                        "Verwende INNER JOIN und GROUP BY mit HAVING COUNT(*) >= 3.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        15
                ),
                createTask(
                        "TEST009",
                        "Test-Aufgabe 3: Verbindungen zwischen Zeugen und Verdächtigen aufdecken (INNER JOIN + DISTINCT)\n" +
                                "> Ermittlungen zeigen, dass die Shadowhand oft über frühere Bekanntschaften agiert hat. Es ist wichtig herauszufinden, welche Verdächtigen aus denselben Wohnorten stammen wie Zeugen.\n\n" +
                                "Aufgabe: Liste die Namen aller Verdächtigen, die aus demselben Wohnort wie mindestens ein Zeuge kommen.",
                        "SELECT DISTINCT verdaechtiger.name FROM verdaechtiger INNER JOIN zeuge ON verdaechtiger.wohnort = zeuge.wohnort",
                        SqlKategorie.JOIN,
                        Schwierigkeit.HARD,
                        "Verwende INNER JOIN mit DISTINCT.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        15
                )
        );



        for (Task t : hardTasks) {
            if (!taskRepository.existsByTaskCode(t.getTaskCode())) {
                taskRepository.save(t);
            }
        }
    }

        private Task createTask(String code,
                String aufgabe,
                String solution,
                SqlKategorie kategorie,
                Schwierigkeit schwierigkeitsgrad,
                String hint,
                TaskType type,
                TaskInteractionType interactionType,
                Integer points) {
            Task task = new Task();
            task.setTaskCode(code);
            task.setAufgabe(aufgabe);
            task.setSolution(solution);
            task.setKategorie(kategorie);
            task.setSchwierigkeitsgrad(schwierigkeitsgrad);
            task.setHint(hint);
            task.setTaskType(type);
            task.setInteractionType(interactionType);
            task.setPoints(points);
            return task;
        }
}
