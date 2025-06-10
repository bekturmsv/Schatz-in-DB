package pti.softwareentwicklg.SchatzInDb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AdminUpdateTeacherDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String subject;

    @NotEmpty
    private List<Long> groupIds;
}