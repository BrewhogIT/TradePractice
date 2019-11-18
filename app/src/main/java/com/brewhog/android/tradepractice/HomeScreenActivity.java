package com.brewhog.android.tradepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {
    private ImageView logoView;
    private List<Integer> lessonsKindList;
    private RecyclerView lessonSectionRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        logoView = findViewById(R.id.logo_view);
        Drawable logo = getResources().getDrawable(R.drawable.logo);
        logoView.setImageDrawable(logo);

        lessonsKindList = new ArrayList<>();
        lessonsKindList.add(R.string.theory);
        lessonsKindList.add(R.string.practice);
        lessonsKindList.add(R.string.how_to_use);

        lessonSectionRecyclerView = findViewById(R.id.lesson_type_recycler_view);
        setupRecyclerView(lessonSectionRecyclerView);

    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        CenterZoomLayoutManager layoutManager = new CenterZoomLayoutManager(
                this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new LessonTypeAdapter(lessonsKindList));

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setOnFlingListener(snapHelper);

        int recyclerCenterPosition = layoutManager.getItemCount() / 2;
        layoutManager.scrollToPosition(Integer.MAX_VALUE / 2 - 1);
        recyclerView.smoothScrollToPosition(recyclerCenterPosition);
    }

    private class LessonTypeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView mLessonTypeLogoView;
        private int mLessonKind;

        public LessonTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            mLessonTypeLogoView = itemView.findViewById(R.id.lesson_kind_logo);
            itemView.setOnClickListener(this);
        }

        public void bind(int lessonKind){
            int resourseID = 0;
            Drawable lessonTypeIcon = null;
            mLessonKind = lessonKind;

            switch (mLessonKind){
                case R.string.practice:
                    resourseID = R.drawable.practice;
                    break;
                case  R.string.theory:
                    resourseID =R.drawable.theory;
                    break;
                case R.string.how_to_use:
                    resourseID = R.drawable.howtouse;
                    break;
            }

            lessonTypeIcon = getResources().getDrawable(resourseID);
            mLessonTypeLogoView.setImageDrawable(lessonTypeIcon);
            mLessonTypeLogoView.setTag(resourseID);
        }

        @Override
        public void onClick(View view) {
            switch (mLessonKind){
                case R.string.practice:
                    //Open practice page
                    break;
                case  R.string.theory:
                    int imageResId = getDrawableResId(mLessonTypeLogoView);
                    Intent intent = TheoryListActivity.newIntent(getBaseContext(),imageResId);
                    TheoryListActivity.startActivityWithTransition(
                            HomeScreenActivity.this,mLessonTypeLogoView,intent);
                    break;
                case R.string.how_to_use:
                    //Open tutorial
                    break;
            }
        }
    }

    private class LessonTypeAdapter extends RecyclerView.Adapter<LessonTypeViewHolder>{
        List<Integer> lessonKinds;

        public LessonTypeAdapter(List<Integer> lessonKinds) {
            this.lessonKinds = lessonKinds;
        }

        @NonNull
        @Override
        public LessonTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(HomeScreenActivity.this);
            View view = inflater.inflate(R.layout.lesson_kind_view,parent,false);
            LessonTypeViewHolder holder = new LessonTypeViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull LessonTypeViewHolder holder, int position) {
            int realPosition = position % lessonKinds.size();
            holder.bind(lessonKinds.get(realPosition));
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }

    private int getDrawableResId (ImageView view){
        return (Integer) view.getTag();
    }
}
