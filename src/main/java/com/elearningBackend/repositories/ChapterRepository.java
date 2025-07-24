package com.elearningBackend.repositories;

import com.elearningBackend.models.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    boolean existsById(Long id);
}
