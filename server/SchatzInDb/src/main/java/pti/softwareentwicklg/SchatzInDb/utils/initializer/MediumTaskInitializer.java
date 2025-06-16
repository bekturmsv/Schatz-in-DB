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
public class MediumTaskInitializer {

    private final TaskRepository taskRepository;

    public MediumTaskInitializer(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @PostConstruct
    public void initEasyTasks() {
//        if (taskRepository.count() > 0) {
//            return;
//        }
        List<Task> mediumTasks = List.of(
                createTask(
                        "T028",
                        "Alle Verdächtigen auflisten\n" +
                                "> Table-Town scheint ruhig, doch der Schein trügt. Auf Ihrem Schreibtisch stapeln sich alte Akten – viele Namen, viele Fragen.\n\n" +
                                "Ihre Aufgabe: Listen Sie alle registrierten Verdächtigen namentlich auf.",
                        "SELECT name FROM verdaechtiger",
                        SqlKategorie.SELECT,
                        Schwierigkeit.MEDIUM,
                        "Verwende SELECT, um alle Namen abzurufen.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T029",
                        "Tatorte und Schadenssummen\n" +
                                "> Im Schatten der Stadt wurden in letzter Zeit immer größere Schäden gemeldet. Die Tatorte häufen sich.\n\n" +
                                "Ihre Aufgabe: Erfassen Sie alle Tatorte mit der jeweiligen Schadenshöhe.",
                        "SELECT tatort, schadenssumme FROM fall",
                        SqlKategorie.SELECT,
                        Schwierigkeit.MEDIUM,
                        "Verwende SELECT mit mehreren Spalten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T030",
                        "Verdächtige mit braunen Haaren\n" +
                                "> Eine Haarprobe vom letzten Tatort wird untersucht. Ergebnis: braune Haare. Ein entscheidender Hinweis.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Verdächtigen mit braunen Haaren.",
                        "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: richtige Reihenfolge von SELECT, FROM und WHERE beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "T031",
                        "Zeugen über 35 Jahren\n" +
                                "> Ältere Zeugen liefern oft präzisere Aussagen. Vielleicht ist hier der Schlüssel.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Zeugen, die älter als 35 Jahre sind.",
                        "SELECT name FROM zeuge WHERE alter > 35",
                        SqlKategorie.WHERE,
                        Schwierigkeit.MEDIUM,
                        "Verwende WHERE mit Vergleichsoperator >.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T032",
                        "Verdächtige mit braunen oder schwarzen Haaren\n" +
                                "> Ein Augenzeuge erinnert sich: braune oder schwarze Haare. Sicherheitshalber müssen Sie beide Gruppen prüfen.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Verdächtigen mit braunen oder schwarzen Haaren.",
                        "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun' OR haarfarbe = 'schwarz'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.MEDIUM,
                        "Verwende WHERE mit OR.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        12
                ),
                createTask(
                        "T033",
                        "Verdächtige außerhalb Midtown\n" +
                                "> Midtown bleibt sauber – die Verdächtigen stammen wohl aus anderen Vierteln.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Verdächtigen, die nicht aus Midtown stammen.",
                        "SELECT name FROM verdaechtiger WHERE NOT wohnort = 'Midtown'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.MEDIUM,
                        "Verwende WHERE mit NOT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T034",
                        "Verdächtige alphabetisch sortieren (ORDER BY) (Fehler ausbessern)\n" +
                                "> Ordnung im Chaos: Sie brauchen eine alphabetische Liste der Verdächtigen.\n\n" +
                                "Ihre Aufgabe: Sortieren Sie die Namen aller Verdächtigen alphabetisch.",
                        "SELECT name FROM verdaechtiger ORDER BY name ASC",
                        SqlKategorie.ORDER_BY,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: richtige Reihenfolge von SELECT, FROM und ORDER BY beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "T035",
                        "Fälle nach Schadenshöhe sortieren\n" +
                                "> Je größer der Schaden, desto brisanter der Fall. Beginnen Sie mit den schwersten Fällen.\n\n" +
                                "Ihre Aufgabe: Listen Sie die Fälle absteigend nach Schadenshöhe.",
                        "SELECT tatort, schadenssumme FROM fall ORDER BY schadenssummen DESC",
                        SqlKategorie.ORDER_BY,
                        Schwierigkeit.MEDIUM,
                        "Verwende ORDER BY mit DESC.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T036",
                        "Unterschiedliche Wohnorte der Verdächtigen\n" +
                                "> Wo verstecken sich die Täter? Vielleicht gibt der Wohnort Hinweise.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle unterschiedlichen Wohnorte der Verdächtigen.",
                        "SELECT DISTINCT wohnort FROM verdaechtiger",
                        SqlKategorie.DISTINCT,
                        Schwierigkeit.MEDIUM,
                        "Verwende DISTINCT, um doppelte Wohnorte herauszufiltern.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        12
                ),
                createTask(
                        "T037",
                        "Anzahl der Verdächtigen\n" +
                                "> Wie viele Verdächtige sind überhaupt registriert? Ein Überblick ist unerlässlich.\n\n" +
                                "Ihre Aufgabe: Zählen Sie alle Verdächtigen.",
                        "SELECT COUNT(*) FROM verdaechtiger",
                        SqlKategorie.COUNT,
                        Schwierigkeit.MEDIUM,
                        "Verwende COUNT, um alle Zeilen zu zählen.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T038",
                        "Gesamtschadenssumme aller Fälle \n" +
                                "> Wie groß ist der angerichtete Gesamtschaden in Table-Town?\n\n" +
                                "Ihre Aufgabe: Berechnen Sie die Summe aller Schadenssummen.",
                        "SELECT SUM(schadenssumme) FROM fall",
                        SqlKategorie.SUM,
                        Schwierigkeit.MEDIUM,
                        "Verwende SUM, um alle Werte zu addieren.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T039",
                        "Durchschnittsalter der Verdächtigen (AVG) (Fehler ausbessern)\n" +
                                "> Ein Altersprofil könnte helfen, ein Muster zu erkennen.\n\n" +
                                "Ihre Aufgabe: Ermitteln Sie das Durchschnittsalter aller Verdächtigen.",
                        "SELECT AVG(alter) FROM verdaechtiger",
                        SqlKategorie.AVG,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: AVG-Funktion korrekt verwenden.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "T040",
                        "Fälle pro Ermittler (GROUP BY) (Fehler ausbessern)\n" +
                                "> Einige Ermittler haben besonders viele Fälle übernommen. Wer sind die Fleißigsten?\n\n" +
                                "Ihre Aufgabe: Zählen Sie die Fälle pro Ermittler.",
                        "SELECT ermittler, COUNT(*) FROM fall GROUP BY ermittler",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: korrekte Reihenfolge von SELECT, COUNT und GROUP BY beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "T041",
                        "Zeugenanzahl je Wohnort\n" +
                                "> Wo melden sich besonders viele Zeugen?\n\n" +
                                "Ihre Aufgabe: Gruppieren Sie die Zeugen nach Wohnort und zählen Sie die Anzahl.",
                        "SELECT wohnort, COUNT(*) FROM zeuge GROUP BY wohnort",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.MEDIUM,
                        "Verwende GROUP BY mit COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T042",
                        "Ermittler mit mehr als 2\n" +
                                "> Nur Ermittler mit vielen Fällen haben einen Überblick.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Ermittler, die mehr als zwei Fälle bearbeitet haben.",
                        "SELECT ermittler, COUNT(*) FROM fall GROUP BY ermittler HAVING COUNT(*) > 2",
                        SqlKategorie.HAVING,
                        Schwierigkeit.MEDIUM,
                        "Verwende GROUP BY und HAVING mit COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T043",
                        "Wohnorte mit mehr als 3 Verdächtigen (GROUP BY + HAVING) (Fehler ausbessern)\n" +
                                "> Gibt es Viertel, die besonders kriminell sind?\n\n" +
                                "Ihre Aufgabe: Finden Sie Wohnorte mit mehr als drei Verdächtigen.",
                        "SELECT wohnort, COUNT(*) FROM verdaechtiger GROUP BY wohnort HAVING COUNT(*) > 3",
                        SqlKategorie.HAVING,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: korrekte Platzierung von COUNT und GROUP BY beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "T044",
                        "Verdächtige aus verdächtigen Vierteln\n" +
                                "> Spezielle Viertel benötigen mehr Aufmerksamkeit.\n\n" +
                                "Ihre Aufgabe: Finden Sie Verdächtige aus Wohnorten mit mehr als drei Verdächtigen.",
                        "SELECT name FROM verdaechtiger WHERE wohnort IN (SELECT wohnort FROM verdaechtiger GROUP BY wohnort HAVING COUNT(*) > 3)",
                        SqlKategorie.SUBQUERY,
                        Schwierigkeit.MEDIUM,
                        "Verwende Subquery mit IN.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        12
                ),
                createTask(
                        "T045",
                        "Fahrzeuge ohne zugeordneten Fall\n" +
                                "> Einige Fahrzeuge tauchen nie in Akten auf – ein gefährliches Zeichen.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Fahrzeuge ohne zugeordneten Fall.",
                        "SELECT f.kennzeichen FROM fahrzeug f LEFT JOIN fall_fahrzeug ff ON f.id = ff.fahrzeug_id WHERE ff.fall_id IS NULL",
                        SqlKategorie.JOIN,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: korrekte Syntax für LEFT JOIN und WHERE IS NULL beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "T046",
                        "Zeugen und ihre Fälle\n" +
                                "> Welche Zeugen haben zu welchen Fällen ausgesagt?\n\n" +
                                "Ihre Aufgabe: Verbinden Sie Zeugen mit ihren jeweiligen Tatorten.",
                        "SELECT zeuge.name, fall.tatort FROM zeuge " +
                                "INNER JOIN fall_zeuge fz ON zeuge.id = fz.zeuge_id " +
                                "INNER JOIN fall ON fz.fall_id = fall.id",
                        SqlKategorie.JOIN,
                        Schwierigkeit.MEDIUM,
                        "Verwende INNER JOIN mit mehreren Tabellen und ON.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        12
                ),
                createTask(
                        "T047",
                        "Verdächtige und ihre Fälle (INNER JOIN) (Fehler ausbessern)\n" +
                                "> Ordnen Sie die Verdächtigen den Fällen zu.\n\n" +
                                "Ihre Aufgabe: Finden Sie Namen der Verdächtigen und ihre Tatorte.",
                        "SELECT verdaechtiger.name, fall.tatort FROM verdaechtiger " +
                                "INNER JOIN fall_verdaechtiger fv ON verdaechtiger.id = fv.verdaechtiger_id " +
                                "INNER JOIN fall ON fv.fall_id = fall.id",
                        SqlKategorie.JOIN,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: richtige Reihenfolge von JOINs und ON-Klauseln beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "T048",
                        "Zeugen ohne Fall \n" +
                                "> Zeugen, die noch nicht ausgesagt haben, könnten entscheidend sein.\n\n" +
                                "Ihre Aufgabe: Finden Sie Zeugen ohne zugeordneten Fall.",
                        "SELECT zeuge.name FROM zeuge LEFT JOIN fall_zeuge fz ON zeuge.id = fz.zeuge_id WHERE fz.fall_id IS NULL",
                        SqlKategorie.JOIN,
                        Schwierigkeit.MEDIUM,
                        "Verwende LEFT JOIN und filtere WHERE … IS NULL.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T049",
                        "Verdächtige ohne Fall (LEFT JOIN + WHERE NULL) (Fehler ausbessern)\n" +
                                "> Verdächtige ohne Akteneintrag? Das sollten Sie prüfen.\n\n" +
                                "Ihre Aufgabe: Finden Sie Verdächtige ohne bekannten Fall.",
                        "SELECT verdaechtiger.name FROM verdaechtiger LEFT JOIN fall_verdaechtiger fv ON verdaechtiger.id = fv.verdaechtiger_id WHERE fv.fall_id IS NULL",
                        SqlKategorie.JOIN,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: richtige Reihenfolge von FROM, JOIN und WHERE beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "T050",
                        "Zeugen aus demselben Wohnort wie ein Verdächtiger\n" +
                                "> Gemeinsame Herkunft könnte Verbindungen offenbaren.\n\n" +
                                "Ihre Aufgabe: Finden Sie Zeugen aus denselben Wohnorten wie Verdächtige.",
                        "SELECT DISTINCT z.name FROM zeuge z INNER JOIN verdaechtiger v ON z.wohnort = v.wohnort",
                        SqlKategorie.JOIN,
                        Schwierigkeit.MEDIUM,
                        "Verwende INNER JOIN mit ON und DISTINCT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T051",
                        "Fälle ohne Zeugen\n" +
                                "> Fälle ohne Zeugen sind schwer aufzuklären.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Fälle ohne zugeordnete Zeugen.",
                        "SELECT fall.tatort FROM fall LEFT JOIN fall_zeuge fz ON fall.id = fz.fall_id WHERE fz.zeuge_id IS NULL",
                        SqlKategorie.JOIN,
                        Schwierigkeit.MEDIUM,
                        "Verwende LEFT JOIN und filtere WHERE … IS NULL.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        12
                ),
                createTask(
                        "T052",
                        "Ermittler mit den meisten Fällen\n" +
                                "> Ermitteln Sie, wer die meisten Fälle betreut.\n\n" +
                                "Ihre Aufgabe: Finden Sie Ermittler mit den meisten Fällen.",
                        "SELECT ermittler, COUNT(*) AS anzahl_faelle FROM fall GROUP BY ermittler ORDER BY anzahl_faelle DESC",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.MEDIUM,
                        "Verwende GROUP BY und ORDER BY mit COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        12
                ),
                createTask(
                        "T053",
                        "Verdächtige mit mehreren Vorstrafen (WHERE + ORDER BY) (Fehler ausbessern)\n" +
                                "> Wiederholungstäter müssen besonders genau überprüft werden.\n\n" +
                                "Ihre Aufgabe: Finden Sie Verdächtige mit mehr als zwei Vorstrafen.",
                        "SELECT name, vorstrafen FROM verdaechtiger WHERE vorstrafen > 2 ORDER BY vorstrafen DESC",
                        SqlKategorie.WHERE,
                        Schwierigkeit.MEDIUM,
                        "Fehlerbefehl korrigieren: richtige Reihenfolge von WHERE und ORDER BY beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        12
                ),
                createTask(
                        "TEST004",
                        "Test-Aufgabe 1: Der Schatten war nicht allein. (Befehl selber schreiben)\n" +
                                "> In alten Akten finden Sie Hinweise auf Komplizen mit mindestens drei Vorstrafen, die ebenfalls festgenommen wurden.\n\n" +
                                "Ihre Aufgabe: Finden Sie die Namen aller Verdächtigen, die mindestens drei Vorstrafen haben.",
                        "SELECT name FROM verdaechtiger WHERE vorstrafen >= 3",
                        SqlKategorie.WHERE,
                        Schwierigkeit.MEDIUM,
                        "Verwende WHERE mit vorstrafen >= 3.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "TEST005",
                        "Test-Aufgabe 2: Geldspur verfolgen. (Befehl selber schreiben)\n" +
                                "> Der „Schatten“ hat besonders lukrative Coups geplant.\n\n" +
                                "Ihre Aufgabe: Listen Sie alle Fälle auf, bei denen der Schaden über 10.000 beträgt, sortiert nach Schadenshöhe absteigend.",
                        "SELECT tatort, schadenssumme FROM fall WHERE schadenssummme > 10000 ORDER BY schadenssumme DESC",
                        SqlKategorie.ORDER_BY,
                        Schwierigkeit.MEDIUM,
                        "Verwende WHERE mit schadenssummme > 10000 und ORDER BY schadenssumme DESC.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "TEST006",
                        "Test-Aufgabe 3: Verbindungen aufdecken. (Befehl selber schreiben)\n" +
                                "> Einer der entscheidenden Hinweise kam von Zeugen, die aus demselben Viertel wie Verdächtige stammen.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle unterschiedlichen Wohnorte, die sowohl Zeugen als auch Verdächtige gemeinsam haben.",
                        "SELECT DISTINCT z.wohnort FROM zeuge z INNER JOIN verdaechtiger v ON z.wohnort = v.wohnort",
                        SqlKategorie.JOIN,
                        Schwierigkeit.MEDIUM,
                        "Verwende INNER JOIN mit DISTINCT.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        10
                )
        );
        for (Task t : mediumTasks) {
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
