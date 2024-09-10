package org.example.pojo;

import lombok.Builder;
import org.example.entities.QuestionAnswer;

import java.util.List;

@Builder
public record TestResultDto(int trueAnswersCount, Long testId, String testName, boolean passed,
                            List<QuestionAnswer> questionAnswers, String resultAnswer) {
}
