package com.elearningBackend.services;

import com.elearningBackend.dto.QuestionRequest;
import com.elearningBackend.dto.QuizRequest;
import com.elearningBackend.dto.QuizResponse;
import com.elearningBackend.mapper.MapQuiz;
import com.elearningBackend.mapper.MapUser;
import com.elearningBackend.models.Question;
import com.elearningBackend.models.Quiz;
import com.elearningBackend.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final MapQuiz mapQuiz;
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;


    public QuizService(QuizRepository quizRepository, MapQuiz mapQuiz, CourseRepository courseRepository, ChapterRepository chapterRepository) {
        this.quizRepository = quizRepository;
        this.mapQuiz = mapQuiz;
        this.courseRepository = courseRepository;
        this.chapterRepository = chapterRepository;
    }

    public QuizResponse createQuiz(QuizRequest quizRequest) {

        Quiz quiz = new Quiz();
        quiz.setType(quizRequest.type());
        quiz.setTitle(quizRequest.title());
        if (chapterRepository.existsById(quizRequest.chapterId())) {
            quiz.setChapter(chapterRepository.findById(quizRequest.chapterId()).orElseThrow(() -> new RuntimeException("Chapter not found")));
        }
        if (courseRepository.existsById(quizRequest.courseId())) {
            quiz.setCourse(courseRepository.findById(quizRequest.courseId()).orElseThrow(() -> new RuntimeException("Course not found")));
        }
        //Quiz quizSaved = quizRepository.save(quiz);

        List<Question> questions = new ArrayList<>();

        for (QuestionRequest questionRequest : quizRequest.questions()) {
            Question question = new Question();
            question.setText(questionRequest.text());
            question.setCorrectAnswer(questionRequest.correctAnswer());
            question.setOptions(questionRequest.options());
            question.setQuiz(quiz);
            questions.add(question);
        }

        quiz.setQuestions(questions);

        Quiz quizSaved = quizRepository.save(quiz);
        return mapQuiz.mapQuizToResponse(quizSaved);
    }


}
