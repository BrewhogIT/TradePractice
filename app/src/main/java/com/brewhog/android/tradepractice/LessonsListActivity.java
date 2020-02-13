package com.brewhog.android.tradepractice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

public class LessonsListActivity extends SingleFragmentActivity implements GuideFragmentWeb.Callback{
    public static final String ILLUSTRATION_LESSON_KIND_EXTRA =
            "com.brewhog.android.tradepractice.IllustrationLessonKindExtra";


    public static Intent newIntent(Context context, int imageResId){
        Intent intent = new Intent(context, LessonsListActivity.class);
        intent.putExtra(LessonsListActivity.ILLUSTRATION_LESSON_KIND_EXTRA,imageResId);
        return intent;
    }

    public static void startActivityWithTransition(Activity activity, View view, Intent intent){
        String transitionalName = activity.getString(R.string.lesson_kind_logo_transitional_name);
        view.setTransitionName(transitionalName);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,view,transitionalName);
        activity.startActivity(intent,options.toBundle());
    }

    @Override
    public Fragment newFragment() {
        Fragment fragment = null;
        int imageResId = getIntent().getIntExtra(ILLUSTRATION_LESSON_KIND_EXTRA,0);
        switch (imageResId){
            case R.drawable.theory:
                fragment = TheoryListFragment.newInstance(imageResId);
                break;
            case R.drawable.practice:
                fragment = PracticeListFragment.newInstance(imageResId);
                break;
            case R.drawable.howtouse:
                fragment = GuideFragmentWeb.newInstance(imageResId);
        }
        return fragment;
    }

    //Нужен для остановки анимации перехода между общим элементом
    @Override
    public void setStopPostTransition() {
        supportPostponeEnterTransition();
        postponeEnterTransition();
    }

    //Нужен для возобновления анимации перехода между общим элементом
    @Override
    public void setStartPostTransition() {
        startPostponedEnterTransition();
    }
}
