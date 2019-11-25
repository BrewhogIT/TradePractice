package com.brewhog.android.tradepractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestPageFragment extends Fragment {
    public static TestPageFragment newInstance() {
        Bundle args = new Bundle();

        TestPageFragment fragment = new TestPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
