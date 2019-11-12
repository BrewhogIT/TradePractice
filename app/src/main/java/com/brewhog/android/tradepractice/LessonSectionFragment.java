package com.brewhog.android.tradepractice;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LessonSectionFragment extends Fragment {
    private static final String ARG_LESSON_KIND = "LessonKind";
    private int LessonKind;

    public static LessonSectionFragment newInstance(int lessonKind) {
        Bundle args = new Bundle();
        args.putInt(ARG_LESSON_KIND,lessonKind);

        LessonSectionFragment fragment = new LessonSectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lesson_section,container,false);
        ImageView logoView = view.findViewById(R.id.lesson_kind_logo);
        Drawable icon = null;

        LessonKind = getArguments().getInt(ARG_LESSON_KIND);
        switch (LessonKind){
            case R.string.practice:
                icon = getResources().getDrawable(R.drawable.practice);
                break;
            case  R.string.theory:
                icon = getResources().getDrawable(R.drawable.theory);
                break;
            case R.string.how_to_use:
                icon = getResources().getDrawable(R.drawable.howtouse);
                break;
        }

        logoView.setImageDrawable(icon);

        return view;

    }
}
