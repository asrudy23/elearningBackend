package com.elearningBackend.services;

import com.elearningBackend.dto.ChapterRequest;
import com.elearningBackend.dto.ChapterResponse;
import com.elearningBackend.dto.LessonRequest;
import com.elearningBackend.mapper.MapChapter;
import com.elearningBackend.models.Chapter;
import com.elearningBackend.models.Course;
import com.elearningBackend.models.File;
import com.elearningBackend.models.Lesson;
import com.elearningBackend.repositories.ChapterRepository;
import com.elearningBackend.repositories.CourseRepository;
import com.elearningBackend.repositories.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChapterService {
    private final LessonRepository lessonRepository;
    private CourseService courseService;
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService;
    private final MapChapter mapChapter;
    public ChapterService(CourseService courseService, ChapterRepository chapterRepository, CourseRepository courseRepository, FileStorageService fileStorageService, MapChapter mapChapter, LessonRepository lessonRepository) {
        this.courseService = courseService;
        this.chapterRepository = chapterRepository;
        this.courseRepository = courseRepository;
        this.fileStorageService = fileStorageService;
        this.mapChapter = mapChapter;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    public ChapterResponse createChapter(ChapterRequest chapterRequest) {

        Chapter chapter = new Chapter();
        chapter.setTitle(chapterRequest.title());
        if (courseRepository.existsById(chapterRequest.courseId())) {
            chapter.setCourse(courseRepository.findById(chapterRequest.courseId()).orElse(null));
        }
        else{
            throw new RuntimeException("Couldn't find course with id " + chapterRequest.courseId());
        }
       Chapter chapterSaved = chapterRepository.save(chapter);


        List<Lesson> lessons = new ArrayList<>();
        for (LessonRequest lessonRequest : chapterRequest.lessons()) {


            File fileEntity = fileStorageService.storeFile(lessonRequest.file());
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonRequest.title());
        lesson.setContent(lessonRequest.content());
        lesson.setChapter(chapterSaved);
        lesson.setFile(fileEntity);
        fileEntity.setLesson(lesson);
        lessons.add(lesson);
       lessonRepository.save(lesson);
        }

        chapterSaved.setLessons(lessons);
        chapterRepository.save(chapterSaved);


        return mapChapter.mapChapterToResponse(chapterSaved);


    }

    public ChapterResponse getChapter(Long id) {
        Chapter chapter = chapterRepository.findById(id).orElseThrow(()->new RuntimeException("Couldn't find chapter with id " + id));
        return mapChapter.mapChapterToResponse(chapter);
    }

    public List<ChapterResponse> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAll();
        return chapters.stream().map(mapChapter::mapChapterToResponse).collect(Collectors.toList());
    }

}
