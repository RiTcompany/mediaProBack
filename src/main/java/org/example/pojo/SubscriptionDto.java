package org.example.pojo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SubscriptionDto {
    String name;
    Integer price;
    String description;
}
