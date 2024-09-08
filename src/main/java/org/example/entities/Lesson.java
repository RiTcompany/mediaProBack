package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "lessons")
@NoArgsConstructor
public class Lesson {  //отдавать дтохой

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String name;

    private String description;

    private String content;

    private String practiceTask;  //get если есть подписка

    private String videoUrl;

    private String duration;

    private Boolean isCompleted;

    private LocalDateTime completedSetTime;

    @Enumerated(EnumType.STRING)
    private EAccess accessLevel;

    private Boolean isFavourite;

    private LocalDateTime favouriteSetTime;

    @ManyToMany
    private List<Tag> tags;
}
