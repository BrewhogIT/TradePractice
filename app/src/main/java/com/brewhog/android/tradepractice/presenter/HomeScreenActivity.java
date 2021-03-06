package com.brewhog.android.tradepractice.presenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewhog.android.tradepractice.R;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {
    private static final String TAG = "HomeScreenActivity";
    private static final String ADAPTER_KEY = "AdapterKey";

    private ImageView logoView;
    private List<Integer> lessonsKindList;
    private RecyclerView lessonSectionRecyclerView;
    private CenterZoomLayoutManager layoutManager;
    private int orientation;
    private Integer adapterPosition;

    public static Intent newIntent(Context context){
        return new Intent(context, HomeScreenActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            adapterPosition = savedInstanceState.getInt(ADAPTER_KEY);
        }

        setContentView(R.layout.activity_home_screen);
        orientation = this.getResources().getConfiguration().orientation;
        ActionBarHider.hideIfLandscape(this,this);

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        adapterPosition = layoutManager.findFirstVisibleItemPosition() + 1;
        outState.putInt(ADAPTER_KEY, adapterPosition);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        int managerOrientation = (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL;

        layoutManager = new CenterZoomLayoutManager(
                this, managerOrientation,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new LessonTypeAdapter(lessonsKindList));

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setOnFlingListener(snapHelper);

        //прокручиваем recyclerView на середину, центральный елемент должен быть "Руководство"
        if (adapterPosition == null){
            int recyclerCenterPosition = layoutManager.getItemCount() / 2;
            layoutManager.scrollToPosition(recyclerCenterPosition);
            recyclerView.smoothScrollToPosition(recyclerCenterPosition + 2);
        }else{
            layoutManager.scrollToPosition(adapterPosition);
            recyclerView.smoothScrollToPosition(adapterPosition);
        }

    }

    private class LessonTypeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView mLessonTypeLogoView;
        private TextView lessonKindTopic;
        private int mLessonKind;
        private int resourseID;
        private String topic;

        public LessonTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            mLessonTypeLogoView = itemView.findViewById(R.id.lesson_kind_logo);
            lessonKindTopic = itemView.findViewById(R.id.lesson_kind_topic);
            itemView.setOnClickListener(this);
        }

        public void bind(int lessonKind){
            mLessonKind = lessonKind;

            switch (mLessonKind){
                case R.string.practice:
                    resourseID = R.drawable.practice;
                    topic = getString(R.string.practice);
                    break;
                case  R.string.theory:
                    resourseID = R.drawable.theory;
                    topic = getString(R.string.theory);
                    break;
                case R.string.how_to_use:
                    resourseID = R.drawable.howtouse;
                    topic = getString(R.string.how_to_use);
                    break;
            }

            //Меняем изображение и текст в зависимости от типа урока
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/SourceSerifPro.ttf");
            lessonKindTopic.setTypeface(typeface);
            lessonKindTopic.setText(topic);
            mLessonTypeLogoView.setImageResource(resourseID);

            //меняем размер ооносительно экрана
            if (orientation == Configuration.ORIENTATION_PORTRAIT){
                int activityWidth = getPixFromDp(getResources().getConfiguration().screenWidthDp);
                mLessonTypeLogoView.getLayoutParams().width = (int)Math.round(activityWidth * 0.75);
            }else {
                int activityHeight = getPixFromDp(getResources().getConfiguration().screenHeightDp);
                mLessonTypeLogoView.getLayoutParams().height = (int)Math.round(activityHeight * 0.55);
            }

        }

        @Override
        public void onClick(View view) {
            Intent intent = LessonsListActivity.newIntent(getBaseContext(),resourseID);
            LessonsListActivity.startActivityWithTransition(
                    HomeScreenActivity.this,mLessonTypeLogoView,intent);
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

    private int getPixFromDp(float i) {
        //конвертируем пиксели в дип
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                i, getResources().getDisplayMetrics());
    }

}
