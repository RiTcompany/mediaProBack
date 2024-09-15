package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_lessons")
public class UserLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "is_favourite")
    private Boolean isFavourite;

    @Column(name = "favourite_set_time")
    private LocalDateTime favouriteSetTime;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_set_time")
    private LocalDateTime completedSetTime;

    public UserLesson(Lesson lesson, User user) {
        this.lesson = lesson;
        this.user = user;
        this.isFavourite = false;
        this.favouriteSetTime = LocalDateTime.now();
        this.isCompleted = false;
        this.completedSetTime = LocalDateTime.now();
    }
}

