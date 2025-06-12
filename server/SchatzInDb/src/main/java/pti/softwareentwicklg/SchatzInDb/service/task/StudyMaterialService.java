package pti.softwareentwicklg.SchatzInDb.service.task;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pti.softwareentwicklg.SchatzInDb.model.task.StudyMaterial;
import pti.softwareentwicklg.SchatzInDb.repository.task.StudyMaterialRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Material " + id + " not found"));
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
