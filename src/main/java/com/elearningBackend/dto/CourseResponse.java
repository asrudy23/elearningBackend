package com.elearningBackend.dto;

import com.elearningBackend.models.*;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CourseResponse(Long id, String title,
                             @NotBlank @Lob String description,
                             FileResponse file, List<ChapterResponse> chapters,
                             List<QuizResponse> quizzes,
                             List<UserResponse> students,
                             List<UserResponse> teachers) {
}
