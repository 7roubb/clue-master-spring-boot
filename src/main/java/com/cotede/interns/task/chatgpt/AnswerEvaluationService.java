package com.cotede.interns.task.chatgpt;

import com.cotede.interns.task.cases.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerEvaluationService {

    private final ChatGPTService chatGPTService;

    public Integer evaluateAnswer(Answer answer, String expectedAnswer) {
        Optional<Double> evaluationScore = chatGPTService.compareAnswers(answer.getAnswer(), expectedAnswer);
        return evaluationScore.map(score -> (int) (score * 100)).orElse(0);
    }
}
