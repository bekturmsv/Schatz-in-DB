package pti.softwareentwicklg.SchatzInDb.dto.response;

import lombok.Data;

@Data
public class PlayerRegisterResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String nickname;
    private String role;
}