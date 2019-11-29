package com.brewhog.android.tradepractice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager implements TestPageFragment.PagingSetting {
    private boolean isPagingEnabled;

    public CustomViewPager(@NonNull Context context) {
        super(context);
        isPagingEnabled = true;
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        isPagingEnabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return  this.isPagingEnabled && super.onTouchEvent(ev);
    }

    @Override
    public void setPagingEnabled(boolean pagingEnabled) {
        isPagingEnabled = pagingEnabled;
    }

    @Override
    public void nextPage() {
        this.setCurrentItem(this.getCurrentItem()+1,true);
    }
}
