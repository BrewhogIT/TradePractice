package com.brewhog.android.tradepractice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.brewhog.android.tradepractice.R;
import com.brewhog.android.tradepractice.database.TheoryLessonsDbScheme.LessonTable;

import java.util.UUID;

import androidx.annotation.Nullable;

public class TheoryLessonHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "theoryLessonBase.db";
    private static final int VERSION = 1;
    private Context mContext;

    public TheoryLessonHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + LessonTable.TABLE_NAME + "(" +
                " _id integer primary key autoincrement, " +
                LessonTable.Cols.ID + ", " +
                LessonTable.Cols.TOPIC + ", " +
                LessonTable.Cols.IS_DONE + ")"
                );

        addAllLesson(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void addLesson(SQLiteDatabase dataBase, String topic,int id){
        ContentValues values = new ContentValues();
        values.put(LessonTable.Cols.ID, id);
        values.put(LessonTable.Cols.IS_DONE,0);
        values.put(LessonTable.Cols.TOPIC,topic);

        dataBase.insert(LessonTable.TABLE_NAME,null,values);
    }

    private void addAllLesson(SQLiteDatabase dataBase){
        String[]lessons = mContext.getResources().getStringArray(R.array.lessons);
        for (int i = 0; i < lessons.length; i++){
            String topic = lessons[i];
            addLesson(dataBase,topic,i);
        }
    }
}
