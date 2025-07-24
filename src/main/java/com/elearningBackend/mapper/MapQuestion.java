package com.elearningBackend.mapper;

import com.elearningBackend.dto.QuestionResponse;
import com.elearningBackend.models.Question;
import org.springframework.stereotype.Component;

@Component
public class MapQuestion {
    public QuestionResponse MapQuestionToResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getText(),
                question.getOptions(),
                question.getCorrectAnswer(),
                question.getQuiz().getId() == null ? null : question.getQuiz().getId()
        );
    }
}
