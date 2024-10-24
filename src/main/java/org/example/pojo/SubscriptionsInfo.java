package org.example.pojo;

import lombok.Builder;
import lombok.Value;
import org.example.entities.Subscription;

import java.util.List;

@Value
@Builder
public class SubscriptionsInfo {
    List<Subscription> subscriptions;
    double discount;
}
