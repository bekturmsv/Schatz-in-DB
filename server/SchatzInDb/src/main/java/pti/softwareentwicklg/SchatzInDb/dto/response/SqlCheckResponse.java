package pti.softwareentwicklg.SchatzInDb.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlCheckResponse {
    private boolean correct;
    private String errorMessage;

    public SqlCheckResponse(boolean correct, String errorMessage) {
        this.correct = correct;
        this.errorMessage = errorMessage;
    }
}