package com.brewhog.android.tradepractice;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class ChartActivity extends SingleFragmentActivity {
    public static final String CHART_URL_EXTRA = "com.brewhog.android.tradepractice.chartUrl";
    public static final String CHART_DONE_URL_EXTRA = "com.brewhog.android.tradepractice.chartDoneUrl";
    public static final String SIGNALS_URL_EXTRA = "com.brewhog.android.tradepractice.signals";

    public static Intent newIntent(Context context, String chartUrl, String chartDoneUri, ArrayList<String> signals){
        Intent intent = new Intent(context, ChartActivity.class);
        intent.putExtra(CHART_URL_EXTRA,chartUrl);
        intent.putExtra(CHART_DONE_URL_EXTRA,chartDoneUri);
        intent.putStringArrayListExtra(SIGNALS_URL_EXTRA,signals);

        return intent;
    }

    @Override
    public Fragment newFragment() {
        String chartUrl = getIntent().getStringExtra(CHART_URL_EXTRA);
        String chartDoneUrl = getIntent().getStringExtra(CHART_DONE_URL_EXTRA);
        ArrayList<String> signals = getIntent().getStringArrayListExtra(SIGNALS_URL_EXTRA);

        Fragment fragment = ChartFragment.newInstance(chartUrl,chartDoneUrl,signals);
        return fragment;
    }
}
