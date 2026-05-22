# LearnJava – Quiz Submission

Implements POST /api/quiz/submit — a REST endpoint that evaluates a learner's
5-question quiz for a Java OOP lesson. The backend validates answers, calculates
score, updates progress and streak, and determines whether a break prompt should
trigger.

Built with Spring Boot 3, Spring Data JPA, and H2 in-memory database.

## Running it

```bash
mvn spring-boot:run
```

Then POST to `http://localhost:8080/api/quiz/submit`.

H2 console is at `http://localhost:8080/h2-console` — connect with JDBC URL `jdbc:h2:mem:learnjavadb`, username `sa`, no password.

## Submitting a quiz

```bash
curl -X POST http://localhost:8080/api/quiz/submit \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user1",
    "lessonId": 1,
    "answers": [
      {"questionId": 1, "selectedOption": "A"},
      {"questionId": 2, "selectedOption": "B"},
      {"questionId": 3, "selectedOption": "C"},
      {"questionId": 4, "selectedOption": "A"},
      {"questionId": 5, "selectedOption": "D"}
    ]
  }'
```

Exactly 5 answers required, one per question in the lesson. Wrong lesson ID or missing fields returns 400.

## Performance testing

Start the app first (`mvn spring-boot:run`), then run JMeter headlessly from your JMeter `bin` directory:

**Windows**
```bat
jmeter.bat -n -t "path\to\repo\src\test\resources\performancetest\LearnJava_Performance_Test.jmx" -l "path\to\repo\src\test\resources\performancetest\results.csv"
```

**macOS / Linux**
```bash
./jmeter -n -t "path/to/repo/src/test/resources/performancetest/LearnJava_Performance_Test.jmx" -l "path/to/repo/src/test/resources/performancetest/results.csv"
```

Replace `path/to/repo` with the absolute path to this repository on your machine. Delete `results.csv` before re-running to avoid appending to old results.

To open the test plan in the JMeter GUI instead:
```bat
jmeter.bat
```
Then **File → Open** and select `src/test/resources/performancetest/LearnJava_Performance_Test.jmx`.

## What gets saved

Progress is stored in `USER_PROGRESS` — one row per user/lesson pair, updated on each submission. Streak is the total number of lessons a user has completed. Every even streak triggers a break prompt in the response.

```sql
SELECT * FROM USER_PROGRESS;
```

## Response shape

```json
{
  "userId": "user1",
  "lessonId": 1,
  "score": 3,
  "total": 5,
  "percentage": 60.0,
  "passed": true,
  "streakCount": 2,
  "breakPromptTriggered": true,
  "message": "Great work! Take a 5-minute break before your next lesson.",
  "nextLessonUnlocked": 2,
  "results": [...]
}
```

Pass threshold is 3/5. `nextLessonUnlocked` increments only on a pass.