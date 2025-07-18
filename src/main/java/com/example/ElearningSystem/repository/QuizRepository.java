package com.example.ElearningSystem.repository;

import com.example.ElearningSystem.model.Quiz;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    @Query(value = "select * from quiz where category=?1 and level=?2 and user_id=?3", nativeQuery = true)
    Quiz findByCategoryAndLevelAndUserId(String category, String level, int userId);

    @Modifying
    @Transactional
    @Query(value = "update quiz set total_marks=?1 where quiz_id=?2", nativeQuery = true)
    void updateQuizTotalMarks(long totalMarks, int quizId);
}
