package org.example.pojo;

import lombok.Builder;
import lombok.Data;
import org.example.entities.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class LessonDto {

    private Long id;

    private Long courseId;

    private String name;

    private String duration;

    private String description;

    private Boolean isCompleted;

    private Boolean isAvailable;

    private Boolean isFavourite;

    private String accessLevel;

    private LocalDateTime favouriteSetTime;

    private List<Tag> tags;
}
