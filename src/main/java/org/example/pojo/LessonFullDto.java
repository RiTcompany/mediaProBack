package org.example.pojo;

import lombok.Builder;
import lombok.Value;
import org.example.enums.EAccess;
import org.example.entities.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class LessonFullDto {
    Long id;
    Long courseId;
    String name;
    String description;
    String content;
    String practiceTask;
    String videoUrl;
    String duration;
    Boolean isCompleted;
    LocalDateTime completedSetTime;
    EAccess accessLevel;
    Boolean isFavourite;
    Boolean isAvailable;
    LocalDateTime favouriteSetTime;
    List<Tag> tags;
    Boolean isAvailableForPracticeTaskCheck;
}
