package com.elearningBackend.repositories;

import com.elearningBackend.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTitle(String title);
    boolean existsById(Long id);
}
