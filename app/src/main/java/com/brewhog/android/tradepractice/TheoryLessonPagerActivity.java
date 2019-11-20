package com.brewhog.android.tradepractice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TheoryLessonPagerActivity extends AppCompatActivity {
    public static final String LESSONS_ID_EXTRA = "com.brewhog.android.tradepractice.lesson_number";
    private ViewPager theoryLessonPager;
    private Lesson mLesson;

    public static Intent newIntent(Context context, UUID lessonID){
        Intent intent = new Intent(context, TheoryLessonPagerActivity.class);
        intent.putExtra(LESSONS_ID_EXTRA,lessonID);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID lessonID = (UUID) getIntent().getSerializableExtra(LESSONS_ID_EXTRA);
        for (Lesson lesson:LessonPack.getLessonPack(this).getLessons()){
            if (lesson.getUUID().equals(lessonID)){
                mLesson = lesson;
            }
        }

        setContentView(R.layout.activity_theory_lesson);
        FragmentManager manager = getSupportFragmentManager();
        theoryLessonPager = findViewById(R.id.theory_lesson_pager);
        theoryLessonPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = PageOfTheoryFragment.newInstance();
                return fragment;
            }

            @Override
            public int getCount() {
                return mLesson.getPages().size();
            }
        });


    }
}
