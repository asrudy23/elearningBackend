package com.elearningBackend.dto;

import com.elearningBackend.models.Course;
import com.elearningBackend.models.Lesson;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

public record FileResponse( Long id,
         String originalFileName,
         String fileName     ,
         String fileType,
         Long fileSize,
         String filePath,
         LocalDateTime uploadedAt,
                            Long courseId,
                            Long lessonId) { }
