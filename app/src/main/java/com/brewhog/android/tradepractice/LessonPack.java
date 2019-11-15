package com.brewhog.android.tradepractice;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class LessonPack {
    private static LessonPack mLessonPack;
    private List<Lesson> mLessonsList;
    private Context mContext;

    private LessonPack(Context context) {
        mContext = context;
        loadLessonList();
    }

    public static LessonPack getLessonPack(Context context){
        if(mLessonPack == null){
            mLessonPack = new LessonPack(context);
        }
        return mLessonPack;
    }

    public List<Lesson> getLessons() {
        return mLessonsList;
    }

    private void loadLessonList() {
        mLessonsList = new ArrayList<>();

        Drawable lesson1Preview = mContext.getResources().getDrawable(R.drawable.lesson1);
        Lesson theoryLesson1 = new Lesson(R.string.lesson1);
        theoryLesson1.setPreview(lesson1Preview);
        mLessonsList.add(theoryLesson1);

        Drawable lesson2Preview = mContext.getResources().getDrawable(R.drawable.lesson2);
        Lesson theoryLesson2 = new Lesson(R.string.lesson2);
        theoryLesson2.setPreview(lesson2Preview);
        mLessonsList.add(theoryLesson2);
    }
}
