package org.example.pojo;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class SubscriptionAddRequest {
    SubscriptionDto subscription;
    LocalDateTime dateTime;
}
