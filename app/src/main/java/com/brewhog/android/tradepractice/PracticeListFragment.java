package com.brewhog.android.tradepractice;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PracticeListFragment extends Fragment {
    private ImageView lessonKindIllustration;
    private RecyclerView practiceListRecyclerView;

    public static final String IMAGE_RES_ID_ARGS = "Resource id for lesson kind logo";

    public static PracticeListFragment newInstance(int imageResID) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RES_ID_ARGS,imageResID);

        PracticeListFragment fragment = new PracticeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice_list,container,false);

        int imageResId = getArguments().getInt(IMAGE_RES_ID_ARGS);
        lessonKindIllustration = view.findViewById(R.id.practice_lesson_large_illustration);
        lessonKindIllustration.setImageResource(imageResId);

        practiceListRecyclerView = view.findViewById(R.id.practice_recycler_view);

        return view;
    }
}
