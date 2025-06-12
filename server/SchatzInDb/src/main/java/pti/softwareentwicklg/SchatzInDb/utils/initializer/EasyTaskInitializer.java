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
public class EasyTaskInitializer {

    private final TaskRepository taskRepository;


    public EasyTaskInitializer(TaskRepository taskRepository)  {
        this.taskRepository = taskRepository;
    }

    @PostConstruct
    public void initEasyTasks() {

        List<Task> easyTasks = List.of(
                createTask(
                        "T001",
                        "Alle Verdächtigen auflisten\n" +
                                "> Wer sind die Menschen, die in den letzten Wochen durch auffälliges Verhalten aufgefallen sind? " +
                                "Ein Detektiv muss wissen, mit wem er es zu tun hat. Verschaffen Sie sich einen ersten Überblick über alle registrierten Verdächtigen.",
                        "SELECT name FROM verdaechtiger",
                        SqlKategorie.SELECT,
                        Schwierigkeit.EASY,
                        "Drag & Drop Blöcke: SELECT ; name ; FROM ; verdaechtiger",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        10
                ),
                createTask(
                        "T002",
                        "Namen und Geschlecht der Verdächtigen\n" +
                                "> Gesichter sind vergänglich, doch Statistiken lügen nicht. Vielleicht gibt es unter den Verdächtigen ein Muster? " +
                                "Erweitern Sie die Liste um das Geschlecht und überprüfen Sie, ob sich daraus erste Auffälligkeiten ergeben.",
                        "SELECT name, geschlecht FROM verdaechtiger",
                        SqlKategorie.SELECT,
                        Schwierigkeit.EASY,
                        "Verwende SELECT mit mehreren Spalten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "T003",
                        "Tatorte auflisten\n" +
                                "> Tatorte sind nicht nur Orte – sie sind Geschichten, die darauf warten, erzählt zu werden. " +
                                "Vielleicht liegt der erste Hinweis nicht bei den Menschen, sondern bei den Plätzen, an denen sie zugeschlagen haben. " +
                                "Listen Sie alle Tatorte auf.",
                        "SELECT tatort FROM fall",
                        SqlKategorie.SELECT,
                        Schwierigkeit.EASY,
                        "Fehlerbefehl korrigieren: SELECT tatort FROM fall",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        10
                ),
                createTask(
                        "T004",
                        "Verdächtige mit braunen Haaren\n" +
                                "> Während Sie die Berichte durchgehen, erreicht Sie eine Nachricht vom Spurensicherungsteam: " +
                                "Am letzten Tatort wurde eine braune Haarsträhne sichergestellt. Ihre Aufgabe: Finden Sie alle Verdächtigen mit braunen Haaren.",
                        "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.EASY,
                        "Drag & Drop Blöcke: SELECT name ; FROM ; verdaechtiger ; WHERE ; haarfarbe = 'braun'",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        10
                ),
                createTask(
                        "T005",
                        "Verdächtige mit Schuhgröße 44\n" +
                                "> Die Spurensicherung legt nach: Ein klarer Abdruck, Schuhgröße 44, frisch im Schlamm gefunden. " +
                                "Eine Seltenheit in Table-Town. Wer könnte darauf passen? Finden Sie es heraus.",
                        "SELECT name FROM verdaechtiger WHERE schuhgroesse = 44",
                        SqlKategorie.WHERE,
                        Schwierigkeit.EASY,
                        "Fehlerbefehl korrigieren: SELECT name FROM verdaechtiger WHERE schuhgroesse = 44",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        10
                ),
                createTask(
                        "T006",
                        "Männliche Verdächtige\n" +
                                "> Eine Zeugin erinnert sich: \"Er war groß, schlank – und definitiv ein Mann.\" Ein wichtiges Detail. Filtern Sie alle männlichen Verdächtigen heraus und bringen Sie sich einen Schritt näher an die Wahrheit.\n\n" +
                                "Ihre Aufgabe: Filtern Sie alle männlichen Verdächtigen heraus.",
                        "SELECT name FROM verdaechtiger WHERE geschlecht = 'm'",
                        SqlKategorie.WHERE,
                        Schwierigkeit.EASY,
                        "Verwende WHERE mit geschlecht.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),

                createTask(
                        "T007",
                        "Zeugen über 40\n" +
                                "> Erfahrung bringt Klarheit. Ältere Zeugen erinnern sich oft besser an Details. Finden Sie alle Zeugen, die älter als 40 Jahre sind. Vielleicht verbirgt sich in ihren Aussagen ein Schlüssel zum Fall.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Zeugen, die älter als 40 Jahre sind.",
                        "SELECT name FROM zeuge WHERE alter > 40",
                        SqlKategorie.WHERE,
                        Schwierigkeit.EASY,
                        "Verwende WHERE mit Vergleichsoperator >.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),

                createTask(
                        "T008",
                        "Verdächtige nach Alter sortieren\n" +
                                "> In der Kriminologie gibt es Altersschwerpunkte. Vielleicht hilft es, die Verdächtigen nach Alter zu ordnen, um weitere Muster zu erkennen. Wer ist der Jüngste? Wer der Älteste?\n\n" +
                                "Ihre Aufgabe: Sortieren Sie die Verdächtigen nach Alter und zeigen Sie Name sowie Alter an.",
                        "SELECT name, alter FROM verdaechtiger ORDER BY alter",
                        SqlKategorie.ORDER_BY,
                        Schwierigkeit.EASY,
                        "Verwende ORDER BY, um die Ergebnisse zu sortieren.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        10
                ),
                createTask(
                        "T009",
                        "Zeugen alphabetisch sortieren\n" +
                                "> Ordnung ist der erste Schritt zur Wahrheit. Listen Sie alle Zeugen alphabetisch auf – ein klarer Blick kann helfen, wenn die Hinweise anfangen, sich zu überlagern.\n\n" +
                                "Ihre Aufgabe: Listen Sie alle Zeugen alphabetisch auf.",
                        "SELECT name FROM zeuge ORDER BY name ASC",
                        SqlKategorie.ORDER_BY,
                        Schwierigkeit.EASY,
                        "Fehlerbefehl korrigieren: richtige Reihenfolge von SELECT, FROM und ORDER BY beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        10
                ),
                createTask(
                        "T010",
                        "Fälle nach Schadenshöhe sortieren (ORDER BY) (Befehl selber schreiben)\n" +
                                "> Wo schlug der Täter am härtesten zu? Sortieren Sie alle Fälle nach Schadenssumme, absteigend. Vielleicht ist die Größe der Beute ein Hinweis auf den Ehrgeiz des Täters.\n\n" +
                                "Ihre Aufgabe: Sortieren Sie alle Fälle nach Schadenssumme absteigend.",
                        "SELECT tatort, schadenssumme FROM fall ORDER BY schadenssumme DESC",
                        SqlKategorie.ORDER_BY,
                        Schwierigkeit.EASY,
                        "Verwende ORDER BY mit DESC.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "T011",
                        "Braune Haare und männlich\n" +
                                "> Die bisherigen Hinweise verdichten sich: Der Täter ist männlich und hat braune Haare. Finden Sie alle Verdächtigen, die beide Merkmale teilen.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Verdächtigen, die braune Haare haben und männlich sind.",
                        "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun' AND geschlecht = 'm'",
                        SqlKategorie.AND,
                        Schwierigkeit.EASY,
                        "Fehlerbefehl korrigieren: Bedingungen korrekt mit AND verknüpfen.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        10
                ),
                createTask(
                        "T012",
                        "Braune oder schwarze Haare\n" +
                                "> Manche Zeugenberichte weichen ab – vielleicht sind es braune oder schwarze Haare? Erweitern Sie die Suche.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Verdächtigen mit braunen oder schwarzen Haaren.",
                        "SELECT name FROM verdaechtiger WHERE haarfarbe = 'braun' OR haarfarbe = 'schwarz'",
                        SqlKategorie.OR,
                        Schwierigkeit.EASY,
                        "Verwende WHERE mit OR.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "T013",
                        "Verdächtige unter 70\n" +
                                "> Die körperliche Fitness spielt eine Rolle. Schließen Sie alle aus, die älter als 70 Jahre sind.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Verdächtigen, die jünger als 70 Jahre sind.",
                        "SELECT name, alter FROM verdaechtiger WHERE NOT alter >= 70",
                        SqlKategorie.NOT,
                        Schwierigkeit.EASY,
                        "Verwende WHERE mit NOT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "T014",
                        "Wohnorte der Verdächtigen\n" +
                                "> Vielleicht ist der Täter ortstreu? Finden Sie heraus, aus welchen Wohnorten die Verdächtigen stammen.\n\n" +
                                "Ihre Aufgabe: Listen Sie alle unterschiedlichen Wohnorte der Verdächtigen auf.",
                        "SELECT DISTINCT wohnort FROM verdaechtiger",
                        SqlKategorie.DISTINCT,
                        Schwierigkeit.EASY,
                        "Verwende DISTINCT, um doppelte Wohnorte herauszufiltern.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        10
                ),
                createTask(
                        "T015",
                        "Haarfarben der Verdächtigen\n" +
                                "> Gibt es weitere Haarfarbenmuster? Listen Sie alle unterschiedlichen Haarfarben auf.\n\n" +
                                "Ihre Aufgabe: Listen Sie alle unterschiedlichen Haarfarben der Verdächtigen auf.",
                        "SELECT DISTINCT haarfarbe FROM verdaechtiger",
                        SqlKategorie.DISTINCT,
                        Schwierigkeit.EASY,
                        "Verwende DISTINCT, um doppelte Haarfarben herauszufiltern.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),

                createTask(
                        "T016",
                        "Anzahl der Verdächtigen\n" +
                                "> Wie viele Verdächtige haben wir insgesamt? Eine Übersicht hilft bei der weiteren Eingrenzung.\n\n" +
                                "Ihre Aufgabe: Ermitteln Sie die Gesamtzahl der Verdächtigen.",
                        "SELECT COUNT(*) FROM verdaechtiger",
                        SqlKategorie.COUNT,
                        Schwierigkeit.EASY,
                        "Fehlerbefehl korrigieren: richtige Reihenfolge von COUNT und FROM beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        10
                ),
                createTask(
                        "T017",
                        "Gestohlene Gegenstände\n" +
                                "> Wie viele Gegenstände wurden insgesamt entwendet? Vielleicht hilft der Umfang der Beute bei der Täterprofilierung.\n\n" +
                                "Ihre Aufgabe: Ermitteln Sie die Gesamtzahl entwendeter Gegenstände.",
                        "SELECT SUM(anzahl_gegenstaende) FROM fall",
                        SqlKategorie.SUM,
                        Schwierigkeit.EASY,
                        "Verwende SUM, um die Gesamtanzahl zu berechnen.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "T018",
                        "Durchschnittsalter der Verdächtigen\n" +
                                "> Berechnen Sie das Durchschnittsalter. Der Täter ist wahrscheinlich keine Ausnahme.\n\n" +
                                "Ihre Aufgabe: Ermitteln Sie das durchschnittliche Alter aller Verdächtigen.",
                        "SELECT AVG(alter) FROM verdaechtiger",
                        SqlKategorie.AVG,
                        Schwierigkeit.EASY,
                        "Verwende AVG, um den Mittelwert zu berechnen.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        10
                ),
                createTask(
                        "T019",
                        "Verdächtige nach Wohnort\n" +
                                "> In welchen Stadtteilen tummeln sich die meisten Verdächtigen? Gruppieren Sie nach Wohnort.\n\n" +
                                "Ihre Aufgabe: Zählen Sie die Verdächtigen pro Wohnort.",
                        "SELECT wohnort, COUNT(*) FROM verdaechtiger GROUP BY wohnort",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.EASY,
                        "Fehlerbefehl korrigieren: richtige Reihenfolge von SELECT, COUNT und GROUP BY beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        10
                ),
                createTask(
                        "T020",
                        "Verdächtige nach Haarfarbe\n" +
                                "> Gibt es eine Häufung bestimmter Haarfarben? Gruppieren Sie nach Haarfarbe.\n\n" +
                                "Ihre Aufgabe: Zählen Sie die Verdächtigen pro Haarfarbe.",
                        "SELECT haarfarbe, COUNT(*) FROM verdaechtiger GROUP BY haarfarbe",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.EASY,
                        "Verwende GROUP BY zusammen mit COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "T021",
                        "Zeugen nach Alter\n" +
                                "> Gruppieren Sie die Zeugen nach Alter, um einen Überblick zu gewinnen.\n\n" +
                                "Ihre Aufgabe: Zählen Sie die Zeugen pro Altersgruppe.",
                        "SELECT alter, COUNT(*) FROM zeuge GROUP BY alter",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.EASY,
                        "Verwende GROUP BY zusammen mit COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "T022",
                        "Wohnorte mit mind. 3 Verdächtigen\n" +
                                "> Konzentrieren Sie sich auf Hotspots: Wohnorte mit mindestens drei Verdächtigen.\n\n" +
                                "Ihre Aufgabe: Finden Sie Wohnorte mit mindestens drei Verdächtigen.",
                        "SELECT wohnort, COUNT(*) FROM verdaechtiger GROUP BY wohnort HAVING COUNT(*) >= 3",
                        SqlKategorie.HAVING,
                        Schwierigkeit.EASY,
                        "Verwende HAVING mit COUNT, um Mindestanzahl zu filtern.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        10
                ),
                createTask(
                        "T023",
                        "Häufige Haarfarben\n" +
                                "> Listen Sie Haarfarben, die mindestens zweimal vorkommen. Muster könnten sich herauskristallisieren.\n\n" +
                                "Ihre Aufgabe: Finden Sie Haarfarben mit mindestens zwei Verdächtigen.",
                        "SELECT haarfarbe, COUNT(*) FROM verdaechtiger GROUP BY haarfarbe HAVING COUNT(*) >= 2",
                        SqlKategorie.HAVING,
                        Schwierigkeit.EASY,
                        "Verwende HAVING zusammen mit COUNT.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "T024",
                        "Zeugen und ihr Fall\n" +
                                "> Finden Sie die Namen der Zeugen und die Nummer der Fälle, bei denen Sie ausgesagt haben. Beschränken Sie sich auf die wichtigsten Informationen.\n\n" +
                                "Ihre Aufgabe: Verknüpfen Sie Zeugen mit den zugehörigen Fällen.",
                        "SELECT zeuge.name, fall_zeuge.fall_id FROM zeuge INNER JOIN fall_zeuge ON zeuge.id = fall_zeuge.zeuge_id",
                        SqlKategorie.JOIN,
                        Schwierigkeit.EASY,
                        "Verwende INNER JOIN mit ON.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        10
                ),
                createTask(
                        "T025",
                        "Verdächtige und ihr Fall\n" +
                                "> Finden Sie die Namen der Verdächtigen und den jeweiligen Fall, dem sie zugeordnet wurden. Nur zwei Tabellen werden benötigt.\n\n" +
                                "Ihre Aufgabe: Verknüpfen Sie Verdächtige mit den zugehörigen Fällen.",
                        "SELECT verdaechtiger.name, fall_verdaechtiger.fall_id FROM verdaechtiger INNER JOIN fall_verdaechtiger ON verdaechtiger.id = fall_verdaechtiger.verdaechtiger_id",
                        SqlKategorie.JOIN,
                        Schwierigkeit.EASY,
                        "Fehlerbefehl korrigieren: richtige Tabellenreihenfolge und ON-Klausel beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        10
                ),
                createTask(
                        "T026",
                        "Alle Verdächtigen und zugeordnete Fälle\n" +
                                "> Finden Sie alle Verdächtigen und die ihnen zugeordneten Fälle. Auch Verdächtige, die noch keinem Fall zugeordnet sind, sollen angezeigt werden.\n\n" +
                                "Ihre Aufgabe: Verwenden Sie LEFT JOIN, um alle Verdächtigen mit ihren Fällen zu listen.",
                        "SELECT verdaechtiger.name, fall_verdaechtiger.fall_id FROM verdaechtiger LEFT JOIN fall_verdaechtiger ON verdaechtiger.id = fall_verdaechtiger.verdaechtiger_id",
                        SqlKategorie.JOIN,
                        Schwierigkeit.EASY,
                        "Verwende LEFT JOIN mit ON.",
                        TaskType.REGULAR,
                        TaskInteractionType.DRAG_AND_DROP,
                        10
                ),
                createTask(
                        "T027",
                        "Alle Zeugen und zugeordnete Fälle\n" +
                                "> Finden Sie alle Zeugen und die Fälle, zu denen sie Aussagen gemacht haben. Auch Zeugen ohne Fall sollen angezeigt werden.\n\n" +
                                "Ihre Aufgabe: Verwenden Sie LEFT JOIN, um alle Zeugen mit ihren Fällen zu listen.",
                        "SELECT zeuge.name, fall_zeuge.fall_id FROM zeuge LEFT JOIN fall_zeuge ON zeuge.id = fall_zeuge.zeuge_id",
                        SqlKategorie.JOIN,
                        Schwierigkeit.EASY,
                        "Fehlerbefehl korrigieren: richtige ON-Klausel bei LEFT JOIN beachten.",
                        TaskType.REGULAR,
                        TaskInteractionType.SQL_ERROR_CORRECTION,
                        10
                ),
                createTask(
                        "TEST001",
                        "Test-Aufgabe 1: Letzte Spur: Schuhgröße 44.\n" +
                                "> Max Berger hatte Schuhgröße 44 – vielleicht gibt es noch andere Verdächtige mit derselben Schuhgröße, die übersehen wurden.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Verdächtigen mit Schuhgröße 44.",
                        "SELECT name FROM verdaechtiger WHERE schuhgroesse = 44",
                        SqlKategorie.WHERE,
                        Schwierigkeit.EASY,
                        "Verwende WHERE mit schuhgroesse = 44.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "TEST002",
                        "Test-Aufgabe 2: Wohnort-Hotspots. (Befehle selber schreiben)\n" +
                                "> Max Berger stammte aus einem Viertel mit hoher Kriminalitätsrate.\n\n" +
                                "Ihre Aufgabe: Finden Sie alle Wohnorte, aus denen mindestens drei Verdächtige stammen.",
                        "SELECT wohnort, COUNT(*) FROM verdaechtiger GROUP BY wohnort HAVING COUNT(*) >= 3",
                        SqlKategorie.GROUP_BY,
                        Schwierigkeit.EASY,
                        "Verwende GROUP BY mit HAVING COUNT(*) >= 3.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        10
                ),
                createTask(
                        "TEST003",
                        "Test-Aufgabe 3: Tatorte im Blick. (Befehle selber schreiben)\n" +
                                "> Max Berger wurde an mehreren Orten gesehen – manche Tatorte fallen durch besonders hohe Schadenssummen auf.\n\n" +
                                "Ihre Aufgabe: Listen Sie alle Tatorte mit einer Schadenssumme größer als 5 000.",
                        "SELECT tatort FROM fall WHERE schadenssumme > 5000",
                        SqlKategorie.WHERE,
                        Schwierigkeit.EASY,
                        "Verwende WHERE mit schadenssumme > 5000.",
                        TaskType.TEST,
                        TaskInteractionType.SQL_INPUT,
                        10
                )

        );

        for (Task t : easyTasks) {
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
