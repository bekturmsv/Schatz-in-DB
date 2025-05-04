package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCode(String code);
}
