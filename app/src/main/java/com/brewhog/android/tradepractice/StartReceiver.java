package com.brewhog.android.tradepractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartReceiver extends BroadcastReceiver {
    private static final String TAG = "StartReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"onReceive is start");

        boolean shouldStartService = UserPreferences.isAlarmOn(context);
        ChartUpdateService.setChartServiceAlarm(context,shouldStartService);
    }
}
