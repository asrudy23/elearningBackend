package com.elearningBackend.services;

import com.elearningBackend.models.File;
import com.elearningBackend.repositories.FileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class FileStorageService {

    private final FileRepository fileRepository;
    public FileStorageService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    public File storeFile(MultipartFile multipartFile) {

/*
        public void validateFile(MultipartFile file) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Fichier vide");
            }

            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Fichier trop volumineux (>10MB)");
            }

            String contentType = file.getContentType();
            if (!Arrays.asList("image/jpeg", "application/pdf", "video/mp4").contains(contentType)) {
                throw new IllegalArgumentException("Type de fichier non supporté");
            }
        }*/
        try {
            // 1. Créer le nom unique
            String uniqueFileName = generateUniqueFileName(multipartFile.getOriginalFilename());

            // 2. Créer le chemin de stockage
            Path storagePath = Paths.get(uploadDir, uniqueFileName);

            // 3. Sauvegarder physiquement
            Files.copy(multipartFile.getInputStream(), storagePath);

            // 4. Créer l'entité File
            File fileEntity = new File();
            fileEntity.setOriginalFileName(multipartFile.getOriginalFilename());
            fileEntity.setFileName(uniqueFileName);
            fileEntity.setFileType(multipartFile.getContentType());
            fileEntity.setFileSize(multipartFile.getSize());
            fileEntity.setFilePath(storagePath.toString());


            return fileEntity;
        } catch (IOException e) {
            throw new RuntimeException("Échec du stockage du fichier: " + e.getMessage());
        }
    }

    private String generateUniqueFileName(String originalName) {
        return UUID.randomUUID() + "_" + originalName.replace(" ", "_");
    }

    @Transactional
    public void deleteFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("Fichier non trouvé"));

        try {
            // Supprimer le fichier physique
            Files.deleteIfExists(Paths.get(file.getFilePath()));

            // Supprimer l'entité
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new RuntimeException("Échec de la suppression du fichier", e);
        }
    }
}