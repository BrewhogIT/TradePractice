package com.brewhog.android.tradepractice;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
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
        урока, необходимо добавить строку */
        try {
            mLessonsList = new ArrayList<>();
            String[] allLessons = mAssetManager.list(MAIN_LESSONS_FOLDER);
            String[] lessonsTopics = mContext.getResources().getStringArray(R.array.lessons);

            if (allLessons != null){
                for (int i = 0; i < allLessons.length; i++){
                    String lessonFolder = MAIN_LESSONS_FOLDER + "/" + allLessons[i];
                    String lessonIllustrationFolder = lessonFolder + "/" + "illustrations";
                    String lessonPagesFolder = lessonFolder + "/" + "pages";
                    String lessonTestsFolder = lessonFolder + "/" + "test";

                    Lesson theoryLesson = new Lesson(lessonsTopics[i]);
                    String[] pageFileNames = mAssetManager.list(lessonPagesFolder);
                    String[] illustrationFileNames = mAssetManager.list(lessonIllustrationFolder);
                    List<String> pagePaths = new ArrayList<>();
                    List<Drawable> illustrations = new ArrayList<>();

                    for (int j = 0; j < pageFileNames.length; j++){
                        pagePaths.add("file:///android_asset/" + lessonPagesFolder + "/" + pageFileNames[j]);
                        InputStream inputStream = mAssetManager
                                .open(lessonIllustrationFolder + "/" + illustrationFileNames[j]);
                        illustrations.add(Drawable.createFromStream(inputStream,null));
                        inputStream.close();
                    }

                    //add parse test
                    String[]testsFileName = mAssetManager.list(lessonTestsFolder);
                    List<Test> tests = new ArrayList<>();
                    for (int j = 0; j < testsFileName.length; j++){
                        
                    }

                    theoryLesson.setPages(pagePaths);
                    theoryLesson.setIllustrations(illustrations);

                    mLessonsList.add(theoryLesson);
                }
            }

        } catch (IOException e) {
            Log.e(TAG,"IO Exception while lessons loading");
            e.printStackTrace();
        }

    }
}
