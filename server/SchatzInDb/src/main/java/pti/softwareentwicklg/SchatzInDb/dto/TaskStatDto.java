package pti.softwareentwicklg.SchatzInDb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatDto {
    private Schwierigkeit type;
    private SqlKategorie theme;
    private String taskCode;
}