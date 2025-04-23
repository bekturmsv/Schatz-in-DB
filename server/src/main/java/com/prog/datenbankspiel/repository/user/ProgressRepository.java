package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Progress;
import com.prog.datenbankspiel.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long>  {
}
