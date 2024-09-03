package org.example.pojo;

import lombok.Builder;
import lombok.Data;
import org.example.entities.Course;
import org.example.entities.Tag;

import java.util.List;

@Data
@Builder
public class LessonDto {

    private Long id;

    private Course course;

    private String name;

    private String duration;

    private String description;

    private Boolean isCompleted;

    private Boolean accessability;

    private Boolean isFavourite;

    private List<Tag> tags;
}
