package com.brewhog.android.tradepractice;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
    private CardView levelFrame;

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

        int imageResId = getArguments().getInt(IMAGE_RES_ID_ARGS);
        lessonKindIllustration = view.findViewById(R.id.theory_lesson_large_illustration);
        lessonKindIllustration.setImageResource(imageResId);

        theoryListRecyclerView = view.findViewById(R.id.theory_recycler_view);
        theoryListRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        levelFrame = view.findViewById(R.id.users_level_view);
        levelIconView = view.findViewById(R.id.level_icon);
        userLevelProgress = view.findViewById(R.id.user_level_progressBar);
        levelIcons = loadLevelIcons();

        updateUI();
        welcomeAnimation();

        return view;
    }

    private void welcomeAnimation() {
        LayoutAnimationController animationController = AnimationUtils
                .loadLayoutAnimation(getActivity(),R.anim.layout_animation_fall_down);
        theoryListRecyclerView.setLayoutAnimation(animationController);

        Animation slideRightAnimation = AnimationUtils
                .loadAnimation(getActivity(),R.anim.item_animation_from_left);
        userLevelProgress.setAnimation(slideRightAnimation);

        Animation slideFromAngle = AnimationUtils
                .loadAnimation(getActivity(),R.anim.item_animation_from_angle);
        levelFrame.setAnimation(slideFromAngle);
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
            int lessonID = mLesson.getLessonID();
            TheoryLessonPagerActivity.startActivityWithTransition(
                    getActivity(),
                    lessonID,
                    lessonPreview);
        }
    }

    private class TheoryAdapter extends RecyclerView.Adapter<TheoryHolder>{

        private List<Lesson> mLessons;

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
