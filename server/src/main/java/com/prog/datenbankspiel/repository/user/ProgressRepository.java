package com.prog.datenbankspiel.repository.user;

import com.prog.datenbankspiel.model.user.Progress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProgressRepository extends JpaRepository<Progress, Long>  {

    Progress findByUserId(Long playerId);

    boolean existsByUser_IdAndCompletedTaskIdsContains(Long userId, Long taskId);
}
