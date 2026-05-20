package com.learnjava.dto;

import java.util.List;

public class QuizSubmissionRequest {

    private Long lessonId;
    private String userId;
    private List<AnswerDto> answers;

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<AnswerDto> getAnswers() { return answers; }
    public void setAnswers(List<AnswerDto> answers) { this.answers = answers; }
}
