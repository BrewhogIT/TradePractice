package com.brewhog.android.tradepractice.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.brewhog.android.tradepractice.R;
import com.brewhog.android.tradepractice.database.LessonCursorWrapper;
import com.brewhog.android.tradepractice.database.TheoryLessonHelper;
import com.brewhog.android.tradepractice.database.TheoryLessonsDbScheme.LessonTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LessonPack {
    public static final String TAG = "LessonPack";
    public static final String MAIN_LESSONS_FOLDER = "lessons";

    private static LessonPack mLessonPack;
    private List<Lesson> mLessonsList;
    private Context mContext;
    private AssetManager mAssetManager;
    private SQLiteDatabase mDataBase;
    private HashMap<String,Integer> signalsMap;

    private LessonPack(Context context) {
        mContext = context;
        mDataBase = new TheoryLessonHelper(context).getWritableDatabase();
        mAssetManager = mContext.getAssets();
        loadLessonList();
        loadSignalMap();
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

    public Lesson getLesson(int lessonID){
        Lesson mLesson = null;
        for (Lesson lesson: getLessons()){
            if (lesson.getLessonID() == lessonID){
                mLesson = lesson;
            }
        }
        return mLesson;
    }

    private void loadLessonList() {
        /*В строковом файле присутствует строковый массив с темами уроков, при загрузке новой папки
        урока, необходимо добавить строку с темой */
        try {
            mLessonsList = new ArrayList<>();
            String[] allLessons = mAssetManager.list(MAIN_LESSONS_FOLDER);

            if (allLessons != null){
                LessonCursorWrapper cursorWrapper = getCursor(null,null);
                cursorWrapper.moveToFirst();
                int i = 0;
                while (!cursorWrapper.isAfterLast()){
                    Lesson theoryLesson  = cursorWrapper.getLesson();

                    String lessonFolder = MAIN_LESSONS_FOLDER + "/" + allLessons[i];
                    String illustrationsFolder = lessonFolder + "/illustrations";
                    String pagesFolder = lessonFolder + "/pages";
                    String testsFolder = lessonFolder + "/test";

                    String[] pageFileNames = mAssetManager.list(pagesFolder);
                    String[] illustrationFileNames = mAssetManager.list(illustrationsFolder);
                    String[] testFileNames = mAssetManager.list(testsFolder);

                    List<Test> tests = loadTests(testsFolder, testFileNames);
                    List<String> pages = new ArrayList<>();
                    List<String> illustrations = new ArrayList<>();

                    //Обход по папкам и добавление в список страниц, которые представляют из себя
                    //путь к html файлу (без корневой папки "file:///android_asset/") и картинок.
                    //Количество страниц отличается от количество изображений на 1, т.к. для
                    // preview используется 2 изображения - для нового и пройденого урока
                    for (int j = 0; j < illustrationFileNames.length; j++){
                        if ( j < pageFileNames.length){
                            pages.add(pagesFolder + "/" + pageFileNames[j]);
                        }

                        illustrations.add(illustrationsFolder + "/" + illustrationFileNames[j]);
                    }

                    theoryLesson.setLessonTest(tests);
                    theoryLesson.setPages(pages);
                    theoryLesson.setIllustrationPaths(illustrations);

                    mLessonsList.add(theoryLesson);
                    cursorWrapper.moveToNext();
                    i++;
                }
            }

        } catch (IOException e) {
            Log.e(TAG,"IO Exception while lessons loading");
            e.printStackTrace();
        }

    }

    private List<Test> loadTests(String lessonTestsFolder, String[] testsFileName) {
        List<Test> allTests = new ArrayList<>();
        for (int j = 0; j < testsFileName.length; j++){
            Test test = new Test();
            String testFilePath = lessonTestsFolder + "/" + testsFileName[j];

            //В текстовом файле теста в первой строке находится вопрос к тесту,
            //во второй правильный ответ, в последующих не корректные ответы
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(
                        new InputStreamReader(mAssetManager.open(testFilePath), StandardCharsets.UTF_8));
                String line;
                for (int k = 0; (line = reader.readLine()) != null; k++){
                    if(k == 0){
                        test.setQuestion(line);
                    }else{
                        boolean isTrue = false;
                        if (k == 1) isTrue = true;
                        test.addAnswer(line,isTrue);
                    }
                }
            }catch (IOException e){
                Log.e(TAG,"error while test file reading");
            }finally {
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            allTests.add(test);
        }
        return allTests;
    }

    private LessonCursorWrapper getCursor(String selection, String[] args){
        Cursor cursor = mDataBase.query(LessonTable.TABLE_NAME,
                null,
                selection,
                args,
                null,
                null,
                null);
        return new LessonCursorWrapper(cursor);
    }

    public void updateDataBase(Lesson lesson){
        Log.i(TAG,"lesson id: " + lesson.getLessonID() + " lesson is done: " + lesson.isDone());
        ContentValues values = new ContentValues();
        values.put(LessonTable.Cols.TOPIC,lesson.getTopic());
        values.put(LessonTable.Cols.IS_DONE,lesson.isDone()? 1:0);
        values.put(LessonTable.Cols.ID,lesson.getLessonID());

        mDataBase.update(LessonTable.TABLE_NAME,
                values,
                LessonTable.Cols.ID + " = ?",
                new String[]{Integer.toString(lesson.getLessonID())});
    }

    private void loadSignalMap(){
        signalsMap = new HashMap<>();
        String[]signalsArray = mContext.getResources().getStringArray(R.array.signals);
        signalsMap.put(signalsArray[0],9);
        signalsMap.put(signalsArray[1],8);
        signalsMap.put(signalsArray[2],8);
        signalsMap.put(signalsArray[3],7);
        signalsMap.put(signalsArray[4],7);
        signalsMap.put(signalsArray[5],7);
        signalsMap.put(signalsArray[6],7);
        signalsMap.put(signalsArray[7],7);
        signalsMap.put(signalsArray[8],10);
        signalsMap.put(signalsArray[9],10);
        signalsMap.put(signalsArray[10],11);
        signalsMap.put(signalsArray[11],14);
    }

    public Integer getLessonIdFromSignal(String signal){
        return signalsMap.get(signal);
    }
}
