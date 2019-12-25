package com.brewhog.android.tradepractice;

import android.content.Context;

import androidx.preference.PreferenceManager;

public class UserPreferences {
    private static final String PREF_USER_LVL = "UserLevel";

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
}
