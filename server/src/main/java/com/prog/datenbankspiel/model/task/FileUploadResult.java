package com.prog.datenbankspiel.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadResult {
    private String filePath;
    private String extractedText;
    // геттеры/сеттеры
}

    public FileUploadResult storeFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            Path path = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            FileUploadResult result = new FileUploadResult();
            result.setFilePath(path.toString());

            String extracted = "";
            if (filename.endsWith(".docx")) {
                extracted = extractTextFromDocx(path.toFile());
            } else if (filename.endsWith(".pdf")) {
                extracted = extractTextFromPdf(path.toFile());
            }

            result.setExtractedText(extracted);
            return result;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке: " + e.getMessage());
        }
    }
