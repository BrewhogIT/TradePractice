package com.brewhog.android.tradepractice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class TheoryLessonPagerActivity extends AppCompatActivity
implements TestPageFragment.CallBack, TheoryPageFragment.CallBack{
    public static final String TAG = "TheoryLessonPagerActivity";
    public static final String LESSONS_ID_EXTRA = "com.brewhog.android.tradepractice.lesson_number";
    public static final String PAGE_TYPE_EXTRA = "com.brewhog.android.tradepractice.page_type";
    private static final int REQUEST_CHOOSE_WAY = 1;
    public static final int LESSON_PAGE_TYPE = 2;
    public static final int TEST_PAGE_TYPE = 3;
    private CustomViewPager theoryLessonPager;
    private Lesson mLesson;
    private int lessonID;
    private int pageType;


    public static Intent newIntent(Context context, int lessonID){
        Intent intent = new Intent(context, TheoryLessonPagerActivity.class);
        intent.putExtra(LESSONS_ID_EXTRA,lessonID);

        return intent;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Для анимации перехода к элементам пейджера, приостанавливаем анимацию
        // разблокировка происходит в TheoryPageFragment.CallBack
        supportPostponeEnterTransition();
        postponeEnterTransition();


        setContentView(R.layout.activity_theory_lesson);

        if (savedInstanceState != null){
            pageType = savedInstanceState.getInt(PAGE_TYPE_EXTRA);
        }else {
            pageType = LESSON_PAGE_TYPE;
        }

        lessonID = getIntent().getIntExtra(LESSONS_ID_EXTRA,0);
        mLesson = LessonPack.getLessonPack(this).getLesson(lessonID);

        theoryLessonPager = findViewById(R.id.theory_lesson_pager);
        updateUI(theoryLessonPager,pageType);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"onSaveInstanceState() called, page Type is " + pageType);
        outState.putInt(PAGE_TYPE_EXTRA, pageType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_WAY) {
            int windowKind = data.getIntExtra(ChooseWayActivity.EXTRA_WINDOW_KIND,0);
            switch (windowKind){
                case ChooseWayActivity.START_NEW_TEST:
                    if (resultCode == Activity.RESULT_OK){
                        pageType = TEST_PAGE_TYPE;
                        updateUI(theoryLessonPager, pageType);
                    }
                    break;
                case ChooseWayActivity.RESTART_TEST:
                    if (resultCode == Activity.RESULT_OK){
                        pageType = TEST_PAGE_TYPE;
                    }else {
                        pageType = LESSON_PAGE_TYPE;
                    }
                    updateUI(theoryLessonPager,pageType);
                    break;
                case ChooseWayActivity.TEST_DONE:
                    if (!mLesson.isDone()){
                        mLesson.setDone(true);
                        UserPreferences.levelUp(this);
                        LessonPack.getLessonPack(this).updateDataBase(mLesson);
                    }

                    finish();
                    break;
            }
        }
    }

    private int getWindowKind(int pageType){
        int windowKind = 0;
        boolean isTestDone = mLesson.checkTest();

        switch (pageType){
            case LESSON_PAGE_TYPE:
                windowKind = ChooseWayActivity.START_NEW_TEST;
                break;
            case TEST_PAGE_TYPE:
                windowKind = isTestDone? ChooseWayActivity.TEST_DONE : ChooseWayActivity.RESTART_TEST;
                break;
        }

        return windowKind;
    }

    @SuppressLint("LongLogTag")
    private void updateUI(final CustomViewPager pager, final int pageType){
        Log.i(TAG, "Page Type is " + pageType);
        //Класс является общим для урока и для теста, поэтому исходя из типа урока создаются
        //разные фрагменты, а также задается длинна пейджера

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
                        fragment = TestPageFragment.newInstance(mLesson.getLessonID(),position);
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return getLimit(pageType);
            }
        };
        pager.setAdapter(adapter);

        int limit = getLimit(pageType);
        pager.setOffscreenPageLimit(limit);

        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPosition;
            boolean hasBeenScrolled;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //В тесте запрещается перелистывать страницы
                lastPosition = position;
                boolean isEnable = (pageType != TEST_PAGE_TYPE);
                pager.setPagingEnabled(isEnable);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onPageScrollStateChanged(int state) {
                //Если у пейджера последняя страница, при пролистывании вызывается окно выбора действия
                switch (state){
                    case ViewPager.SCROLL_STATE_SETTLING:
                        hasBeenScrolled = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if ((lastPosition == pager.getChildCount() - 1)&& !hasBeenScrolled){
                            Intent intent = ChooseWayActivity.newIntent(
                                    TheoryLessonPagerActivity.this,
                                    getWindowKind(pageType),
                                    mLesson.getPercentOfCorrectAnswer());
                            startActivityForResult(intent,REQUEST_CHOOSE_WAY);
                        }else {
                            hasBeenScrolled = false;
                        }
                        break;
                }
            }
        });
    }

    private int getLimit(int pageType) {
        int limit = 0;
        if (pageType == LESSON_PAGE_TYPE){
            limit = mLesson.getPages().size();
        } else if (pageType == TEST_PAGE_TYPE){
            limit = mLesson.getLessonTest().size();
        }
        return limit;
    }

    @Override
    public View getPager() {
        return theoryLessonPager;
    }

    @Override
    public void setStartPostTransition() {
        startPostponedEnterTransition();
    }

    public static void startActivityWithTransition(Activity activity, int lessonID, View view){
        Intent intent = newIntent(activity,lessonID);

        view.setTransitionName(
                activity.getString(R.string.lesson_preview_transition_name, lessonID));

        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(activity,view,
                        view.getTransitionName());
        activity.startActivity(intent,options.toBundle());
    }
}
