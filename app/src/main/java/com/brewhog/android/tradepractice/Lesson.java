package com.brewhog.android.tradepractice;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Lesson {
    private UUID mUUID;
    private String topic;
    private List<String> pages;
    private List<Drawable> illustration;
    private boolean done;

    public Lesson(String topic) {
        this.topic = topic;
        mUUID = UUID.randomUUID();
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getTopic() {
        return topic;
    }

    public Drawable getPreview() {
        return illustration.get(0);
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = new ArrayList<>(pages);
    }

    public List<Drawable> getIllustration() {
        return illustration;
    }

    public void setIllustration(List<Drawable> illustration) {
        this.illustration = new ArrayList<>(illustration);
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
