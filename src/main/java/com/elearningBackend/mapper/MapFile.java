package com.elearningBackend.mapper;

import com.elearningBackend.dto.FileResponse;
import com.elearningBackend.models.File;
import org.springframework.stereotype.Component;

@Component
public class MapFile {
    public FileResponse mapFileToResponse(File file) {
        return new FileResponse(
                file.getId(),
                file.getOriginalFileName(),
                file.getFileName(),
                file.getFileType(),
                file.getFileSize(),
                file.getFilePath(),
                file.getUploadedAt(),
                file.getCourse() != null ? file.getCourse().getId() : null,
                file.getLesson() != null ? file.getLesson().getId() : null
        );
    }
}
