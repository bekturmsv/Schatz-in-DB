package pti.softwareentwicklg.SchatzInDb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupJoinResponse {
    private String groupName;
    private Object user;
}
