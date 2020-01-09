package com.brewhog.android.tradepractice;

import android.graphics.PointF;

public class SupportLine {
    private PointF startPoint;
    private PointF currentPoint;

    public SupportLine(PointF startPoint) {
        this.startPoint = startPoint;
    }

    public PointF getStartPoint() {
        return startPoint;
    }

    public PointF getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(PointF currentPoint) {
        this.currentPoint = currentPoint;
    }
}
