package com.brewhog.android.tradepractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PageOfTheoryFragment extends Fragment {

public static PageOfTheoryFragment newInstance() {
    Bundle args = new Bundle();

    PageOfTheoryFragment fragment = new PageOfTheoryFragment();
    fragment.setArguments(args);
    return fragment;
}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_of_theory,container,false);

        return view;
    }
}
