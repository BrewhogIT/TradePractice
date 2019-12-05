package com.brewhog.android.tradepractice;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LessonPack {
    public static final String TAG = "LessonPack";
    public static final String MAIN_LESSONS_FOLDER = "lessons";

    private static LessonPack mLessonPack;
    private List<Lesson> mLessonsList;
    private Context mContext;
    private AssetManager mAssetManager;

    private LessonPack(Context context) {
        mContext = context;
        mAssetManager = mContext.getAssets();
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

    public Lesson getLesson(UUID lessonID){
        Lesson mLesson = null;
        for (Lesson lesson: getLessons()){
            if (lesson.getLessonID().equals(lessonID)){
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
            String[] lessonsTopics = mContext.getResources().getStringArray(R.array.lessons);

            if (allLessons != null){
                for (int i = 0; i < allLessons.length; i++){
                    Lesson theoryLesson = new Lesson(lessonsTopics[i]);

                    String lessonFolder = MAIN_LESSONS_FOLDER + "/" + allLessons[i];
                    String illustrationsFolder = lessonFolder + "/" + "illustrations";
                    String pagesFolder = lessonFolder + "/" + "pages";
                    String testsFolder = lessonFolder + "/" + "test";

                    String[] pageFileNames = mAssetManager.list(pagesFolder);
                    String[] illustrationFileNames = mAssetManager.list(illustrationsFolder);
                    String[] testFileNames = mAssetManager.list(testsFolder);

                    List<Test> tests = loadTests(testsFolder, testFileNames);
                    List<String> pages = new ArrayList<>();
                    List<Drawable> illustrations = new ArrayList<>();
                    //Обход по папкам и добавление в список картинок и
                    //страниц, которые представляют из себя путь к html файлу.
                    //Количество страниц отличается от количество изображений на 1, т.к. для
                    // preview используется 2 изображения - для нового и пройденого урока
                    for (int j = 0; j < illustrationFileNames.length; j++){
                        if ( j < pageFileNames.length){
                            pages.add("file:///android_asset/" + pagesFolder + "/" + pageFileNames[j]);
                        }

                        InputStream inputStream = mAssetManager
                                .open(illustrationsFolder + "/" + illustrationFileNames[j]);
                        illustrations.add(Drawable.createFromStream(inputStream,null));
                        inputStream.close();
                    }

                    theoryLesson.setLessonTest(tests);
                    theoryLesson.setPages(pages);
                    theoryLesson.setIllustrations(illustrations);

                    mLessonsList.add(theoryLesson);
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
                        new InputStreamReader(mAssetManager.open(testFilePath)));
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
}
