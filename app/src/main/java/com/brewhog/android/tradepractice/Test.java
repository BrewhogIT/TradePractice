package com.brewhog.android.tradepractice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    private String question;
    private Map<String,Boolean> answers;

    public Test() {
        this.answers = new HashMap<>();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, Boolean> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> Boolean) {
        this.answers = answers;
    }

    public void addAnswer(String answer, boolean isTrue){
        answers.put(answer,isTrue);
    }
}
