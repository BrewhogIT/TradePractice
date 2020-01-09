package com.brewhog.android.tradepractice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class ChartPainterView extends View {
    private static final String TAG = "ChartPainter";
    private List<SupportLine> linesList = new ArrayList<>();
    private SupportLine mSupportLine = null;

    public ChartPainterView(Context context) {
        super(context);
    }

    public ChartPainterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"onTouchEvent is run");
        PointF currentPoint = new PointF(event.getX(), event.getY());

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"Action Down");

                mSupportLine = new SupportLine(currentPoint);
                linesList.add(mSupportLine);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"Action Move");

                mSupportLine.setCurrentPoint(currentPoint);
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG,"Action Cancel");

                mSupportLine = null;
                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG,"Action Up");

                mSupportLine = null;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG,"onDraw is run, linesList size is: " + linesList.size());
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(android.R.color.holo_red_light));
        paint.setStrokeWidth(5);

        for (SupportLine line : linesList){
            float left = line.getStartPoint().x;
            float right = line.getCurrentPoint().x;
            float bottom = line.getStartPoint().y;
            float top = line.getCurrentPoint().y;

            canvas.drawLine(left, bottom, right, top,paint);
        }

    }
}
