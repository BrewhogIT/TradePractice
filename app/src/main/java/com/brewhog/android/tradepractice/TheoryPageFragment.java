package com.brewhog.android.tradepractice;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TheoryPageFragment extends SupportFragment {
    private static final String TAG = "TheoryPageFragment";
    private static final String ARG_LESSON_ID = "lesson_id";
    private static final String ARG_PAGE_NUMBER = "page_number";
    private WebView lessonContentView;
    private ImageView pageIllustrationView;
    private Lesson mLesson;
    private CallBack mCallBack;
    private int lessonID;
    private int pageNumber;

    public static TheoryPageFragment newInstance(int lessonID, int pageNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_LESSON_ID,lessonID);
        args.putInt(ARG_PAGE_NUMBER,pageNumber);

        TheoryPageFragment fragment = new TheoryPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface CallBack{
        void setStartPostTransition();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallBack = (CallBack)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_of_theory,container,false);

        lessonID = getArguments().getInt(ARG_LESSON_ID);
        pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);

        mLesson = LessonPack.getLessonPack(getActivity()).getLesson(lessonID);
        String contentPath = mLesson.getPages().get(pageNumber);


        String illustrationPath = mLesson.getIllustrationPaths().get(pageNumber);
        Drawable illustrationDrawable = null;
        try {
            illustrationDrawable = getDrawable(illustrationPath);
        } catch (IOException e) {
            Log.e(TAG,"error with load drawable");
        }

        pageIllustrationView = view.findViewById(R.id.page_illustration);
        pageIllustrationView.setImageDrawable(illustrationDrawable);
        prepareForAnimationTransition();

        String data = fetchHTML(contentPath);
        lessonContentView = view.findViewById(R.id.page_content);
        WebSettings settings = lessonContentView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        lessonContentView.setWebViewClient(new WebViewClient());
        lessonContentView.setBackgroundColor(Color.TRANSPARENT);
        lessonContentView.loadDataWithBaseURL(
                null,
                data,
                "text/html; charset=utf-8",
                "utf-8",
                null);

        Animation showAnimation = AnimationUtils
                .loadAnimation(getActivity(),R.anim.item_animation_scale);
        lessonContentView.setAnimation(showAnimation);

        return view;
    }

    public String fetchHTML(String fileName) {
        StringBuilder sb = new StringBuilder();
        String tmpStr = "";
        try {
            InputStream in = getActivity().getAssets().open(fileName);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((tmpStr = bfr.readLine()) != null) {
                sb.append(tmpStr);
            }
            in.close();
        } catch (IOException e) {
            Log.e(TAG,"error in lesson file reading");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void prepareForAnimationTransition() {
        //Назначаем имя анимированных переходов для первой страницы
        if (pageNumber == 0){
            pageIllustrationView.setTransitionName(
                    getActivity().getString(R.string.lesson_preview_transition_name,lessonID));
        }

        //Возобновляем анимацию остановленную в активности пейджера по готовности ImageView
        pageIllustrationView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                pageIllustrationView.getViewTreeObserver().removeOnPreDrawListener(this);
                mCallBack.setStartPostTransition();
                pageIllustrationView.setTransitionName(null);

                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
