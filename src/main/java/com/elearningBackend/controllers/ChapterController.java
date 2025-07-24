package com.elearningBackend.controllers;

import com.elearningBackend.dto.ChapterRequest;
import com.elearningBackend.dto.ChapterResponse;
import com.elearningBackend.services.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChapterResponse> createChapter(@ModelAttribute @Valid ChapterRequest chapterRequest) {
        ChapterResponse response = chapterService.createChapter(chapterRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterResponse> getChapter(@PathVariable Long id) {
        ChapterResponse response = chapterService.getChapter(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ChapterResponse>> getAllChapters() {
        List<ChapterResponse> responses = chapterService.getAllChapters();
        return ResponseEntity.ok(responses);
    }
}

