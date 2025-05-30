package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Group;
import com.prog.datenbankspiel.model.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCode(String code);
    Optional<Group> findByCode(String code);

    Set<Group> findAllByTeacher(Teacher teacher);
}
