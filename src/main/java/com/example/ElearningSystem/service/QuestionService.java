package com.example.ElearningSystem.service;

import com.example.ElearningSystem.model.Questions;
import com.example.ElearningSystem.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Questions> getAllQuestions() {
        return questionRepository.findAll();
    }


    public Questions addAQuestion(Questions questions) {
        return questionRepository.save(questions);

    }

    public List<Questions> addMultipleQuestions(List<Questions> questions) {
        return questionRepository.saveAll(questions);
    }

    public Questions questionById(String q_id) {

        return questionRepository.findByQuestionId(q_id);
    }

    public Object updateTheQuestion(String questionId, Questions questions) {
        List<Questions> questionsList = questionRepository.findAll();
        Questions questions1 = questionRepository.findByQuestionId(questionId);
         if(questions1==null){
             return HttpStatus.NOT_FOUND;
         }
         if(!questionId.equals(questions.getQuestionId())){
             return HttpStatus.BAD_REQUEST;
         }
           questions1.setQuestion(questions.getQuestion());
           questions1.setCategory(questions.getCategory());
           questions1.setCorrectAnswer(questions.getCorrectAnswer());
           questions1.setDifficultylevel(questions.getDifficultylevel());

        return questionRepository.save(questions1);
    }

    public Object deleteTheQuestion(String questionId) {
        Questions questions=questionRepository.findByQuestionId(questionId);
        if(questions==null){
          return HttpStatus.NOT_FOUND;
        }

            questionRepository.deleteTheQuestionByQuestionId(questionId);





return HttpStatus.OK;

    }
}
