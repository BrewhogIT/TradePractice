package com.brewhog.android.tradepractice.presenter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brewhog.android.tradepractice.ChartPainterImageView;
import com.brewhog.android.tradepractice.R;
import com.brewhog.android.tradepractice.model.LessonPack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    public static final String TAG = "ChartFragment";
    private String chartUrl;
    private String chartDoneUrl;
    private List<String> signals;

    private ChartPainterImageView chartView;
    private FloatingActionButton decisionButton;
    private FloatingActionButton clearButton;
    private FloatingActionButton undoButton;
    private RecyclerView signalsRecyclerView;
    private ProgressBar mProgressBar;
    private SignalAdapter mAdapter;
    private TextView titleText;

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

        View view = inflater.inflate(R.layout.fragment_chart,container,false);
        chartView = view.findViewById(R.id.chart_view);
        decisionButton = view.findViewById(R.id.decision_button);
        undoButton = view.findViewById(R.id.undo_button);
        clearButton = view.findViewById(R.id.clear_button);
        signalsRecyclerView = view.findViewById(R.id.signals_recycler_view);
        mProgressBar = view.findViewById(R.id.progressBar);
        titleText = view.findViewById(R.id.signal_title);

        signalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI(chartUrl);

        decisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI(chartDoneUrl);
                titleText.setText(R.string.signal_title);

                decisionButton.setVisibility(View.GONE);
                undoButton.setVisibility(View.GONE);
                clearButton.setVisibility(View.GONE);

                chartView.clearChart();
                chartView.setDrawEnable(false);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartView.clearChart();
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartView.undoLastDraw();
            }
        });

        return view;
    }

    private void updateUI(String chartUrl) {
        updateAdapter();
        updateChart(chartUrl);
    }

    private void updateAdapter() {
        //Изначально список адаптера содержит 1 эллемент с инструкцией,
        //после обновления подгружаются список сигналов, инструкция заменяется дисклеймером

        if (mAdapter == null){
            signals = new ArrayList<>();
            signals.add(getString(R.string.signal_info));

            mAdapter = new SignalAdapter();
            signalsRecyclerView.setAdapter(mAdapter);
        }else {
            signals = getArguments().getStringArrayList(ARG_SIGNALS);
            signals.add(getString(R.string.signal_attention));
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateChart(String pictureUrl) {
        mProgressBar.setVisibility(View.VISIBLE);

        Glide.with(getActivity())
                .load(pictureUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,
                                                Object model, Target<Drawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource,
                                                   Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(chartView);
    }

    private class SignalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView signalText;

        public SignalHolder(@NonNull View itemView) {
            super(itemView);
            signalText = itemView.findViewById(R.id.signal_name);
            itemView.setOnClickListener(this);
        }

        public void bind(String signal){
            signalText.setText(signal);
        }

        @Override
        public void onClick(View view) {
            Integer lessonID = LessonPack
                    .getLessonPack(getActivity())
                    .getLessonIdFromSignal((String)signalText.getText());

            if (lessonID != null){
                Intent intent = TheoryLessonPagerActivity.newIntent(getActivity(),lessonID);
                startActivity(intent);
            }

        }
    }

    private class SignalAdapter extends RecyclerView.Adapter<SignalHolder>{

        @NonNull
        @Override
        public SignalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(viewType,parent,false);
            SignalHolder holder = new SignalHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SignalHolder holder, int position) {
            holder.bind(signals.get(position));
        }

        @Override
        public int getItemCount() {
            return signals.size();
        }

        @Override
        public int getItemViewType(int position) {
            //В качестве последнего элемента выводит дисклеймер
            int viewType = (position == signals.size() - 1)?
                    R.layout.list_item_attention : R.layout.list_item_signal;

            return viewType;
        }
    }
}