package org.example.pojo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SubscriptionBenefitDto {
    Long id;
    String benefitName;
    boolean isAvailable;
}
