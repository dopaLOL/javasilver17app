package com.example.java;

import java.util.List;

public class QuestionData {
    private String qid;
    private String chapter;
    private String code;
    private String questionText;
    private List<String> choices;
    private List<Integer> answerIndex;
    private String explanation;

    public QuestionData(String qid, String chapter, String code, String questionText,
                        List<String> choices, List<Integer> answerIndex, String explanation) {
        this.qid = qid;
        this.chapter = chapter;
        this.code = code;
        this.questionText = questionText;
        this.choices = choices;
        this.answerIndex = answerIndex;
        this.explanation = explanation;
    }

    public String getQid() {
        return qid;
    }

    public String getChapter() {
        return chapter;
    }

    public String getCode() {
        return code;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getChoices() {
        return choices;
    }

    public List<Integer> getAnswerIndex() {
        return answerIndex;
    }

    public String getExplanation() {
        return explanation;
    }
}
