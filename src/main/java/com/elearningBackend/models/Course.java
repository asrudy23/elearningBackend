package com.elearningBackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    @ManyToMany(mappedBy = "enrolledCourses")
    private List<User> students = new ArrayList<>();

    @ManyToMany(mappedBy = "coursesTaught")
    private List<User> teachers = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="file_id",referencedColumnName = "id")
    private File file;

}