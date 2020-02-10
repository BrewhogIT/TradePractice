package com.brewhog.android.tradepractice;

public class GuideItem {
    private int info;
    private int type;

    public GuideItem(int type) {
        this.type = type;
    }

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }
}
