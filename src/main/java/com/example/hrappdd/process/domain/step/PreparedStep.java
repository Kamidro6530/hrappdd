package com.example.hrappdd.process.domain.step;

import com.example.hrappdd.process.domain.Question;

import java.util.List;

public class PreparedStep {
    private final List<Question> questions;
    private final Step step;

    public PreparedStep(List<Question> questions, Step step) {

        if (questions.size() < 1) {
            throw new IllegalStateException();
        }

        this.questions = questions;
        this.step = step;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Step getStep() {
        return step;
    }
}