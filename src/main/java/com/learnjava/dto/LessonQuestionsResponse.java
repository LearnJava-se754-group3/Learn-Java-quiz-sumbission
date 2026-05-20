package com.learnjava.dto;

import java.util.List;

public class LessonQuestionsResponse {

    private Long lessonId;
    private String title;
    private String concept;
    private List<LessonQuestionDto> questions;

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public List<LessonQuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<LessonQuestionDto> questions) {
        this.questions = questions;
    }
}
