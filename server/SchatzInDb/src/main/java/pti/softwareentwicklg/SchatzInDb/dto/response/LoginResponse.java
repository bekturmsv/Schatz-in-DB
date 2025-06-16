package pti.softwareentwicklg.SchatzInDb.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Object user;
}