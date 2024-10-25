package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.ESubscriptionType;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "benefits")
public class SubscriptionBenefit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String benefitName;

    private ESubscriptionType subscriptionType;
}
