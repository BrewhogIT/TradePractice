package com.brewhog.android.tradepractice;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TheoryListFragment extends Fragment {
    private RecyclerView theoryListRecyclerView;
    private LessonPack mLessonPack;
    private ImageView lessonKindIllustration;
    private ProgressBar userLevelProgress;
    private TheoryAdapter mAdapter;
    private ImageView levelIconView;
    private List<Drawable> levelIcons;

    public static final String IMAGE_RES_ID_ARGS = "Resource id for lesson kind logo";

    public static TheoryListFragment newInstance(int imageResId) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RES_ID_ARGS,imageResId);

        TheoryListFragment fragment = new TheoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theory_list,container,false);

        Drawable lessonKindImage = getResources()
                .getDrawable(getArguments().getInt(IMAGE_RES_ID_ARGS));
        lessonKindIllustration = view.findViewById(R.id.lesson_kind_large_illustration);
        lessonKindIllustration.setImageDrawable(lessonKindImage);

        theoryListRecyclerView = view.findViewById(R.id.theory_recycler_view);
        theoryListRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        levelIconView = view.findViewById(R.id.level_icon);
        userLevelProgress = view.findViewById(R.id.user_level_progressBar);
        levelIcons = loadLevelIcons();

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        mLessonPack = LessonPack.getLessonPack(getActivity());
        if (mAdapter == null){
            mAdapter = new TheoryAdapter(mLessonPack.getLessons());
            theoryListRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }

        int userLevel = UserPreferences.getUserLevel(getActivity());
        userLevelProgress.setMax(mAdapter.getItemCount());
        userLevelProgress.setProgress(userLevel);
        levelIconView.setImageDrawable(levelIcons.get(userLevel));

    }

    private List<Drawable> loadLevelIcons(){
        AssetManager manager = getActivity().getAssets();
        List<Drawable> iconsList = new ArrayList<>();
        try {
            String[] icons = manager.list("level_icons");

            for (int i = 0; i < icons.length; i++){
                InputStream inputStream = manager
                        .open("level_icons/" + icons[i]);
                iconsList.add(Drawable.createFromStream(inputStream,null));
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iconsList;
    }

    private class TheoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView lessonPreview;
        private TextView lessonTopic;
        private Lesson mLesson;

        public TheoryHolder(@NonNull View itemView) {
            super(itemView);
            lessonPreview = itemView.findViewById(R.id.lesson_preview);
            lessonTopic = itemView.findViewById(R.id.lesson_topic);
            itemView.setOnClickListener(this);
        }

        public void bind(Lesson lesson){
            mLesson = lesson;
            lessonPreview.setImageDrawable(mLesson.getPreview());
            lessonTopic.setText(mLesson.getTopic());
        }

        @Override
        public void onClick(View view) {
            UUID lessonID = mLesson.getLessonID();
            Intent intent = TheoryLessonPagerActivity.newIntent(getActivity(),lessonID);
            startActivity(intent);
        }
    }

    private class TheoryAdapter extends RecyclerView.Adapter<TheoryHolder>{

        List<Lesson> mLessons;

        public TheoryAdapter(List<Lesson> lessons) {
            mLessons = lessons;
        }

        @NonNull
        @Override
        public TheoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_theory, parent, false);
            TheoryHolder holder = new TheoryHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull TheoryHolder holder, int position) {
            Lesson lesson = mLessons.get(position);
            holder.bind(lesson);
        }

        @Override
        public int getItemCount() {
            return mLessons.size();
        }
    }
}
