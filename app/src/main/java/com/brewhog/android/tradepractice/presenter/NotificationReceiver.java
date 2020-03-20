package com.brewhog.android.tradepractice.presenter;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.brewhog.android.tradepractice.presenter.ChartUpdateService;

import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"receive result: " + getResultCode());
        if (getResultCode() != Activity.RESULT_OK){
            //Активность переднего плана отменила рассылку
            return;
        }

        int requestCode = intent.getIntExtra(ChartUpdateService.REQUEST_CODE,0);
        Notification notification = intent.getParcelableExtra(ChartUpdateService.NOTIFICATION);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(requestCode,notification);
    }
}
