package com.brewhog.android.tradepractice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;

public class ChartPainterImageView extends AppCompatImageView {
    private static final String TAG = "ChartPainterImageView";
    private List<SupportLine> linesList = new ArrayList<>();
    private SupportLine mSupportLine = null;
    private Paint mPaint;
    private boolean drawEnable;

    public ChartPainterImageView(Context context) {
        super(context);
    }

    public ChartPainterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawEnable = true;

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
        mPaint.setStrokeWidth(5);

    }

    public boolean isDrawEnable() {
        return drawEnable;
    }

    public void setDrawEnable(boolean drawEnable) {
        this.drawEnable = drawEnable;
    }

    public void clearChart(){
        linesList.clear();
        invalidate();
    }

    public void undoLastDraw(){
        if (linesList.size() > 0){
            int last = linesList.size() - 1;
            linesList.remove(last);
            invalidate();
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //отрисовка линий при обновлении представления
        Log.i(TAG,"onDraw is run, linesList size is: " + linesList.size());

        for (SupportLine line : linesList){
            float left = line.getStartPoint().x;
            float right = line.getCurrentPoint().x;
            float bottom = line.getStartPoint().y;
            float top = line.getCurrentPoint().y;

            canvas.drawLine(left, bottom, right, top, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //создает объекты линий при взаимодействии с экраном
        // обновляет представление для их отрисовки на экране
        if (drawEnable) {
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
                    performClick();
                    break;
            }
        }
        return true;
    }
}
