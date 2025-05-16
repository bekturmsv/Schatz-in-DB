package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCode(String code);
    Optional<Group> findByCode(String code);
}
