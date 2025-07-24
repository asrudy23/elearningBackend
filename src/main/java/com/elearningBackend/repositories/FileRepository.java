package com.elearningBackend.repositories;

import com.elearningBackend.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
