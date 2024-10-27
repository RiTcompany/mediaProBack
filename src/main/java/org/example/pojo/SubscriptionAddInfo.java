package org.example.pojo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SubscriptionAddInfo {
    SubscriptionDto subscription;
    boolean hasTelegramId;
}
