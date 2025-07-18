package com.example.ElearningSystem.model;

import com.example.ElearningSystem.utils.Levels;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz")
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int quizId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Levels level;
    @Column(nullable = false, name = "user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int user_id;
    @Column(nullable = false)
    private String category;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long totalMarks;

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public Levels getLevel() {
        return level;
    }

    public void setLevel(Levels level) {
        this.level = level;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(long totalMarks) {
        this.totalMarks = totalMarks;
    }

}
