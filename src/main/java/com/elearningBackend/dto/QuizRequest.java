package com.elearningBackend.dto;

import com.elearningBackend.enumeration.QuizType;
import com.elearningBackend.models.Chapter;
import com.elearningBackend.models.Course;
import com.elearningBackend.models.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record QuizRequest(

        @NotBlank QuizType type,
        @NotBlank String title,
        Long chapterId,
        Long courseId,
        List<QuestionRequest> questions) {
}
