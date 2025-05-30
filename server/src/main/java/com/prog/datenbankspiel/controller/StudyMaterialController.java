package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.model.task.FileUploadResult;
import com.prog.datenbankspiel.model.task.StudyMaterial;
import com.prog.datenbankspiel.service.FileStorageService;
import com.prog.datenbankspiel.service.StudyMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class StudyMaterialController {

    private final StudyMaterialService materialService;
    private final FileStorageService fileStorageService;

    @PostMapping("/upload-and-create")
    public ResponseEntity<StudyMaterial> uploadAndCreate(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("teacherId") Long teacherId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "videoUrl", required = false) String videoUrl,
            @RequestParam(value = "externalLink", required = false) String externalLink,
            @RequestParam(value = "content", required = false) String manualContent
    ) {
        StudyMaterial material = new StudyMaterial();
        material.setDescription(description);
        material.setTeacherId(teacherId);
        material.setType(type);
        material.setVideoUrl(videoUrl);
        material.setExternalLink(externalLink);

        if (file != null && !file.isEmpty()) {
            FileUploadResult result = fileStorageService.storeFile(file);
            material.setFilePath(result.getFilePath());
            material.setContent(result.getExtractedText());
        } else {
            material.setContent(manualContent);
        }

        StudyMaterial saved = materialService.createMaterial(material, teacherId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<StudyMaterial> getAll() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyMaterial> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyMaterial> update(
            @PathVariable Long id,
            @RequestBody StudyMaterial material
    ) {
        return ResponseEntity.ok(materialService.updateMaterial(id, material));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher/{teacherId}")
    public List<StudyMaterial> getByTeacher(@PathVariable Long teacherId) {
        return materialService.getMaterialsByTeacher(teacherId);
    }
}
