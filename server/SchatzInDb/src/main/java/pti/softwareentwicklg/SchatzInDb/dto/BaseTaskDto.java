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
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseTaskDto {
    private Long id;
    private String taskCode;
    private String aufgabe;
    private SqlKategorie kategorie;
    private Schwierigkeit schwierigkeitsgrad;
    private String hint;
    private TaskType taskType;
    private TaskInteractionType taskInteractionType;
    private boolean solved;
    private String tableName;
    private List<Map<String, Object>> tableData;
}
