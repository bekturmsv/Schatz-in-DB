package pti.softwareentwicklg.SchatzInDb.dto;

import lombok.Data;

@Data
public class PlayerProfileUpdateDto {
    private String firstName;
    private String lastName;
    private String newPassword;
    private String username;
    private String email;
}
