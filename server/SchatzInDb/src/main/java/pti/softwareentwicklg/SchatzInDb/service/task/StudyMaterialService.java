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

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public StudyMaterial createMaterial(StudyMaterial material, Long teacherId) {
        userRepo.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        material.setTeacher(teacherId);
        return materialRepo.save(material);
    }

    @PreAuthorize("hasAnyRole('PLAYER', 'TEACHER', 'ADMIN')")
    public List<StudyMaterial> getAllMaterials() {
        return materialRepo.findAll();
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public List<StudyMaterial> getMaterialsByTeacher(Long teacherId) {
        return materialRepo.findByTeacher(teacherId);
    }

    @PreAuthorize("hasAnyRole('PLAYER', 'TEACHER', 'ADMIN')")
    public StudyMaterial getMaterialById(Long id) {
        return materialRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Material " + id + " not found"));
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public void deleteMaterial(Long id) {
        materialRepo.deleteById(id);
    }

    public void uploadMaterial(String title, String rawDescription, SqlKategorie category, Long teacherId) {
        String formattedDescription = formatDescription(rawDescription);

        StudyMaterial material = new StudyMaterial();
        material.setTitle(title);
        material.setDescription(formattedDescription);
        material.setSqlKategorie(category);
        material.setTeacher(teacherId);

        materialRepo.save(material);
    }

    private String formatDescription(String description) {
        description = description
                .replaceAll("(?i)Example\\s*", "\n\nExample:\n```sql\n")
                .replaceAll("(?i)Syntax\\s*", "\n\nSyntax:\n```sql\n")
                .replaceAll("(?i)(;\\s*)", "$1```\n") // Закрываем код после ; (если есть)
                .trim();

        description = description.replaceAll("(?m)^[ \t]*\r?\n", "\n"); // Удаляем лишние пустые строки

        return description;
    }
}