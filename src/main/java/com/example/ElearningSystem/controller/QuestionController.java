package com.example.ElearningSystem.controller;

import com.example.ElearningSystem.model.Questions;
import com.example.ElearningSystem.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("adminControl/getAllQuestions")
    public ResponseEntity<List<Questions>> getAllQuestions() {
        return ResponseEntity.status(200).body(questionService.getAllQuestions());

    }

    @PostMapping("adminControl/addQuestion")
    public ResponseEntity<Questions> addAQuestion(@RequestBody Questions questions) {
        return ResponseEntity.status(201)
                .body(questionService.addAQuestion(questions));
    }

    @PostMapping("adminControl/addMultipleQuestions")
    public ResponseEntity<List<Questions>> addMultipleQuestions(@RequestBody List<Questions> questions) {
        return ResponseEntity.status(201).body(questionService.addMultipleQuestions(questions));

    }

    @GetMapping("adminControl/questionById/{questionId}")
    public ResponseEntity<Object> questionById(@PathVariable("questionId") String q_id) {
        Object object=questionService.questionById(q_id);

        if(object==null){
        return ResponseEntity.status(404).body("Question not found");
        }
        return ResponseEntity.status(200).body(object);
    }

    @PutMapping("adminControl/updateTheQuestion/{questionId}")
    public ResponseEntity<Object> updateTheQuestion(@PathVariable("questionId") String questionId, @RequestBody Questions questions) {
        Object object=questionService.updateTheQuestion(questionId,questions);

        if(object.equals(HttpStatus.NOT_FOUND)){
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(object);
        }if(object.equals(HttpStatus.BAD_REQUEST)){
            return ResponseEntity.status(400).body(object);
        }
        return ResponseEntity.status(202).body(object);
    }

    @DeleteMapping("adminControl/deleteTheQuestion/{questionId}")
    public ResponseEntity<Object> deleteTheQuestion(@PathVariable("questionId") String questionId) {
        Object object=questionService.deleteTheQuestion(questionId);
        if(object.equals(HttpStatus.NOT_FOUND)){
            return ResponseEntity.status(404).body(HttpStatus.NOT_FOUND.name());
        }
        return ResponseEntity.status(200).body(object);
    }

}

