package com.brewhog.android.tradepractice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TheoryPageFragment extends Fragment {
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
        Drawable illustrationDrawable = mLesson.getIllustrations().get(pageNumber);

        pageIllustrationView = view.findViewById(R.id.page_illustration);
        pageIllustrationView.setImageDrawable(illustrationDrawable);
        prepareForAnimationTransition();

        lessonContentView = view.findViewById(R.id.page_content);
        lessonContentView.getSettings().setJavaScriptEnabled(true);
        lessonContentView.setWebViewClient(new WebViewClient());
        lessonContentView.loadUrl(contentPath);

        Animation showAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.item_animation_scale);
        lessonContentView.setAnimation(showAnimation);

        return view;
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
