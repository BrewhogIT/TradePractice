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
    private boolean done;

    public Lesson(String topic) {
        this.topic = topic;
        lessonID = UUID.randomUUID();
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
