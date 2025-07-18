package com.example.ElearningSystem.repository;

import com.example.ElearningSystem.model.Questions;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Integer> {

    @Query(value = "SELECT * FROM questions WHERE question_id = :id", nativeQuery = true)
    Questions findByQuestionId(@Param("id") String questionId);

    @Query(value = "select * from questions where category=:category", nativeQuery = true)
    List<Questions> findByCategory(@Param("category") String category);

    @Query(value = "update questions set question=?1 ,correct_answer=?2,category=?3, difficultylevel=?4 where question_id=?5", nativeQuery = true)
    void updateEditFieldByQuestionId(String question, String category, String level, String correctAnswer, String questionId);

    @Modifying
    @Transactional
    @Query(value = "delete from questions where question_id=?1", nativeQuery = true)
    void deleteTheQuestionByQuestionId(String questionId);


}
