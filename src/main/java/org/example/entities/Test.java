package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CollectionType;

import java.util.List;

@Entity
@Table(name = "tests")
@Getter
@Setter
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;

    private String name;

    private String description;

    @OneToMany
    private List<QuestionAnswer> questionAnswers;

    private String trueAnswers;

    public Test(Long courseId, String name, String description, List<QuestionAnswer> questionAnswers) {
    this.courseId = courseId;
    this.name = name;
    this.description = description;
    this.questionAnswers = questionAnswers;
    }

    public Test() {

    }
}
