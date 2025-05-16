package com.prog.datenbankspiel.dto.profile;

import lombok.Data;

import java.util.List;

@Data
public class AdminUpdateTeacherDto {
    private String firstName;
    private String lastName;
    private String subject;
    private List<Long> groupIds;
}
