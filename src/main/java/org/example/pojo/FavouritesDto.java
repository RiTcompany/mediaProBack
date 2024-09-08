package org.example.pojo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
public record FavouritesDto(List<Long> favouriteCourses, List<Long> favouriteLessons) {

}
