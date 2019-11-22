package com.brewhog.android.tradepractice;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lesson {
    private UUID lessonID;
    private String topic;
    private List<String> pages;
    private List<Drawable> illustrations;
    private List<Test> lessonTest;
    private int correctAnswersCount;
    private boolean done;

    public Lesson(String topic) {
        this.topic = topic;
        lessonID = UUID.randomUUID();
        correctAnswersCount = 0;
    }

    public UUID getLessonID() {
        return lessonID;
    }

    public String getTopic() {
        return topic;
    }

    public Drawable getPreview() {
        return illustrations.get(0);
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = new ArrayList<>(pages);
    }

    public List<Drawable> getIllustrations() {
        return illustrations;
    }

    public void setIllustrations(List<Drawable> illustrations) {
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
}
