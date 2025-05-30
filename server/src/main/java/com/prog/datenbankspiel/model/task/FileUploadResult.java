package com.prog.datenbankspiel.model.task;

import lombok.Data;

@Data
public class FileUploadResult {
    private String filePath;
    private String extractedText;
}
