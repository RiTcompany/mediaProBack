package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("eSubscriptionType")
    ESubscriptionType eSubscriptionType;
    @JsonIgnore
    ESubscriptionType esubscriptionType;
    List<SubscriptionBenefitDto> benefits;
}
