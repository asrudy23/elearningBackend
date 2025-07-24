package com.elearningBackend.dto;

import com.elearningBackend.models.Quiz;
import jakarta.persistence.*;

import java.util.List;

public record QuestionResponse(
        Long id,
        String text,
        List<String> options,
        String correctAnswer,
        Long quizId) {
}
