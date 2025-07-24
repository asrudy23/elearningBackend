package com.elearningBackend.repositories;

import com.elearningBackend.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsById(Long id);
}
