package org.example.pojo;

import lombok.Builder;
import lombok.Value;
import org.example.entities.SubscriptionBenefit;
import org.example.enums.ESubscriptionType;

import java.util.List;

@Value
@Builder
public class SubscriptionDto {
    Integer id;
    String name;
    Integer price;
    String description;
    ESubscriptionType eSubscriptionType;
    List<SubscriptionBenefitDto> benefits;
}
