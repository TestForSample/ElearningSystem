package com.example.ElearningSystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "quizreport")
public class QuizReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ansId;
    @Column(name = "quiz_id", unique = false, nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int quiz_id;
    @Column(name = "qu_id", nullable = false, unique = true)
    private String qu_id;
    @Column(unique = false, nullable = false)
    private String answersByUser;
    @Column(updatable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int marks;

    public int getAnsId() {
        return ansId;
    }

    public void setAnsId(int ansId) {
        this.ansId = ansId;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQu_id() {
        return qu_id;
    }

    public void setQu_id(String qu_id) {
        this.qu_id = qu_id;
    }

    public String getAnswersByUser() {
        return answersByUser;
    }

    public void setAnswersByUser(String answersByUser) {
        this.answersByUser = answersByUser;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }


}
