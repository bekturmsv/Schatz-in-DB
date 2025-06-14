package pti.softwareentwicklg.SchatzInDb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskInteractionType;
import pti.softwareentwicklg.SchatzInDb.model.enums.TaskType;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class IncorrectSqlTaskDto extends BaseTaskDto {
    private String wrongQuery;

    public IncorrectSqlTaskDto(Long id, String taskCode, String aufgabe, SqlKategorie kategorie, Schwierigkeit schwierigkeitsgrad, String hint, TaskType taskType, TaskInteractionType taskInteractionType, boolean solved, String tableName, List<Map<String, Object>> tableData, String wrongQuery) {
        super(id, taskCode, aufgabe, kategorie, schwierigkeitsgrad, hint, taskType, taskInteractionType, solved, tableName, tableData);
        this.wrongQuery = wrongQuery;
    }
}
