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
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String name;

    private String description;

    private String content;

    private String practiceTask;

    private String imgUrl;

    private String duration;

    private Boolean isCompleted;

    private LocalDateTime completedSetTime;

    private Boolean accessability;

    private Boolean isFavourite;

    private LocalDateTime favouriteSetTime;

    @Lob
    private String testQuestions;

    @ManyToMany
    private List<Tag> tags;
}
