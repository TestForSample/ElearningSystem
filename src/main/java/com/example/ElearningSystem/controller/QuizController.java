package com.example.ElearningSystem.controller;

import com.example.ElearningSystem.model.QuizReport;
import com.example.ElearningSystem.service.QuizService;
import com.example.ElearningSystem.utils.Levels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("addQuiz/{category}/{level}/{username}")
    public List<QuizReport> addQuiz(@PathVariable("category") String ca, @PathVariable("level") Levels level
            , @PathVariable("username") String email, @RequestBody List<QuizReport> quizReportList) {
        return quizService.addTheQuiz(ca, level, email, quizReportList);
    }
}
