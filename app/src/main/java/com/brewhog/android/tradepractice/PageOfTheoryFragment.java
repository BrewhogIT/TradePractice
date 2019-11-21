package com.brewhog.android.tradepractice;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PageOfTheoryFragment extends Fragment {
    private static final String ARG_LESSON_ID = "lesson_id";
    private static final String ARG_PAGE_NUMBER = "page_number";
    private WebView lessonContentView;
    private ImageView pageIllustrationView;
    private Lesson mLesson;

    public static PageOfTheoryFragment newInstance(UUID lessonID, int pageNumber) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LESSON_ID,lessonID);
        args.putInt(ARG_PAGE_NUMBER,pageNumber);

        PageOfTheoryFragment fragment = new PageOfTheoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_of_theory,container,false);

        UUID lessonID = (UUID)getArguments().getSerializable(ARG_LESSON_ID);
        int pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);

        mLesson = LessonPack.getLessonPack(getActivity()).getLesson(lessonID);
        String contentPath = mLesson.getPages().get(pageNumber);
        Drawable illustrationDrawable = mLesson.getIllustrations().get(pageNumber);

        pageIllustrationView = view.findViewById(R.id.page_illustration);
        pageIllustrationView.setImageDrawable(illustrationDrawable);

        lessonContentView = view.findViewById(R.id.page_content);
        lessonContentView.getSettings().setJavaScriptEnabled(true);
        lessonContentView.setWebViewClient(new WebViewClient());
        lessonContentView.loadUrl(contentPath);

        return view;
    }
}
