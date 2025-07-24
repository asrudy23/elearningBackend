package com.elearningBackend.repositories;

import com.elearningBackend.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository  extends JpaRepository<Question, Long> {
    boolean existsById(Long id);
}
