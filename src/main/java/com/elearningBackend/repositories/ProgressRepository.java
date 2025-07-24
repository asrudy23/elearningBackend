package com.elearningBackend.repositories;

import com.elearningBackend.models.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository  extends JpaRepository<Progress, Long> {
    boolean existsById(Long id);
}
