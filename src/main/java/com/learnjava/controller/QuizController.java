package com.learnjava.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnjava.dto.QuizSubmissionRequest;
import com.learnjava.dto.QuizSubmissionResponse;
import com.learnjava.service.QuizService;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizSubmissionResponse> submit(@RequestBody QuizSubmissionRequest request) {
        return ResponseEntity.ok(quizService.submitQuiz(request));
    }
}
