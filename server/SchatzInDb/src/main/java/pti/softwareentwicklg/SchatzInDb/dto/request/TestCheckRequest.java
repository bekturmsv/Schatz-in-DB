package pti.softwareentwicklg.SchatzInDb.dto.request;

import lombok.Getter;
import lombok.Setter;
import pti.softwareentwicklg.SchatzInDb.model.enums.Schwierigkeit;

import java.time.Duration;

@Getter
@Setter
public class TestCheckRequest {
    private Schwierigkeit schwierigkeit;
    private int spentTimeInSeconds;
}
