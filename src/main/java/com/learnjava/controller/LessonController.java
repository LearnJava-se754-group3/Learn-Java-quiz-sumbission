package com.learnjava.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnjava.dto.LessonQuestionsResponse;
import com.learnjava.service.QuizService;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final QuizService quizService;

    public LessonController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{lessonId}/questions")
    public ResponseEntity<LessonQuestionsResponse> getLessonQuestions(@PathVariable Long lessonId) {
        return ResponseEntity.ok(quizService.getLessonQuestions(lessonId));
    }
}
