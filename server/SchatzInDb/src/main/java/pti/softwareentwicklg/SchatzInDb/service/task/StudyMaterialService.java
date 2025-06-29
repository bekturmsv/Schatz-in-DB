package pti.softwareentwicklg.SchatzInDb.service.task;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.task.StudyMaterial;
import pti.softwareentwicklg.SchatzInDb.repository.task.StudyMaterialRepository;
import pti.softwareentwicklg.SchatzInDb.repository.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyMaterialService {

    private final StudyMaterialRepository materialRepo;
    private final UserRepository userRepo;

    public StudyMaterial createMaterial(StudyMaterial material, Long teacherId) {
        userRepo.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        material.setTeacher(teacherId);
        return materialRepo.save(material);
    }

    public List<StudyMaterial> getAllMaterials() {
        return materialRepo.findAll();
    }

    public List<StudyMaterial> getMaterialsByTeacher(Long teacherId) {
        return materialRepo.findByTeacher(teacherId);
    }

    public StudyMaterial getMaterialById(Long id) {
        return materialRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Material " + id + " not found"));
    }

    public void deleteMaterial(Long id) {
        materialRepo.deleteById(id);
    }

    public void uploadMaterial(String title, String rawDescription, SqlKategorie category, Long teacherId) {

        StudyMaterial material = new StudyMaterial();
        material.setTitle(title);
        material.setDescription(rawDescription);
        material.setSqlKategorie(category);
        material.setTeacher(teacherId);

        materialRepo.save(material);
    }
}