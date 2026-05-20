package com.learnjava.dto;

import java.util.List;

public class QuizSubmissionResponse {

    private Long lessonId;
    private String userId;
    private int score;
    private int total;
    private double percentage;
    private boolean passed;
    private Long nextLessonUnlocked;
    private boolean breakPromptTriggered;
    private int streakCount;
    private String message;
    private List<QuizResultItem> results;

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public Long getNextLessonUnlocked() { return nextLessonUnlocked; }
    public void setNextLessonUnlocked(Long nextLessonUnlocked) { this.nextLessonUnlocked = nextLessonUnlocked; }

    public boolean isBreakPromptTriggered() { return breakPromptTriggered; }
    public void setBreakPromptTriggered(boolean breakPromptTriggered) { this.breakPromptTriggered = breakPromptTriggered; }

    public int getStreakCount() { return streakCount; }
    public void setStreakCount(int streakCount) { this.streakCount = streakCount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<QuizResultItem> getResults() { return results; }
    public void setResults(List<QuizResultItem> results) { this.results = results; }
}
