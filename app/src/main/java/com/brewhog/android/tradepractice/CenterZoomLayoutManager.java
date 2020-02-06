package com.brewhog.android.tradepractice;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLayoutManager extends LinearLayoutManager {

    private final float mShrinkAlpha = 0.9f;
    private final float mShrinkAmount = 0.15f;
    private final float mShrinkDistance = 0.9f;
    private static final String TAG = "CenterZoomLayoutManager";

    public CenterZoomLayoutManager(Context context) {
        super(context);
    }

    public CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if(orientation == HORIZONTAL){
            int scrolled = super.scrollHorizontallyBy(dx,recycler,state);

            float midpoint = getWidth() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            float a1 = 1.f - mShrinkAlpha;

            for (int i = 0; i < getChildCount(); i++){
                View child = getChildAt(i);
                TextView topic = child.findViewById(R.id.lesson_kind_topic);

                float childMidpoint =
                        (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);

                float alpha = s0 + (a1 - s0) * (d - d0) / (d1 - d0);
                topic.setAlpha(alpha);
            }
            return scrolled;
        }else {
            return 0;
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == RecyclerView.VERTICAL){
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);

            float midpoint = getHeight() / 2.f;
            float d0 = 0.f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;
            float a1 = 1.f - mShrinkAlpha;

            for (int i = 0; i < getChildCount(); i++){
                View child = getChildAt(i);
                TextView topic = child.findViewById(R.id.lesson_kind_topic);

                float childMidpoint =
                        (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);

                float alpha = s0 + (a1 - s0) * (d - d0) / (d1 - d0);
                topic.setAlpha(alpha);
            }

            return scrolled;
        }else {
            return 0;
        }
    }
}
