package pti.softwareentwicklg.SchatzInDb.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SqlCheckResponse {
    private boolean correct;
    private String errorMessage;
    private List<Map<String, Object>> userResult;

    public SqlCheckResponse(boolean correct, String errorMessage) {
        this.correct = correct;
        this.errorMessage = errorMessage;
    }

    public SqlCheckResponse(boolean correct, String errorMessage, List<Map<String, Object>> userResult) {
        this.correct = correct;
        this.errorMessage = errorMessage;
        this.userResult = userResult;
    }
}