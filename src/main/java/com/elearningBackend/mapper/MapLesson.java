package com.elearningBackend.mapper;

import com.elearningBackend.dto.LessonResponse;
import com.elearningBackend.models.Lesson;
import org.springframework.stereotype.Component;

@Component
public class MapLesson {
    private final MapFile mapFile;
    public MapLesson(MapFile mapFile) {
        this.mapFile = mapFile;
    }
    public LessonResponse mapLessonToResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getContent(),
                mapFile.mapFileToResponse(lesson.getFile()),
                lesson.getChapter().getId()

        );
    }
}
