package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pti.softwareentwicklg.SchatzInDb.model.enums.SpecialistGroup;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationsController {

    @GetMapping("/getAll")
    public ResponseEntity<?> getSpecialist () {
        List<SpecialistGroup> specialistGroups = List.of(SpecialistGroup.values());
        return ResponseEntity.ok(specialistGroups);
    }
}