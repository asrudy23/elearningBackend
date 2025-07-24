package com.elearningBackend.repositories;

import com.elearningBackend.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    boolean existsById(Long id);
}
