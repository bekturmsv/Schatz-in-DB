package pti.softwareentwicklg.SchatzInDb.service.task;

import pti.softwareentwicklg.SchatzInDb.model.task.FileUploadResult;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() throws IOException {
        if (uploadDir == null || uploadDir.isBlank()) {
            throw new IllegalStateException(
                    "Property 'file.upload-dir' must be set (e.g. uploads/)"
            );
        }
        Files.createDirectories(Paths.get(uploadDir));
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
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    public String extractTextFromDocx(File file) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(file))) {
            return new XWPFWordExtractor(doc).getText();
        }
    }

    public String extractTextFromPdf(File file) throws IOException {
        try (PDDocument doc = PDDocument.load(file)) {
            return new PDFTextStripper().getText(doc);
        }
    }

    public Resource loadFileAsResource(String filename) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("Файл не найден: " + filename);
        }
    }
}