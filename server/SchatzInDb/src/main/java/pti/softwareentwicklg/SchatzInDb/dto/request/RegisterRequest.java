package pti.softwareentwicklg.SchatzInDb.dto.request;

import lombok.Data;
import pti.softwareentwicklg.SchatzInDb.model.enums.SpecialistGroup;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Integer matriculationNumber;
    private SpecialistGroup specialistGroup;
}