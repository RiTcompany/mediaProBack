package org.example.pojo;

import lombok.Builder;
import lombok.Data;
import org.example.enums.EAccess;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private String urlImg;
    private String duration;
    private Boolean isFavourite;
    private LocalDateTime favouriteSetTime;
    private LocalDateTime startTime;
    private Boolean isStarted;
    private Boolean isTestPassed;
    private Boolean isAvailable;
    private EAccess accessLevel;
    private List<LessonDto> lessons;
}
