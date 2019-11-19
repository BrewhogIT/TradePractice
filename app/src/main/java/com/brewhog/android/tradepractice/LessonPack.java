package com.brewhog.android.tradepractice;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    private void loadLessonList() {
        /*В строковом файле присутствует строковый массив с темами уроков, при загрузке новой папки
        урока, необходимо добавить строку */
        try {
            mLessonsList = new ArrayList<>();
            String[] assetsFolders = mAssetManager.list(MAIN_LESSONS_FOLDER);
            String[] lessonTopics = mContext.getResources().getStringArray(R.array.lessons);

            if (assetsFolders != null){
                for (int i = 0; i < assetsFolders.length; i++){
                    String lessonPreviewPath = MAIN_LESSONS_FOLDER + "/" + assetsFolders[i] + "/preview.png";
                    InputStream inputStream = mAssetManager.open(lessonPreviewPath);
                    Drawable lessonPreview = Drawable.createFromStream(inputStream,null);

                    Lesson theoryLesson = new Lesson(lessonTopics[i]);
                    theoryLesson.setPreview(lessonPreview);
                    mLessonsList.add(theoryLesson);

                    inputStream.close();
                }
            }

        } catch (IOException e) {
            Log.e(TAG,"IO Exception while lessons loading");
            e.printStackTrace();
        }

    }
}
