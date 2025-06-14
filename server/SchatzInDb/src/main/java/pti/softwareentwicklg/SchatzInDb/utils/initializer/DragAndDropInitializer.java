package pti.softwareentwicklg.SchatzInDb.utils.initializer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pti.softwareentwicklg.SchatzInDb.repository.task.TaskRepository;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Component
public class DragAndDropInitializer {

    private final TaskRepository taskRepository;
    private final JdbcTemplate jdbcTemplate;

    public DragAndDropInitializer(TaskRepository taskRepository,
                                  @Qualifier("taskJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.taskRepository = taskRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadDragAndDropExamples() {
        Map<String, List<String>> blocksByTaskCode = Map.ofEntries(
                // EASY
                entry("T001", List.of("FROM", "SELECT", "name", "verdaechtiger")),
                entry("T004", List.of("=", "'braun'", "haarfarbe", "SELECT", "FROM", "name", "WHERE", "verdaechtiger")),
                entry("T008", List.of("FROM", "verdaechtiger", "ORDER BY", "alter", "SELECT", "name", "alter")),
                entry("T014", List.of("wohnort", "SELECT", "DISTINCT", "verdaechtiger", "FROM")),
                entry("T018", List.of("AVG(alter)", "verdaechtiger", "FROM", "SELECT")),
                entry("T022", List.of("SELECT", "COUNT()", "HAVING", "FROM", "wohnort", "verdaechtiger", ">", "GROUP BY", "COUNT(*)", "3", "wohnort,")),
                entry("T024", List.of("fall_zeuge", "INNER JOIN", "=", "zeuge.name,", "ON", "zeuge.id", "fall_zeuge.zeuge_id", "FROM", "SELECT", "fall_zeuge.fall_id", "zeuge")),
                entry("T026", List.of("SELECT", "verdaechtiger", "=", "LEFT JOIN", "verdaechtiger.name", "ON", "fall_verdaechtiger", "FROM", "fall_verdaechtiger.fall_id", "verdaechtiger.id", "fall_verdaechtiger.verdaechtiger_id")),

                // MEDIUM
                entry("T032", List.of("haarfarbe = 'schwarz'", "FROM", "verdaechtiger", "haarfarbe = 'braun'", "SELECT", "OR", "WHERE", "name")),
                entry("T036", List.of("SELECT", "wohnort", "FROM", "DISTINCT", "verdaechtiger")),
                entry("T044", List.of("SELECT name", "IN", "verdaechtiger", "FROM", "WHERE wohnort", "(SELECT wohnort", "FROM verdaechtiger", "GROUP BY wohnort", "HAVING COUNT(*) > 3", ")")),
                entry("T046", List.of("ON zeuge.id = fz.zeuge_id", "INNER JOIN fall", "fall.tatort", "INNER JOIN fall_zeuge fz", "FROM zeuge", "ON fz.fall_id = fall.id", "SELECT zeuge.name,")),
                entry("T051", List.of("LEFT JOIN", "IS NULL", "fall_zeuge fz ON", "SELECT fall.tatort", "fall.id = fz.fall_id", "WHERE fz.zeuge_id", "FROM fall"))
        );

        blocksByTaskCode.forEach((code, blocks) -> {
            taskRepository.findByTaskCode(code).ifPresent(task -> {
                Long taskId = task.getId();

                Integer count = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM drag_and_drop_example WHERE task_id = ?",
                        Integer.class,
                        taskId
                );

                if (count != null && count == 0) {
                    jdbcTemplate.update(
                            "INSERT INTO drag_and_drop_example (task_id, drag_and_drop_query) VALUES (?, to_jsonb(?::json))",
                            taskId,
                            toJsonArray(blocks)
                    );
                }
            });
        });
    }

    private String toJsonArray(List<String> blocks) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < blocks.size(); i++) {
            sb.append("\"").append(blocks.get(i).replace("\"", "\\\"")).append("\"");
            if (i < blocks.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
