package com.elearningBackend.services;

import com.elearningBackend.dto.CourseRequest;
import com.elearningBackend.dto.CourseResponse;
import com.elearningBackend.enumeration.Role;
import com.elearningBackend.mapper.MapCourse;
import com.elearningBackend.models.Course;
import com.elearningBackend.models.File;
import com.elearningBackend.models.User;
import com.elearningBackend.repositories.CourseRepository;
import com.elearningBackend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final MapCourse mapCourse;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    public CourseService(CourseRepository courseRepository, MapCourse mapCourse, FileStorageService fileStorageService, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.mapCourse = mapCourse;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest courseRequest) {


        if(courseRepository.existsByTitle(courseRequest.title())){
            throw new RuntimeException("Course title already exists");
        }
        File fileEntity = fileStorageService.storeFile(courseRequest.file());

            Course course = new Course();
            course.setTitle(courseRequest.title());
            course.setDescription(courseRequest.description());
            course.setFile(fileEntity);
            fileEntity.setCourse(course);

            List<User> teachers = new ArrayList<>();
            for(Long teacherId : courseRequest.teacherIds()) {
                User teacher = userRepository.findById(teacherId).orElse(null);
                if(teacher.getRole()== Role.TEACHER) {
                    teachers.add(teacher);
                }
                else{
                    throw new RuntimeException("You are not a teacher ");
                }
            }
            course.setTeachers(teachers);
            fileEntity.setCourse(course);
            Course courseSaved = courseRepository.save(course);
            return mapCourse.mapCourseToResponse(courseSaved);
        }
        public CourseResponse getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        return mapCourse.mapCourseToResponse(course);

        }

        public List<CourseResponse> getAllCourse() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(mapCourse::mapCourseToResponse).collect(Collectors.toList());

        }


    }



