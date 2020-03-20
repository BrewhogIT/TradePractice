package com.brewhog.android.tradepractice.presenter;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActionBarHider {
    private static int orientation;

    public static void hideIfLandscape(Context context, AppCompatActivity activity){
        orientation = context.getResources().getConfiguration().orientation;
        ActionBar actionBar = activity.getSupportActionBar();

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (actionBar != null){
                actionBar.hide();
            }
        }else {
            if (actionBar != null){
                actionBar.show();
            }
        }
    }
}
