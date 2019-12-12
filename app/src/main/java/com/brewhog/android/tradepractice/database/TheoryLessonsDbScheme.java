package com.brewhog.android.tradepractice.database;

public class TheoryLessonsDbScheme {

    public static final class LessonTable{
        public static final String TABLE_NAME = "Lessons";

        public static final class Cols{
            public static final String ID = "LessonID";
            public static final String TOPIC = "Topic";
            public static final String IS_DONE = "IsDone";
        }
    }
}
