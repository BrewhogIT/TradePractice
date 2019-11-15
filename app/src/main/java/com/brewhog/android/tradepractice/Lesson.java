package com.brewhog.android.tradepractice;

import android.graphics.drawable.Drawable;

import java.util.List;
import java.util.Map;

public class Lesson {
    private int topic;
    private Drawable preview;
    private List<String> pages;
    private List<Drawable> illustration;
    private boolean done;

    public Lesson(int topic) {
        this.topic = topic;
    }

    public int getTopic() {
        return topic;
    }

    public Drawable getPreview() {
        return preview;
    }

    public void setPreview(Drawable preview) {
        this.preview = preview;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public List<Drawable> getIllustration() {
        return illustration;
    }

    public void setIllustration(List<Drawable> illustration) {
        this.illustration = illustration;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
