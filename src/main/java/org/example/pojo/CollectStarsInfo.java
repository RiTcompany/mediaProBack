package org.example.pojo;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CollectStarsInfo {
    Integer currentStarsCount;
    Integer targetStarsCount;
    Integer currentLessonStreak;
    Integer targetLessonStreak;
    double featureDiscount;
    LocalDateTime expiresAt;
}
