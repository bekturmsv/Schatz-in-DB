package com.prog.datenbankspiel.controller;

import com.prog.datenbankspiel.model.user.enums.SpecialistGroup;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/specializations")
public class SpecialistGroupController {

    @GetMapping
    public List<String> getAllSpecialistGroups() {
        return Arrays.stream(SpecialistGroup.values())
                .map(Enum::name)
                .toList();
    }
}
