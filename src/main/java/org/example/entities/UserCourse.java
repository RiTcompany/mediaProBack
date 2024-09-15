package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_courses")
public class UserCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "is_favourite")
    private Boolean isFavourite;

    @Column(name = "favourite_set_time")
    private LocalDateTime favouriteSetTime;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_set_time")
    private LocalDateTime completedSetTime;

    @Column(name = "is_started")
    private Boolean isStarted;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "is_test_passed")
    private Boolean isTestPassed;

    public UserCourse(Course course, User user) {
        this.course = course;
        this.user = user;
        this.isFavourite = false;
        this.favouriteSetTime = LocalDateTime.now();
        this.isCompleted = false;
        this.completedSetTime = LocalDateTime.now();
        this.isStarted = false;
    }
}
