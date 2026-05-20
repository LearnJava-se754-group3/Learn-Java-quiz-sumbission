package com.learnjava.dto;

public class QuizResultItem {

    private Long questionId;
    private boolean correct;
    private String correctOption;

    public QuizResultItem(Long questionId, boolean correct, String correctOption) {
        this.questionId = questionId;
        this.correct = correct;
        this.correctOption = correctOption;
    }

    public Long getQuestionId() { return questionId; }
    public boolean isCorrect() { return correct; }
    public String getCorrectOption() { return correctOption; }
}
