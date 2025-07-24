package com.elearningBackend.dto;

import com.elearningBackend.models.Course;
import com.elearningBackend.models.Lesson;
import jakarta.persistence.*;

import java.util.List;

public record ChapterResponse(
        Long id,
        String title,
        Long courseId,
        List<LessonResponse> lessons) {
}
