package com.brewhog.android.tradepractice.presenter;

import android.os.Bundle;

import com.brewhog.android.tradepractice.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    public abstract Fragment newFragment();
    public abstract String getFragmentTAG();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment  = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = newFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment,getFragmentTAG())
                    .commit();

        }
    }
}
