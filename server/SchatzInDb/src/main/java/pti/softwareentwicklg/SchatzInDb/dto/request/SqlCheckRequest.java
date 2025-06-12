package pti.softwareentwicklg.SchatzInDb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlCheckRequest {
    private String userSql;
    private String taskCode;
}