package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.dto.GroupDTO;
import com.prog.datenbankspiel.dto.TeacherDTO;
import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.model.user.Teacher;
import com.prog.datenbankspiel.model.user.enums.Roles;
import com.prog.datenbankspiel.repository.user.GroupRepository;
import com.prog.datenbankspiel.repository.user.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;

    // methods creates teacher account
    public TeacherDTO createTeacher(TeacherDTO teacherCreateDTO){
        Teacher teacher = new Teacher();
        teacher.setRole(Roles.TEACHER);
        teacher.setEmail(teacherCreateDTO.getEmail());
        teacher.setPassword(teacherCreateDTO.getPassword());
        teacher.setUsername(teacherCreateDTO.getUsername());
        teacher.setFirst_name(teacherCreateDTO.getFirst_name());
        teacher.setLast_name(teacherCreateDTO.getLast_name());
        teacher.setSubject(teacherCreateDTO.getSubject());
        teacher = teacherRepository.save(teacher);
        return mapToTeacherDto(teacher);
    }

    // method creates study groups
    @Override
    public GroupDTO createGroup(GroupDTO groupCreateDTO) {
        Group group = new Group();
        group.setName(groupCreateDTO.getName());
        group = groupRepository.save(group);
        return mapToGroupDto(group);
    }

    // maps Teacher entity to TeacherDto
    private TeacherDTO mapToTeacherDto(Teacher t) {
        return TeacherDTO.builder()

                .username(t.getUsername())
                .email(t.getEmail())
                .first_name(t.getFirst_name())
                .last_name(t.getLast_name())
                .password(t.getPassword())
                .subject(t.getSubject())
                .build();
    }

    // maps Teacher entity to TeacherDto
    private GroupDTO mapToGroupDto(Group group) {
        return GroupDTO.builder()

                .name(group.getName())
                .build();
    }
}
