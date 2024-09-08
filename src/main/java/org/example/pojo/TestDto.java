package org.example.pojo;

import lombok.Builder;
import org.example.entities.QuestionAnswer;

import java.util.List;

@Builder
public record TestDto(Long courseId, String name, String description, List<QuestionAnswer> questionAnswers) {
}
