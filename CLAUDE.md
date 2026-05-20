# LearnJava – Quiz Submission Feature: Implementation Brief

## Context

LearnJava is a Java OOP learning application built for SOFTENG 754 Assignment 6.
The goal is to implement a **minimal but realistic** Spring Boot backend for a
quiz submission feature, sufficient to support Apache JMeter performance testing.

This is an academic assignment. The implementation does not need a frontend or
authentication. It must expose a working REST endpoint, perform real database
reads and writes, and return a structured JSON response.

---

## Assignment Constraint

The marker will assess:
- A working POST endpoint that can be hit by JMeter
- Real DB reads + writes per request (so load testing produces meaningful results)
- Clean, structured JSON responses with assertions JMeter can validate
- Code committed to GitHub under a standard Maven project structure

---

## Tech Stack

| Layer       | Choice              | Notes                                              |
|-------------|---------------------|----------------------------------------------------|
| Framework   | Spring Boot 3.x     | Use Spring Initializr structure                    |
| Database    | H2 (in-memory)      | No external DB needed; resets on restart           |
| ORM         | Spring Data JPA     | Standard repositories                              |
| Build       | Maven               | Standard pom.xml                                   |
| Java        | Java 17+            |                                                    |

---

## Project Structure

```
learnjava/
├── src/
│   ├── main/
│   │   ├── java/com/learnjava/
│   │   │   ├── LearnJavaApplication.java
│   │   │   ├── controller/
│   │   │   │   └── QuizController.java
│   │   │   ├── service/
│   │   │   │   └── QuizService.java
│   │   │   ├── model/
│   │   │   │   ├── Lesson.java
│   │   │   │   ├── Question.java
│   │   │   │   └── UserProgress.java
│   │   │   ├── repository/
│   │   │   │   ├── LessonRepository.java
│   │   │   │   ├── QuestionRepository.java
│   │   │   │   └── UserProgressRepository.java
│   │   │   └── dto/
│   │   │       ├── QuizSubmissionRequest.java
│   │   │       ├── AnswerDto.java
│   │   │       └── QuizSubmissionResponse.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       └── resources/
│           └── performancetest/
│               └── (JMeter .jmx and .csv files go here — do not create these)
├── pom.xml
└── README.md
```

---

## The Endpoint

### POST /api/quiz/submit

This is the **only endpoint** that needs to be implemented.

#### Request Body

```json
{
  "lessonId": 1,
  "userId": "user_42",
  "answers": [
    { "questionId": 1, "selectedOption": "B" },
    { "questionId": 2, "selectedOption": "A" },
    { "questionId": 3, "selectedOption": "C" },
    { "questionId": 4, "selectedOption": "D" },
    { "questionId": 5, "selectedOption": "B" }
  ]
}
```

- `lessonId`: which lesson's quiz is being submitted
- `userId`: string identifier for the learner (no auth required)
- `answers`: exactly 5 answers, one per question in the lesson

#### Response Body (success)

```json
{
  "lessonId": 1,
  "userId": "user_42",
  "score": 4,
  "total": 5,
  "percentage": 80.0,
  "passed": true,
  "nextLessonUnlocked": 2,
  "breakPromptTriggered": true,
  "streakCount": 3,
  "message": "Great work! Take a 5-minute break before your next lesson.",
  "results": [
    { "questionId": 1, "correct": true,  "correctOption": "B" },
    { "questionId": 2, "correct": true,  "correctOption": "A" },
    { "questionId": 3, "correct": false, "correctOption": "A" },
    { "questionId": 4, "correct": true,  "correctOption": "D" },
    { "questionId": 5, "correct": true,  "correctOption": "B" }
  ]
}
```

#### Response Body (validation error)

```json
{
  "error": "Invalid submission",
  "message": "Lesson not found or incorrect number of answers submitted."
}
```

HTTP status codes:
- `200 OK` on successful evaluation
- `400 Bad Request` on validation failure
- `404 Not Found` if lessonId does not exist

---

## Business Logic (QuizService)

Implement the following steps in order inside `QuizService.submitQuiz()`:

1. **Validate** lessonId exists — throw 404 if not
2. **Validate** exactly 5 answers are submitted — throw 400 if not
3. **Fetch** all 5 questions for the lessonId from the database
4. **Evaluate** each submitted answer against the correct option stored in the Question entity
5. **Calculate** score (count of correct answers) and percentage
6. **Determine** passed: score >= 3 out of 5 (60% pass threshold)
7. **Fetch** existing UserProgress for (userId, lessonId) — create new record if not found
8. **Update** UserProgress:
   - Set `completed = true`
   - Set `score = calculated score`
   - Update `lastCompletedAt = now`
   - Increment `streak` by 1
9. **Save** the updated UserProgress back to the database
10. **Determine** `nextLessonUnlocked`: if passed, return lessonId + 1, else return lessonId
11. **Determine** `breakPromptTriggered`: true if streak % 2 == 0 (every 2 completed lessons)
12. **Build and return** the full QuizSubmissionResponse

Steps 3, 7, and 9 are the DB operations that create realistic load under JMeter testing.

---

## Data Models

