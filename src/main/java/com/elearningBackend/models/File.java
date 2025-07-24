package com.elearningBackend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class File {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;
        private String originalFileName;   // nom d’origine
        private String fileName;           // nom stocké (UUID, timestamp…)
        private String fileType;           // MIME type (ex. image/png)
        private Long fileSize;             // taille en octets
        private String filePath;           // chemin local ou URL externe
        private LocalDateTime uploadedAt;  // date d’upload
        private String uploadedBy;//// optionnel, utilisateur uploader
        @OneToOne(mappedBy ="file")
        private Course course;

        @OneToOne(mappedBy ="file")
        private Lesson lesson;
    }


