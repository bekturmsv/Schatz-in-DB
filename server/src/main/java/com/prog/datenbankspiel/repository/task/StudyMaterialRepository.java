package com.prog.datenbankspiel.repository.task;

import com.prog.datenbankspiel.model.task.StudyMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {
    List<StudyMaterial> findByTeacherId(Long teacherId);
}
