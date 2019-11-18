package com.brewhog.android.tradepractice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

public class TheoryListActivity extends SingleFragmentActivity {
    public static final String ILLUSTRATION_LESSON_KIND_EXTRA = "IllustrationLindExtra";


    public static Intent newIntent(Context context, int imageResId){
        Intent intent = new Intent(context,TheoryListActivity.class);
        intent.putExtra(TheoryListActivity.ILLUSTRATION_LESSON_KIND_EXTRA,imageResId);
        return intent;
    }

    public static void startActivityWithTransition(Activity activity, View view, Intent intent){
        String transitionalName = activity.getString(R.string.lesson_kind_logo);
        view.setTransitionName(transitionalName);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,view,transitionalName);
        activity.startActivity(intent,options.toBundle());
    }

    @Override
    public Fragment newFragment() {
        int imageResId = getIntent().getIntExtra(ILLUSTRATION_LESSON_KIND_EXTRA,0);
        return TheoryListFragment.newInstance(imageResId);
    }
}
