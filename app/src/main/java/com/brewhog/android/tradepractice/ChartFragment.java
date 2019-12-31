package com.brewhog.android.tradepractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChartFragment extends Fragment {
    public static final String ARG_CHART = "chart_image_url";
    public static final String ARG_CHART_DONE = "chart_done_image_url";
    public static final String ARG_SIGNALS = "signals_array";
    private String chartUrl;
    private String chartDoneUrl;
    private List<String> signals;

    private ImageView chartView;
    private FloatingActionButton decisionButton;
    private RecyclerView signalsRecyclerView;

    public static ChartFragment newInstance(String chartUrl, String chartDoneUrl, ArrayList signals) {
        Bundle args = new Bundle();
        args.putString(ARG_CHART,chartUrl);
        args.putString(ARG_CHART_DONE,chartDoneUrl);
        args.putStringArrayList(ARG_SIGNALS,signals);

        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chartUrl = getArguments().getString(ARG_CHART);
        chartDoneUrl = getArguments().getString(ARG_CHART_DONE);
        signals = getArguments().getStringArrayList(ARG_SIGNALS);

        View view = inflater.inflate(R.layout.fragment_chart,container,false);
        chartView = view.findViewById(R.id.chart_view);
        decisionButton = view.findViewById(R.id.decision_button);
        signalsRecyclerView = view.findViewById(R.id.signals_recycler_view);

        decisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChart(chartDoneUrl);
            }
        });

        //выделить в отдельный метод, для обновления UI
        signalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateChart(chartUrl);

        return view;
    }

    private void updateChart(String pictureUrl) {
        Glide.with(getActivity())
                .load(pictureUrl)
                .placeholder(R.drawable.chart_placeholder)
                .into(chartView);
    }
}