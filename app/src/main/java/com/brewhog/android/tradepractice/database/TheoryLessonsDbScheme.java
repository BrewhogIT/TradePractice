package com.brewhog.android.tradepractice.database;

public class TheoryLessonsDbScheme {

    public static final class TestTable{
        public static final String TABLE_NAME = "Test";

        public static final class Cols{
            public static final String LESSON_ID = "LessonID";
            public static final String QUESTION = "Question";
            public static final String ANSWER = "Answer";
            public static final String IS_TRUE = "isTrue";
        }
    }

    public static final class PageTable{
        public static final String TABLE_NAME = "Pages";

        public static final class Cols{
            public static final String LESSON_ID = "LessonID";
            public static final String PAGE_LINK = "PageLink";
        }
    }

    public static final class IllustrationTable{
        public static final String TABLE_NAME = "Illustrations";

        public static final class Cols{
            public static final String LESSON_ID = "LessonID";
            public static final String ILLUSTRATION_LINK = "IllustrationLink";
        }
    }

    public static final class LessonTable{
        public static final String TABLE_NAME = "Lessons";

        public static final class Cols{
            public static final String ID = "LessonID";
            public static final String TOPIC = "Topic";
            public static final String IS_DONE = "IsDone";
        }
    }
}
