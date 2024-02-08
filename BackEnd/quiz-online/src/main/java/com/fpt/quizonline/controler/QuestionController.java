package com.fpt.quizonline.controler;

import com.fpt.quizonline.model.Question;
import com.fpt.quizonline.service.IQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/quizzes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QuestionController {

    private final IQuestionService questionService;

    @PostMapping("/create-new-question")
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question) {
        Question createQuestion = questionService.createQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(createQuestion);
    }

    @GetMapping("/all-questions")
    public ResponseEntity<List<Question>> getALlQuestion() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Optional<Question> question = questionService.getQuestionById(id);
        if (question.isPresent()) {
            return ResponseEntity.ok(question.get());
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @PutMapping("/question/{id}/update")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id,
            @RequestBody Question question) throws ChangeSetPersister.NotFoundException {
        Question updateQuestion = questionService.updateQuestion(id, question);
        return ResponseEntity.ok(updateQuestion);
    }

    @DeleteMapping("/question/{id}/delete")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getAllSubject() {
        List<String> subjects = questionService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/fetch-questions-for-user")
    public ResponseEntity<List<Question>> getQuestionForUser(
            @RequestParam Integer numOfQuestions, @RequestParam String subject) {
        List<Question> allQuestion = questionService.getQuestionForUser(numOfQuestions,subject);
        List<Question> mutableQuestions = new ArrayList<>(allQuestion);
        Collections.shuffle(mutableQuestions);

        int availQuestion = Math.min(numOfQuestions, mutableQuestions.size());
        List<Question> randomQuestions = mutableQuestions.subList(0, availQuestion);
        return ResponseEntity.ok(randomQuestions);
    }
}
