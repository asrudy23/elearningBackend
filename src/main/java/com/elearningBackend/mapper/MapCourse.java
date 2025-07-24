package com.elearningBackend.mapper;

import com.elearningBackend.dto.CourseResponse;
import com.elearningBackend.models.Course;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapCourse {
    private final MapUser mapUser;
    private final MapFile mapFile;
    private final MapChapter mapChapter;
    private final MapQuiz mapQuiz;
    public MapCourse(MapUser mapUser, MapFile mapFile, MapChapter mapChapter, MapQuiz mapQuiz) {
        this.mapUser = mapUser;
        this.mapFile = mapFile;
        this.mapChapter =mapChapter;
        this.mapQuiz = mapQuiz;
    }
    public CourseResponse  mapCourseToResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                mapFile.mapFileToResponse(course.getFile()),
                course.getChapters().stream().map(chapter->mapChapter.mapChapterToResponse(chapter)).collect(Collectors.toList()),
                course.getQuizzes().stream().map(quiz -> mapQuiz.mapQuizToResponse(quiz)).collect(Collectors.toList()),
                course.getStudents().stream().map(student -> mapUser.mapUserToResponse(student)).collect(Collectors.toList()),
                course.getTeachers().stream().map(teacher -> mapUser.mapUserToResponse(teacher)).collect(Collectors.toList())
        );
    }
}
