package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Optional<Theme> findByName(String name);
    boolean existsByName(String name);
}
