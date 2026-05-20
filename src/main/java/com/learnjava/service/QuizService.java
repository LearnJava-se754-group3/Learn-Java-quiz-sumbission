package com.learnjava.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnjava.dto.AnswerDto;
import com.learnjava.dto.QuizResultItem;
import com.learnjava.dto.QuizSubmissionRequest;
import com.learnjava.dto.QuizSubmissionResponse;
import com.learnjava.exception.InvalidSubmissionException;
import com.learnjava.exception.LessonNotFoundException;
import com.learnjava.model.Question;
import com.learnjava.model.UserProgress;
import com.learnjava.repository.LessonRepository;
import com.learnjava.repository.QuestionRepository;
import com.learnjava.repository.UserProgressRepository;

@Service
public class QuizService {
    private static final String INVALID_SUBMISSION_MESSAGE =
            "Lesson not found or incorrect number of answers submitted.";

    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final UserProgressRepository userProgressRepository;

    public QuizService(LessonRepository lessonRepository,
                       QuestionRepository questionRepository,
                       UserProgressRepository userProgressRepository) {
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
        this.userProgressRepository = userProgressRepository;
    }

    @Transactional
    public QuizSubmissionResponse submitQuiz(QuizSubmissionRequest request) {
        validateRequest(request);
        if (!lessonRepository.existsById(request.getLessonId())) {
            throw new LessonNotFoundException("Lesson not found: " + request.getLessonId());
        }

        List<Question> questions = fetchAndValidateLessonQuestions(request.getLessonId());
        Map<Long, String> answerMap = buildAndValidateAnswerMap(request.getAnswers(), questions);

        List<QuizResultItem> results = new ArrayList<>();
        int score = 0;
        for (Question question : questions) {
            String selected = answerMap.getOrDefault(question.getId(), "");
            boolean correct = question.getCorrectOption().equalsIgnoreCase(selected);
            if (correct) score++;
            results.add(new QuizResultItem(question.getId(), correct, question.getCorrectOption()));
        }

        int total = questions.size();
        double percentage = total > 0 ? Math.round((score * 100.0 / total) * 10.0) / 10.0 : 0.0;
        boolean passed = score >= 3;

        UserProgress progress = userProgressRepository
                .findByUserIdAndLessonId(request.getUserId(), request.getLessonId())
                .orElseGet(() -> {
                    UserProgress p = new UserProgress();
                    p.setUserId(request.getUserId());
                    p.setLessonId(request.getLessonId());
                    return p;
                });

        progress.setCompleted(true);
        progress.setScore(score);
        progress.setLastCompletedAt(LocalDateTime.now());
        userProgressRepository.save(progress);

        // streak = total completed lessons across all lessons for this user
        int streak = (int) userProgressRepository.countByUserIdAndCompletedTrue(request.getUserId());
        progress.setStreak(streak);
        userProgressRepository.save(progress);

        Long nextLessonUnlocked = passed ? request.getLessonId() + 1 : request.getLessonId();
        boolean breakPromptTriggered = streak % 2 == 0;
        String message = breakPromptTriggered
                ? "Great work! Take a 5-minute break before your next lesson."
                : "Keep going!";

        QuizSubmissionResponse response = new QuizSubmissionResponse();
        response.setLessonId(request.getLessonId());
        response.setUserId(request.getUserId());
        response.setScore(score);
        response.setTotal(total);
        response.setPercentage(percentage);
        response.setPassed(passed);
        response.setNextLessonUnlocked(nextLessonUnlocked);
        response.setBreakPromptTriggered(breakPromptTriggered);
        response.setStreakCount(streak);
        response.setMessage(message);
        response.setResults(results);

        return response;
    }

    private void validateRequest(QuizSubmissionRequest request) {
        if (request == null || request.getLessonId() == null || request.getUserId() == null) {
            throw new InvalidSubmissionException(INVALID_SUBMISSION_MESSAGE);
        }
        if (request.getAnswers() == null || request.getAnswers().size() != 5) {
            throw new InvalidSubmissionException(INVALID_SUBMISSION_MESSAGE);
        }
    }

    private List<Question> fetchAndValidateLessonQuestions(Long lessonId) {
        List<Question> questions = questionRepository.findByLessonId(lessonId);
        if (questions.size() != 5) {
            throw new InvalidSubmissionException(INVALID_SUBMISSION_MESSAGE);
        }
        return questions;
    }

    private Map<Long, String> buildAndValidateAnswerMap(List<AnswerDto> answers, List<Question> questions) {
        Map<Long, String> answerMap = new HashMap<>();
        Set<Long> submittedQuestionIds = new HashSet<>();
        for (AnswerDto answer : answers) {
            if (answer == null || answer.getQuestionId() == null || answer.getSelectedOption() == null) {
                throw new InvalidSubmissionException(INVALID_SUBMISSION_MESSAGE);
            }
            if (!submittedQuestionIds.add(answer.getQuestionId())) {
                throw new InvalidSubmissionException(INVALID_SUBMISSION_MESSAGE);
            }
            answerMap.put(answer.getQuestionId(), answer.getSelectedOption());
        }

        Set<Long> lessonQuestionIds = questions.stream().map(Question::getId).collect(Collectors.toSet());
        if (!submittedQuestionIds.equals(lessonQuestionIds)) {
            throw new InvalidSubmissionException(INVALID_SUBMISSION_MESSAGE);
        }
        return answerMap;
    }
}
