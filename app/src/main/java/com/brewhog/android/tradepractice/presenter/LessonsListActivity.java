package com.brewhog.android.tradepractice.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.brewhog.android.tradepractice.R;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class LessonsListActivity extends SingleFragmentActivity implements GuideFragment.Callback{
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
                fragment = GuideFragment.newInstance(imageResId);
        }
        return fragment;
    }

    @Override
    public String getFragmentTAG() {
        int imageResId = getIntent().getIntExtra(ILLUSTRATION_LESSON_KIND_EXTRA, 0);
        String TAG = null;
        switch (imageResId) {
            case R.drawable.theory:
                TAG = TheoryListFragment.TAG;
                break;
            case R.drawable.practice:
                TAG = PracticeListFragment.TAG;
                break;
            case R.drawable.howtouse:
                TAG = GuideFragment.TAG;
        }
        return TAG;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(PracticeListFragment.TAG);
        if (fragment != null)
        {
            ((PracticeListFragment)fragment).onActivityResult(requestCode, resultCode, data);
        }
    }
}
