package com.elearningBackend.dto;

import com.elearningBackend.models.File;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CourseRequest(
        @NotBlank String title,
        @NotBlank @Lob String description,
        @NotEmpty List<Long> teacherIds,
        MultipartFile file
) {}
