package org.example.repositories;

import org.example.entities.SubscriptionBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionBenefitRepository extends JpaRepository<SubscriptionBenefit, Long> {
}