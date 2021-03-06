package com.brewhog.android.tradepractice.model;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private int lessonID;
    private String topic;
    private List<String> pages;
    private List<String> illustrations;
    private List<Test> lessonTest;
    private int correctAnswersCount;
    private boolean done;
    private int percentOfCorrectAnswer;

    public Lesson(int id,String topic){
        lessonID = id;
        correctAnswersCount = 0;
        this.topic = topic;
    }

    public int getLessonID() {
        return lessonID;
    }

    public String getTopic() {
        return topic;
    }

    public String getPreviewPath() {
        if (!isDone()){
            return illustrations.get(0);
        }else{
            return illustrations.get(illustrations.size() - 1);
        }
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = new ArrayList<>(pages);
    }

    public List<String> getIllustrationPaths() {
        return illustrations;
    }

    public void setIllustrationPaths(List<String> illustrations) {
        this.illustrations = new ArrayList<>(illustrations);
    }

    public List<Test> getLessonTest() {
        return lessonTest;
    }

    public void setLessonTest(List<Test> lessonTest) {
        this.lessonTest = lessonTest;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public void setCorrectAnswersCount(int correctAnswersCount) {
        this.correctAnswersCount = correctAnswersCount;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPercentOfCorrectAnswer() {
        return percentOfCorrectAnswer;
    }

    private void setPercentOfCorrectAnswer(int percentOfCorrectAnswer) {
        this.percentOfCorrectAnswer = percentOfCorrectAnswer;
    }

    public boolean checkTest(){
        int correctAnswers = getCorrectAnswersCount();
        int questionsCount = getLessonTest().size();
        float passIndex = (float)(1.0 * correctAnswers  / questionsCount);
        boolean isTestPassed = passIndex >= 0.8;

        setPercentOfCorrectAnswer(Math.round(passIndex * 100));
        setCorrectAnswersCount(0);
        return  isTestPassed;
    }
}
