package com.example.ElearningSystem.repository;

import com.example.ElearningSystem.model.QuizReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<QuizReport, Integer> {
    @Query(value = "select * from answers where qu_id:questionId", nativeQuery = true)
    QuizReport findByQuestionId(@Param("questionId") String questionId);

    @Query(value = "delete from answers where qu_id:questionId", nativeQuery = true)
    List<QuizReport> deleteByQuestionId(@Param("questionId") String questionId);
}
