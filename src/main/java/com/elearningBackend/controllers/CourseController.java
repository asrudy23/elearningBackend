package com.elearningBackend.controllers;


import com.elearningBackend.dto.CourseRequest;
import com.elearningBackend.dto.CourseResponse;
import com.elearningBackend.models.Course;
import com.elearningBackend.services.CourseService;
import com.elearningBackend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/courses")
    public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponse> createCourse(
                @Valid @ModelAttribute CourseRequest request
        ) {
            CourseResponse course = courseService.createCourse(request);

            return ResponseEntity.ok(course);
        }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        CourseResponse course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }
    @GetMapping()
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourse();
        return ResponseEntity.ok(courses);
    }
    }

