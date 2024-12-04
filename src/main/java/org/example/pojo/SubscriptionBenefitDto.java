package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SubscriptionBenefitDto {
    Long id;
    String benefitName;
    @JsonProperty("is_available")
    boolean isAvailable;
}
