package com.example.ElearningSystem.model;

import com.example.ElearningSystem.utils.Levels;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "questions")
public class Questions {
    @Id
    @Column(name = "question_id", unique = true, nullable = false)
    private String questionId;
    @Column(nullable = false)
    private String category;
    @Enumerated(EnumType.STRING)
    @Column(name = "difficultylevel", nullable = false)
    private Levels difficultylevel;
    @Column(nullable = false, unique = true)
    private String question;
    @Column(nullable = false)
    private String correctAnswer;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Levels getDifficultylevel() {
        return difficultylevel;
    }

    public void setDifficultylevel(Levels difficultylevel) {
        this.difficultylevel = difficultylevel;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "questionId='" + questionId + '\'' +
                ", category='" + category + '\'' +
                ", difficultylevel=" + difficultylevel +
                ", question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
