package com.elearningBackend.models;

import com.elearningBackend.enumeration.StatusCourseDemande;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionAsking {


        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        // L'étudiant qui fait la demande
        @ManyToOne
        @JoinColumn(name = "student_id", nullable = false)
        private User student;

        // Le cours demandé
        @ManyToOne
        @JoinColumn(name = "course_id", nullable = false)
        private Course course;

        // Statut de la demande
        @Enumerated(EnumType.STRING)
        private StatusCourseDemande statut = StatusCourseDemande.EN_ATTENTE;
    }

