package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.EAccess;

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

    private String videoUrl;

    private String duration;

    @Enumerated(EnumType.STRING)
    private EAccess accessLevel;

    @ManyToMany
    private List<Tag> tags;
}
