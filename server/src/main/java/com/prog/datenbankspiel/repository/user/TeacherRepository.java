package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
