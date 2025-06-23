package pti.softwareentwicklg.SchatzInDb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pti.softwareentwicklg.SchatzInDb.model.enums.SqlKategorie;
import pti.softwareentwicklg.SchatzInDb.model.task.FileUploadResult;
import pti.softwareentwicklg.SchatzInDb.model.task.StudyMaterial;
import pti.softwareentwicklg.SchatzInDb.service.task.FileStorageService;
import pti.softwareentwicklg.SchatzInDb.service.task.StudyMaterialService;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class StudyMaterialController {

    private final StudyMaterialService materialService;

    @PostMapping("/upload")
    public ResponseEntity<Void> upload(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Long teacherId,
            @RequestParam SqlKategorie sqlKategorie
    ) {
        materialService.uploadMaterial(title, description, sqlKategorie, teacherId);
        return ResponseEntity.ok().build();
    }

    //    @PutMapping("/{id}")
//    public ResponseEntity<StudyMaterial> update(
//            @PathVariable Long id,
//            @RequestBody StudyMaterial material
//    ) {
//        return ResponseEntity.ok(materialService.updateMaterial(id, material));
//    }

    @GetMapping
    public List<StudyMaterial> getAll() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyMaterial> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getByTeacherId/{teacherId}")
    public List<StudyMaterial> getByTeacher(@PathVariable Long teacherId) {
        return materialService.getMaterialsByTeacher(teacherId);
    }
}
