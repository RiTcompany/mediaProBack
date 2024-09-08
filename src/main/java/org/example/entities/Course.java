package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String urlImg;

    private String duration;

    private Boolean isFavourite;

    private LocalDateTime favouriteSetTime;

    private Boolean isStarted;

    private LocalDateTime startTime;

    @OneToOne
    private Test courseTest;

    @Enumerated(EnumType.STRING)
    private EAccess accessLevel;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;
}
