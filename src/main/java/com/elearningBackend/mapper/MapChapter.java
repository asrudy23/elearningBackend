package com.elearningBackend.mapper;

import com.elearningBackend.dto.ChapterResponse;
import com.elearningBackend.models.Chapter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class MapChapter {
    private final MapLesson mapLesson;
    public MapChapter(MapLesson mapLesson) {
        this.mapLesson = mapLesson;
    }

    public ChapterResponse mapChapterToResponse(Chapter chapter) {
        return new ChapterResponse(
                chapter.getId(),
                chapter.getTitle(),
                chapter.getCourse().getId() == null ? null : chapter.getCourse().getId(),
                chapter.getLessons().stream().map(lesson ->mapLesson.mapLessonToResponse(lesson)).collect(Collectors.toList())

        );
    }
}
