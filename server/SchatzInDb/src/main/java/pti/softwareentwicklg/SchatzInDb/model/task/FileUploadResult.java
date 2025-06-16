package pti.softwareentwicklg.SchatzInDb.model.task;

import lombok.Data;

@Data
public class FileUploadResult {
    private String filePath;
    private String extractedText;
}