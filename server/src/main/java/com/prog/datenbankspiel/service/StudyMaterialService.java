package com.prog.datenbankspiel.service;

import com.prog.datenbankspiel.model.task.StudyMaterial;
import com.prog.datenbankspiel.repository.task.StudyMaterialRepository;
import com.prog.datenbankspiel.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyMaterialService {

    private final StudyMaterialRepository materialRepo;
    private final UserRepository userRepo;

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public StudyMaterial createMaterial(StudyMaterial material, Long teacherId) {
        userRepo.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        material.setTeacherId(teacherId);
        return materialRepo.save(material);
    }

    @PreAuthorize("hasAnyRole('PLAYER', 'TEACHER', 'ADMIN')")
    public List<StudyMaterial> getAllMaterials() {
        return materialRepo.findAll();
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public List<StudyMaterial> getMaterialsByTeacher(Long teacherId) {
        return materialRepo.findByTeacherId(teacherId);
    }

    @PreAuthorize("hasAnyRole('PLAYER', 'TEACHER', 'ADMIN')")
    public StudyMaterial getMaterialById(Long id) {
        return materialRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found"));
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public StudyMaterial updateMaterial(Long id, StudyMaterial updatedMaterial) {
        StudyMaterial existing = getMaterialById(id);
        existing.setDescription(updatedMaterial.getDescription());
        existing.setType(updatedMaterial.getType());
        existing.setFilePath(updatedMaterial.getFilePath());
        existing.setVideoUrl(updatedMaterial.getVideoUrl());
        existing.setExternalLink(updatedMaterial.getExternalLink());
        return materialRepo.save(existing);
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public void deleteMaterial(Long id) {
        materialRepo.deleteById(id);
    }
}
