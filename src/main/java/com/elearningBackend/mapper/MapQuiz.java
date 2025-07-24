package com.elearningBackend.mapper;

import com.elearningBackend.dto.QuizResponse;
import com.elearningBackend.models.Quiz;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapQuiz {
    private final MapQuestion mapQuestion;
    public MapQuiz(MapQuestion mapQuestion) {
        this.mapQuestion = mapQuestion;
    }
    public QuizResponse mapQuizToResponse(Quiz quiz) {
        return new QuizResponse(
                quiz.getId(),
                quiz.getType(),
                quiz.getTitle(),
                quiz.getChapter().getId() ==null ? null : quiz.getChapter().getId(),
                quiz.getCourse().getId(),
                quiz.getQuestions().stream().map(question ->mapQuestion.MapQuestionToResponse(question) ).collect(Collectors.toList())
        );
    }
}
