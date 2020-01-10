package com.brewhog.android.tradepractice;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ChartUpdateService extends IntentService {
    private static final String TAG = "ChartUpdateService";
    private static final int NOTIFICATION_INTENT_CODE = 1;
    private static final String CHANEL_1_ID = "first_notification_chanel";
    private static final long INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    public static boolean isNetworkConnected = false;
    public static String ACTION_SHOW_NOTIFICATION =
            "com.brewhog.android.tradepractice.SHOW_NOTIFICATION";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String PERM_PRIVATE =
            "com.brewhog.android.tradepractice.PRIVATE";

    public static Intent newIntent(Context context){
        return new Intent(context,ChartUpdateService.class);
    }

    public ChartUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG,"onHandleIntent is Run");

        registerNetworkCallback();
        if (!isNetworkConnected){
            Log.i(TAG,"Network is'n connected");
            return;
        }

        List<Practice> mPracticeList;
        PracticePack mPracticePack;

        mPracticeList = new ArrayList<>();
        mPracticePack = new PracticePack(mPracticeList);
        mPracticePack.loadPracticeList();

        while (!mPracticePack.isLoadDone()) {
            //После завершения загрузки лист с объектами готов к проверке
        }
        if (mPracticeList.size() == 0) {
            return;
        }

        int resultID = mPracticeList.get(0).getId();
        int lastID = UserPreferences.getLastChartID(this);

        //если код последнего элемента отличается от кода полученого,
        //отправляем уведомление
        Log.i(TAG,"result ID is: " + resultID + " last ID is: " + lastID);
        if (resultID != lastID) {
            Log.i(TAG,"new chart is available! ");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                String name = getString(R.string.first_chanel);
                NotificationChannel channel = new NotificationChannel(CHANEL_1_ID, name, importance);

                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            Intent lessonListIntent = LessonsListActivity.newIntent(this, R.drawable.practice);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, NOTIFICATION_INTENT_CODE, lessonListIntent, 0);
            Resources resources = getResources();

            Notification notification = new NotificationCompat.Builder(this, CHANEL_1_ID)
                    .setTicker(resources.getString(R.string.notification_title))
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(resources.getString(R.string.notification_title))
                    .setContentText(resources.getString(R.string.notification_text))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            sendNotification(0,notification);
        }
        UserPreferences.setLastChartID(this, resultID);
    }

    private void registerNetworkCallback(){
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .build();
        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                isNetworkConnected = true;
            }

            @Override
            public void onLost(@NonNull Network network) {
                isNetworkConnected = false;
            }
        };

        try {
            cm.unregisterNetworkCallback(callback);
        } catch (Exception e) {
            Log.w(TAG,"NetworkCallback was not registered or already unregistered, network connected is "
                    + isNetworkConnected);
        }

        try {
            cm.registerNetworkCallback(networkRequest,callback);
        } catch (Exception e) {
            isNetworkConnected = false;
            Log.e(TAG,"registerNetworkCallback error. NetworkCallback was not registered , network connected is "
                    + isNetworkConnected);
        }

    }

    public static void setChartServiceAlarm(Context context, boolean isOn){
        Intent intent = ChartUpdateService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,0);

        AlarmManager manager =(AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn){
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    INTERVAL_MS,pendingIntent);
        }else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

        UserPreferences.setAlarmOn(context,isOn);
    }

    public static boolean isChartServiceAlarmOn(Context context){
        Intent intent = ChartUpdateService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,0,intent,PendingIntent.FLAG_NO_CREATE);

        return pendingIntent != null;
    }

    private void sendNotification(int requestCode,Notification notification){
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE,requestCode);
        i.putExtra(NOTIFICATION,notification);
        sendOrderedBroadcast(i,PERM_PRIVATE,null,null,
                Activity.RESULT_OK,null,null);
    }
}

