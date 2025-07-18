package com.example.ElearningSystem.service;

import com.example.ElearningSystem.model.Questions;
import com.example.ElearningSystem.model.Quiz;
import com.example.ElearningSystem.model.QuizReport;
import com.example.ElearningSystem.model.Users;
import com.example.ElearningSystem.repository.AnswerRepository;
import com.example.ElearningSystem.repository.QuestionRepository;
import com.example.ElearningSystem.repository.QuizRepository;
import com.example.ElearningSystem.repository.UserRepository;
import com.example.ElearningSystem.utils.Levels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {


    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;


    public List<String> getAllCategories() {
        List<Questions> questions = questionRepository.findAll();
        List<String> categories = new ArrayList<>();
        for (Questions questions1 : questions) {
            categories.add(questions1.getCategory());
        }
        return categories;
    }

    public List<Questions> getAllQuestionsByCategory(String category) {
        List<String> categories = getAllCategories();
        List<Questions> list = new ArrayList<>();
        for (String category1 : categories) {
            if (category1.equals(category)) {
                list = questionRepository.findByCategory(category);

            }
        }
        return list;
    }

    public List<Questions> getAllQuestionsByLevel(String category, Levels level) {
        List<Questions> questionsList = getAllQuestionsByCategory(category);
        List<Questions> list = new ArrayList<>();
        for (Questions questions : questionsList) {
            if (questions.getDifficultylevel().name().equals(level.name())) {
                list.add(questions);
            }
        }
        return list;
    }

    public List<QuizReport> addTheQuiz(String category, Levels level, String username, List<QuizReport> quizReportList) {
        List<Questions> list = questionRepository.findAll();
        int count = (int) list.stream().filter(q -> q.getCategory().equals(category))
                .filter(s -> s.getDifficultylevel().equals(level)).count();
        List<Questions> questionsList=list.stream().filter(q -> q.getCategory().equals(category))
                .filter(s -> s.getDifficultylevel().equals(level)).toList();

        List<QuizReport> quizReportSet = new ArrayList<>();
        if (quizReportList.size() == count) {
            List<Quiz> quizList = quizRepository.findAll();
            Quiz quiz = new Quiz();
            List<QuizReport> quizReports = new ArrayList<>();
            Users users = userRepository.findByUsername(username);


            int quizCount = (int) quizList.stream().filter(q -> q.getCategory().equals(category))
                    .filter(s -> s.getLevel().equals(level)).count();
            if (count != 0 && quizCount == 0) {
                quiz.setCategory(category);
                quiz.setLevel(level);
                quiz.setUser_id(users.getUserId());
                quiz = quizRepository.save(quiz);

            } else {
                quiz = quizRepository.findByCategoryAndLevelAndUserId(category, level.name(), users.getUserId());

            }

            quizReports=checkTheAnswers(quizReportList,quiz);


            answerRepository.saveAll(quizReports);
            quiz.setTotalMarks(calculateTheTotalMarks(quizReports, quiz.getQuizId()));

            quizRepository.updateQuizTotalMarks(quiz.getTotalMarks(), quiz.getQuizId());
            quizReportSet = quizReports;
        } else {
            quizReportSet = null;
        }
        return quizReportSet;
    }


    public List<QuizReport> checkTheAnswers(List<QuizReport> quizReports,Quiz quiz) {
        List<QuizReport> quizReportss=new ArrayList<>();

        for (QuizReport quizReport1:quizReports){
            if (quizReport1.getQu_id()==null) continue;
            Questions questions = questionRepository.findByQuestionId(quizReport1.getQu_id());
            QuizReport quizReport=new QuizReport();
            quizReport.setQuiz_id(quiz.getQuizId());
            quizReport.setQu_id(questions.getQuestionId());
            quizReport.setAnswersByUser(quizReport1.getAnswersByUser());
          if(quizReport1.getQu_id().equals(questions.getQuestionId())){
              int mark=(questions.getCorrectAnswer().equals(quizReport1.getAnswersByUser())?5:0);
              quizReport.setMarks(mark);
          }

         quizReportss.add(quizReport);
        }

        return quizReportss;
    }

    public int calculateTheTotalMarks(List<QuizReport> quizReports, int quizId) {
        int total = 0;
        for (QuizReport quizReport : quizReports) {
            if (quizReport.getQuiz_id() == quizId) {
                total = Math.addExact(total, quizReport.getMarks());

            }
        }
        return total;
    }
}
