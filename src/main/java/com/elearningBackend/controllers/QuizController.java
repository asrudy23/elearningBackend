package com.elearningBackend.controllers;

import com.elearningBackend.dto.QuizRequest;
import com.elearningBackend.dto.QuizResponse;
import com.elearningBackend.services.QuizService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizRequest quizRequest) {
        QuizResponse createdQuiz = quizService.createQuiz(quizRequest);
        return ResponseEntity.ok(createdQuiz);
    }
}
