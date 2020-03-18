package com.brewhog.android.tradepractice.database;

import android.database.Cursor;

import com.brewhog.android.tradepractice.Lesson;
import com.brewhog.android.tradepractice.database.TheoryLessonsDbScheme.LessonTable;

public class LessonCursorWrapper extends android.database.CursorWrapper {
    public LessonCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Lesson getLesson(){
        int id = this.getInt(this.getColumnIndex(LessonTable.Cols.ID));
        String topic = this.getString(this.getColumnIndex(LessonTable.Cols.TOPIC));
        boolean isDone = this.getInt(this.getColumnIndex(LessonTable.Cols.IS_DONE)) == 1;

        Lesson lesson = new Lesson(id,topic);
        lesson.setDone(isDone);
        return lesson;
    }
}