### Lesson
```java
@Entity
public class Lesson {
    @Id
    private Long id;
    private String title;
    private String concept;       // e.g. "OOP - Classes & Objects"
    private String difficulty;    // "beginner", "intermediate"
    private int moduleNumber;
    private int estimatedMinutes; // always 5 for digital wellbeing compliance
}
```

### Question
```java
@Entity
public class Question {
    @Id
    private Long id;
    private Long lessonId;
    private String text;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption; // "A", "B", "C", or "D"
}
```

### UserProgress
```java
@Entity
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private Long lessonId;
    private boolean completed;
    private int score;
    private int streak;
    private LocalDateTime lastCompletedAt;
}
```

---

## Seed Data (data.sql)

Seed **3 lessons**, each with exactly **5 questions**. All lessons are about Java OOP concepts.

### Lesson 1 — Classes & Objects (beginner)
### Lesson 2 — Inheritance (beginner)  
### Lesson 3 — Encapsulation (intermediate)

Write realistic Java OOP multiple-choice questions for each lesson.
Each question must have 4 options (A/B/C/D) and one correct answer.

Example question for Lesson 1:
- Q: "What keyword is used to create an object in Java?"
- A: define / B: new / C: create / D: object
- Correct: B

Seed data must use `INSERT INTO` statements compatible with H2.
Use `spring.jpa.hibernate.ddl-auto=create-drop` so schema is auto-generated,
then data.sql populates it.

---

## application.properties

```properties
spring.datasource.url=jdbc:h2:mem:learnjavadb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
spring.h2.console.enabled=true
server.port=8080
```

---

## pom.xml Dependencies

Include:
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `com.h2database:h2` (runtime scope)
- `spring-boot-starter-validation`
- `spring-boot-starter-test` (test scope)

Use Spring Boot parent version 3.2.x or later.

---

## README.md (max 100 words)

Write a README with the following content:

**LearnJava – Quiz Submission Feature**

Implements `POST /api/quiz/submit` — a REST endpoint that evaluates a learner's
5-question quiz for a Java OOP lesson. The backend validates answers, calculates
score, updates progress and streak, and determines whether a break prompt should
trigger. Built with Spring Boot 3, Spring Data JPA, and H2 in-memory database.

To run: `mvn spring-boot:run` then POST to `http://localhost:8080/api/quiz/submit`.
See H2 console at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:learnjavadb`).

---

## What NOT to implement

- No frontend / HTML
- No authentication or JWT
- No GET endpoints (unless a simple health check `/api/health` returning 200 is helpful)
- No Docker or external databases
- No additional endpoints beyond quiz submission

Keep it minimal. The endpoint just needs to work correctly and handle concurrent requests.

---

## Correctness Checks

After implementation, verify manually:

1. POST with all correct answers → score: 5, passed: true
2. POST with 2 correct answers → score: 2, passed: false, nextLessonUnlocked == lessonId
3. POST with invalid lessonId → HTTP 404
4. POST with only 3 answers → HTTP 400
5. POST same userId + lessonId twice → second submission updates the existing progress record (no duplicate rows)
6. H2 console shows UserProgress rows being created/updated after each submission

---

## Notes for the implementer

- UserProgress should use a composite lookup (userId + lessonId) to find existing records before saving
- The `streak` field on UserProgress should count total completed lessons for that user across all lessonIds, not per-lesson. Fetch count of completed lessons for the userId and use that as streak.
- Do not hardcode correct answers in the service — always fetch from the Question table
- All timestamps should use `LocalDateTime.now()`
- Return meaningful messages in the response: "Great work! Take a 5-minute break." when breakPromptTriggered is true, "Keep going!" otherwise

---

## Frontend UI (index.html)

Place a single static HTML file at `src/main/resources/static/index.html`.
Spring Boot serves static files from this directory automatically (no extra config needed).
The page is accessible at `http://localhost:8080`.

### What the UI must do

On page load:
- Show a lesson selector (dropdown or 3 buttons for Lesson 1, 2, 3)

On lesson select:
- Call `GET /api/lessons/{lessonId}/questions` and render:
  - Lesson title and concept
  - All 5 questions, each with 4 radio button options (A/B/C/D)
  - A `Submit Quiz` button (disabled until all 5 questions are answered)

On submit:
- Collect selected answers and call `POST /api/quiz/submit`
- Display:
  - Score (for example: `4 / 5`)
  - Pass or Fail banner
  - Per-question breakdown (correct ✓ / incorrect ✗ with the correct answer shown)
  - Break prompt message if `breakPromptTriggered` is true, displayed prominently
    - Example highlighted box: `🌿 Take a 5-minute break before your next lesson.`
  - A `Try Another Lesson` button to reset and pick a new lesson

### UI requirements

- Plain HTML + CSS + vanilla JavaScript only (no React, npm, or build step)
- No external CDN dependencies (must work fully offline)
- Generate a random `userId` on page load:
  - `user_` + `Math.random().toString(36).substr(2, 9)`
  - Reuse this ID for the current session
  - Display it subtly on the page so the marker can see it
- Use LearnJava green (`#2ecc71`) as the primary colour
- The break prompt box must be visually distinct (green background and bold text) as the Digital Wellbeing feature

### What the UI does not need

- No login screen
- No persistent user accounts
- No lesson teaching content (only quiz questions and results)
- No animations beyond basic CSS transitions
