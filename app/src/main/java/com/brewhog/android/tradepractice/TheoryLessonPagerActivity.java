package com.brewhog.android.tradepractice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class TheoryLessonPagerActivity extends AppCompatActivity {
    public static final String TAG = "TheoryLessonPagerActivity";
    public static final String LESSONS_ID_EXTRA = "com.brewhog.android.tradepractice.lesson_number";
    private static final int REQUEST_CHOOSE_WAY = 1;
    private static final int LESSON_PAGE_TYPE = 2;
    private static final int TEST_PAGE_TYPE = 3;
    private CustomViewPager theoryLessonPager;
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
        mLesson = LessonPack.getLessonPack(this).getLesson(lessonID);

        setContentView(R.layout.activity_theory_lesson);
        theoryLessonPager = findViewById(R.id.theory_lesson_pager);
        updateUI(theoryLessonPager,LESSON_PAGE_TYPE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_WAY && resultCode == Activity.RESULT_OK) {
            updateUI(theoryLessonPager, TEST_PAGE_TYPE);
        }
    }

    private void updateUI(final CustomViewPager pager, final int pageType){
        int limit = 0;
        if (pageType == LESSON_PAGE_TYPE){
            limit = mLesson.getPages().size();
        } else if (pageType == TEST_PAGE_TYPE){
            limit = mLesson.getLessonTest().size();
        }

        final int finalLimit = limit;
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new FragmentStatePagerAdapter(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (pageType){
                    case LESSON_PAGE_TYPE:
                        fragment = TheoryPageFragment.newInstance(mLesson.getLessonID(),position);
                        break;
                    case TEST_PAGE_TYPE:
                        fragment = TestPageFragment.newInstance(mLesson.getLessonID(),position,pager);
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return finalLimit;
            }
        };
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(limit);

        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int actualPosition;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                actualPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                boolean isEnable = (pageType != TEST_PAGE_TYPE);
                pager.setPagingEnabled(isEnable);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if ((state == ViewPager.SCROLL_STATE_DRAGGING) &&
                        (actualPosition == pager.getChildCount() - 1)){
                    Intent intent = ChooseWayActivity.newIntent(
                            TheoryLessonPagerActivity.this,ChooseWayActivity.START_NEW_TEST);
                    startActivityForResult(intent,REQUEST_CHOOSE_WAY);
                }
            }
        });

    }


}
