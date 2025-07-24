package com.elearningBackend.dto;

import com.elearningBackend.models.File;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LessonResponse(
        Long id,
        String title,
        String content,
        FileResponse file,
        @NotNull Long chapterId) {
}
