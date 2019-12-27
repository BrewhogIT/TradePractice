package com.brewhog.android.tradepractice;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PracticeListFragment extends Fragment {
    private static final String TAG = "PracticeListFragment";
    private ImageView lessonKindIllustration;
    private RecyclerView practiceListRecyclerView;
    private FirebaseAuth mAuth;
    private List<Practice> mPracticeList;
    private PracticeAdapter mAdapter;
    public static final String IMAGE_RES_ID_ARGS = "Resource id for lesson kind logo";

    public static PracticeListFragment newInstance(int imageResID) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RES_ID_ARGS,imageResID);

        PracticeListFragment fragment = new PracticeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice_list,container,false);

        int imageResId = getArguments().getInt(IMAGE_RES_ID_ARGS);
        lessonKindIllustration = view.findViewById(R.id.practice_lesson_large_illustration);
        lessonKindIllustration.setImageResource(imageResId);

        practiceListRecyclerView = view.findViewById(R.id.practice_recycler_view);
        practiceListRecyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(),3, RecyclerView.VERTICAL,false));

        connectToFirebase();
        return view;
    }

    private class PracticeHolder extends RecyclerView.ViewHolder{
        private TextView tickerView;
        private ImageView chartView;
        private TextView dateView;

        public PracticeHolder(@NonNull View itemView) {
            super(itemView);

            tickerView = itemView.findViewById(R.id.ticker);
            chartView = itemView.findViewById(R.id.chart);
            dateView = itemView.findViewById(R.id.date_of_chart);
        }

        public void Bind(Practice practiceItem){

            Glide.with(getActivity())
                    .load(practiceItem.getChartUrl())
                    .placeholder(R.drawable.chart_placeholder)
                    .into(chartView);
            tickerView.setText(practiceItem.getTicker());
            dateView.setText(practiceItem.getDate());
        }
    }

    private class PracticeAdapter extends RecyclerView.Adapter<PracticeHolder>{
        private List<Practice> mPracticeList;

        public PracticeAdapter(List<Practice> practiceList) {
            mPracticeList = practiceList;
        }

        @NonNull
        @Override
        public PracticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_practice,parent,false);
            PracticeHolder holder = new PracticeHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PracticeHolder holder, int position) {
            Practice practice = mPracticeList.get(position);
            holder.Bind(practice);
        }

        @Override
        public int getItemCount() {
            return mPracticeList.size();
        }
    }

    private void updateUI(){
        Log.i(TAG,"updateUI called");

        if (mAdapter == null){
            mPracticeList = new ArrayList<>();
            mAdapter = new PracticeAdapter(mPracticeList);
            practiceListRecyclerView.setAdapter(mAdapter);

            //Загружаем данные, вставляем в созданный лист, обнавляем адаптер
            PracticePack mPracticePack = new PracticePack(mAdapter, mPracticeList);
            mPracticePack.loadPracticeList();
        } else{
            mAdapter.notifyDataSetChanged();
        }

    }

    private void connectToFirebase() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            updateUI();
        }else{
            signInAnonymously();
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.e(TAG,"signInAnonymously:SUCCESS");
                        updateUI();

                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"signInAnonymously:FAILURE",e);
                    }
                });
    }
}
