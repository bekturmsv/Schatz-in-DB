package pti.softwareentwicklg.SchatzInDb.utils.initializer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pti.softwareentwicklg.SchatzInDb.repository.task.IncorrectSqlExampleRepository;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;

import java.util.Map;

import static java.util.Map.entry;

@Component
public class IncorrectSqlInitializer {

    private final TaskRepository taskRepository;

    private final IncorrectSqlExampleRepository incorrectSqlExampleRepository;
    private final JdbcTemplate jdbcTemplate;

    public IncorrectSqlInitializer(TaskRepository taskRepository,IncorrectSqlExampleRepository incorrectSqlExampleRepository,
                               @Qualifier("taskJdbcTemplate")
                               JdbcTemplate jdbcTemplate)  {
        this.taskRepository = taskRepository;
        this.incorrectSqlExampleRepository = incorrectSqlExampleRepository;
        this.jdbcTemplate=jdbcTemplate;

    }


    @EventListener(ApplicationReadyEvent.class)
    public void loadIncorrectSql() {
        Map<String, String> wrongSqlByTaskCode = Map.ofEntries(
                entry("T003", "SELECT fall FROM tatort"),
                entry("T005", "SELECT name , schuhgroesse FROM verdaechtiger WHERE 44"),
                entry("T009", "SELECT zeuge, name FROM fall_zeuge WHERE;"),
                entry("T011", "SELECT name FROM verdaechtiger WHERE haarfarbe AND geschlecht = 'braun' , 'm'"),
                entry("T016", "SELECT (*) COUNT FROM verdaechtiger"),
                entry("T019", "SELECT wohnort FROM verdaechtiger COUNT(*) GROUP BY wohnort"),
                entry("T025", "SELECT verdaechtiger.name, fall_verdaechtiger.fall_id FROM fall_verdaechtiger INNER JOIN verdaechtiger ON verdaechtiger.id = fall_verdaechtiger.verdaechtiger_id"),
                entry("T027", "SELECT zeuge.name, fall_zeuge.fall_id FROM zeuge LEFT JOIN fall_zeuge ON fall_zeuge.id = zeuge.id"),
                entry("T030", "SELECT name WHERE haarfarbe = 'braun' FROM verdaechtiger"),
                entry("T034", "SELECT name ORDER BY ASC name FROM verdaechtiger"),
                entry("T039", "SELECT alter (AVG) FROM verdaechtiger"),
                entry("T040", "SELECT fall , COUNT FROM ermittler GROUP BY fall"),
                entry("T043", "SELECT wohnort FROM verdaechtiger , COUNT(*) GROUP BY wohnort HAVING COUNT >3"),
                entry("T045", "SELECT f.kennzeichen LEFT JOIN FROM fahrzeug fall_fahrzeug ff  ON f.id = ff-fahrzeug_id IS NULL WHERE ff.fall_id"),
                entry("T047", "SELECT verdaechtiger.name, fall.tatort FROM fall_verdaechtiger fv INNER JOIN verdaechtiger ON verdaechtiger.id = fv.verdaechtiger_id INNER JOIN fall ON fv.fall_id = fall.id"),
                entry("T049", "SELECT verdaechtiger.name FROM fall_verdaechtiger fv LEFT JOIN verdaechtiger ON verdaechtiger.id = fv.verdaechtiger_id WHERE fv.fall_id IS NULL"),
                entry("T053", "SELECT name FROM verdaechtiger WHERE Vorstrafen > 2 ORDER BY vorstrafen ASC")
        );

        wrongSqlByTaskCode.forEach((code, wrongSql) -> {
            taskRepository.findByTaskCode(code).ifPresent(task -> {
                Long taskId = task.getId();
                Integer count = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM incorrect_sql_example WHERE task_id = ?",
                        Integer.class,
                        taskId
                );
                if (count != null && count == 0) {
                    jdbcTemplate.update(
                            "INSERT INTO incorrect_sql_example (task_id, wrong_query) VALUES (?, ?)",
                            taskId, wrongSql
                    );
                }
            });
        });
    }
}
