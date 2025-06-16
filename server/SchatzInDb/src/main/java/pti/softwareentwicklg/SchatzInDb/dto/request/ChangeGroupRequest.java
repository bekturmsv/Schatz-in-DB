package pti.softwareentwicklg.SchatzInDb.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeGroupRequest {

    @NotBlank
    private String groupCode;
}