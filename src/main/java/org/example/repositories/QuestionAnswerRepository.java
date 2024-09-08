package org.example.repositories;

import org.example.entities.QuestionAnswer;
import org.example.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
}