package com.brewhog.android.tradepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

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

        CenterZoomLayoutManager layoutManager = new CenterZoomLayoutManager(
                this, LinearLayoutManager.HORIZONTAL,false);
        lessonSectionRecyclerView = findViewById(R.id.lesson_type_recycler_view);
        lessonSectionRecyclerView.setLayoutManager(layoutManager);
        lessonSectionRecyclerView.setAdapter(new LessonTypeAdapter());
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(lessonSectionRecyclerView);
        lessonSectionRecyclerView.setOnFlingListener(snapHelper);

        int recyclerCenterPosition = layoutManager.getItemCount() / 2;
        layoutManager.scrollToPosition(Integer.MAX_VALUE / 2 - 1);
        lessonSectionRecyclerView.smoothScrollToPosition(recyclerCenterPosition);

    }

    private class LessonTypeViewHolder extends RecyclerView.ViewHolder{
        private ImageView mLessonTypeLogoView;
        private int mLessonKind;

        public LessonTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            mLessonTypeLogoView = itemView.findViewById(R.id.lesson_kind_logo);
        }

        public void bind(int lessonKind){
            Drawable LessonTypeIcon = null;
            mLessonKind = lessonKind;

            switch (mLessonKind){
                case R.string.practice:
                    LessonTypeIcon = getResources().getDrawable(R.drawable.practice);
                    break;
                case  R.string.theory:
                    LessonTypeIcon = getResources().getDrawable(R.drawable.theory);
                    break;
                case R.string.how_to_use:
                    LessonTypeIcon = getResources().getDrawable(R.drawable.howtouse);
                    break;
            }

            mLessonTypeLogoView.setImageDrawable(LessonTypeIcon);
        }
    }

    private class LessonTypeAdapter extends RecyclerView.Adapter<LessonTypeViewHolder>{

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
            int realPosition = position % lessonsKindList.size();
            holder.bind(lessonsKindList.get(realPosition));
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }
}
