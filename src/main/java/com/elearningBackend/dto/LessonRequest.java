package com.elearningBackend.dto;

import com.elearningBackend.models.Chapter;
import com.elearningBackend.models.File;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record LessonRequest(
                            @NotBlank String title,
                            String content,
                            MultipartFile file) { }
