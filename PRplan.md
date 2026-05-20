Quiz Submission Feature PR Plan

Scope

Implement only the backend feature required for quiz submission and persistence.
Do not add frontend/auth/extra APIs. Keep testing work (test classes, JMeter
artifacts, and deep validation scripts) for a follow-up PR.

Implementation Steps





Scaffold the Maven Spring Boot project structure





Create src/main/java/com/learnjava with app entrypoint and package
 folders.



Add dependencies and Java version in
[/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/pom.xml](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/pom.xml):
web, data-jpa, validation, h2(runtime), test(scope).



Define domain entities and repositories





Add entities in:





[/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/model/Lesson.java](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/model/Lesson.java)



[/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/model/Question.java](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/model/Question.java)



[/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/model/UserProgress.java](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/model/UserProgress.java)



Add repositories in repository/ with methods for:





lesson existence lookup



questions by lesson



progress lookup by (userId, lessonId)



completed lesson count for streak calculation



Define request/response DTO contract





Create request and nested answer DTOs plus success response/result item
 DTOs in:





[/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/dto/QuizSubmissionRequest.java](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/dto/QuizSubmissionRequest.java)



[/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/dto/AnswerDto.java](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/dto/AnswerDto.java)



[/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/dto/QuizSubmissionResponse.java](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/dto/QuizSubmissionResponse.java)



Keep field names aligned to expected JSON shape in CLAUDE.md.



Implement service logic in required order





Build submitQuiz() in
 [/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/service/QuizService.java](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/service/QuizService.java)
 to:





validate lesson (404)



validate exactly 5 answers (400)



fetch questions from DB



evaluate correctness and score



compute pass/fail and next unlock



upsert user progress by (userId, lessonId)



compute streak as user-level completed-lesson count



save progress and return full structured response with break
prompt/message



Expose endpoint + error handling





Implement controller in
 [/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/controller/QuizController.java](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/java/com/learnjava/controller/QuizController.java)
 for POST /api/quiz/submit.



Add centralized exception handling (e.g., @RestControllerAdvice) to
guarantee:





404 for missing lesson



400 for invalid answer count/validation



JSON error body with error and message



Configure persistence + seed realistic data





Add DB config in
 [/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/resources/application.properties](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/resources/application.properties)
 exactly as specified (H2 in-memory, create-drop, SQL init always, H2
 console enabled).



Add
[/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/resources/data.sql](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/src/main/resources/data.sql)
with 3 lessons x 5 questions each on Java OOP topics.



Align documentation for handoff





Replace repo README with assignment-specific usage in
 [/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/README.md](/Users/sohamkulkarni/Desktop/Learn-Java-quiz-sumbission/README.md)
 (<=100 words, endpoint + run command + H2 console).

PR Deliverable Boundaries





Include: complete implementation and runnable app for manual POST submission.



Exclude for this PR: JMeter files, dedicated unit/integration test classes,
performance scripting, CI test tuning.

Request Flow

flowchart TD
client[ClientPostQuiz] --> controller[QuizController]
controller --> service[QuizServiceSubmitQuiz]
service --> lessonRepo[LessonRepository]
service --> questionRepo[QuestionRepository]
service --> progressRepo[UserProgressRepository]
progressRepo --> service
service --> responseDto[QuizSubmissionResponse]
responseDto --> client

Suggested PR Structure





Commit 1: project scaffold + dependencies + config.



Commit 2: entities/repositories/dtos.



Commit 3: service logic + controller + exception handling.



Commit 4: seed data + README polish.
