package com.brewhog.android.tradepractice.database;

import android.content.Context;

import androidx.preference.PreferenceManager;

public class UserPreferences {
    private static final String PREF_USER_LVL = "UserLevel";
    private static final String PREF_LAST_ID = "LastChartID";
    private static final String PREF_ALARM_ON = "AlarmOn";

    public static int getUserLevel(Context context){
       return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_USER_LVL,0);
    }

    private static void setUserLevel(Context context, int level){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_USER_LVL,level)
                .apply();
    }

    public static void levelUp(Context context){
        setUserLevel(context,getUserLevel(context) + 1);
    }

    public static int getLastChartID (Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_LAST_ID,0);
    }

    public static void setLastChartID (Context context, int id){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_LAST_ID,id)
                .apply();
    }

    public static boolean isAlarmOn (Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_ALARM_ON,false);
    }

    public static void setAlarmOn (Context context, boolean isOn){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_ALARM_ON,isOn)
                .apply();
    }


}
