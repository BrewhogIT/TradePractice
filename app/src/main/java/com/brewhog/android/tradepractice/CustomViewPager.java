package com.brewhog.android.tradepractice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager implements TestPageFragment.PagingSetting {
    private boolean isPagingEnabled;
    private ViewPager.OnPageChangeListener mPageChangeListener;

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
        mPageChangeListener.onPageScrollStateChanged(ViewPager.SCROLL_STATE_IDLE);
        this.setCurrentItem(this.getCurrentItem() + 1,true);
    }

    @Override
    public void addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
        mPageChangeListener = listener;
    }
}