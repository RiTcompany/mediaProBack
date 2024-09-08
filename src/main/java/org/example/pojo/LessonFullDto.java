package org.example.pojo;

import org.example.entities.EAccess;
import org.example.entities.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record LessonFullDto(Long id, Long courseId, String name, String description, String content,
                            String practiceTask, String videoUrl, String duration, Boolean isCompleted,
                            LocalDateTime completedSetTime, EAccess accessLevel, Boolean isFavourite,
                            LocalDateTime favouriteSetTime, List<Tag> tags) {
}
