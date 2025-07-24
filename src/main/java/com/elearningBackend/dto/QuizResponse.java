package com.elearningBackend.dto;

import com.elearningBackend.enumeration.QuizType;
import com.elearningBackend.models.Chapter;
import com.elearningBackend.models.Course;
import com.elearningBackend.models.Question;
import jakarta.persistence.*;

import java.util.List;

public record QuizResponse(
        Long id,
        QuizType type,
        String title,
        Long chapterId,
        Long courseId,
        List<QuestionResponse> questions) {
}
