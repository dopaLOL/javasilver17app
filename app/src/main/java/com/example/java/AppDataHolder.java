package com.example.java;

import java.util.List;

public class AppDataHolder {
    private static List<QuestionData> questionList;


    public static List<QuestionData> getQuestionList() {
        return questionList;
    }

    public static void setQuestionList(List<QuestionData> list) {
        questionList = list;
    }
}
