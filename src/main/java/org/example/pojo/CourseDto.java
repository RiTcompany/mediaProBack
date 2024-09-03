package org.example.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
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
    private Date favouriteSetTime;
    private Date startTime;
    private Boolean isStarted;
    private Boolean isAvailable;
    private List<LessonDto> lessons;
}
