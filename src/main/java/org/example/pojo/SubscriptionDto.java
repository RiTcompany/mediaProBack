package org.example.pojo;

import lombok.Builder;
import lombok.Value;
import org.example.entities.Subscription;

import java.time.LocalDateTime;

@Value
@Builder
public class SubscriptionDto {
    Subscription subscription;
    LocalDateTime expirationDate;
}
