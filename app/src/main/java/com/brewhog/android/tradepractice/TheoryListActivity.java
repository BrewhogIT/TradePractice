package com.brewhog.android.tradepractice;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class TheoryListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,TheoryListActivity.class);
        return intent;
    }

    @Override
    public Fragment newFragment() {
        return TheoryListFragment.newInstance();
    }
}
