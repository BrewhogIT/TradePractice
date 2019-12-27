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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            //Загружаем данные с FireBase
            loadPracticeList();
        }else{
            signInAnonymously();
        }
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
        updateUI();
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
        } else{
            mAdapter.notifyDataSetChanged();
        }

    }

    private void loadPracticeList(){
        Log.i(TAG,"start getAllFolders method");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference chartsRef = storage.getReference().child("charts");

        // Получаем список подпапок из каталога
        chartsRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        //Для каждой папки создается объект и заполняется данными
                        setChartReferences(listResult.getPrefixes(),mPracticeList);
                        setChartInfo(listResult.getPrefixes(),mPracticeList);

                        updateUI();
                        Log.i(TAG, "OnSuccess in getAllFolders, list mPracticeList size is " + mPracticeList.size());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"FAILED GET LIST ",e);
                    }
                });
    }

    private void setChartReferences(List<StorageReference> listResult, final List<Practice> practiceList) {
        // Для каждой подпапки создаем объект с ссылками на изображения графиков
        for(StorageReference folderReference : listResult){
            Practice practiceItem = new Practice();
            practiceList.add(practiceItem);
            final int index = listResult.indexOf(folderReference);

            folderReference.child("chart.PNG").getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.i(TAG,"Chart link was gotten: " + uri.toString());
                    practiceList.get(index).setChartUrl(uri.toString());
                    mAdapter.notifyItemChanged(index);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Chart link doesnt get",e);
                }
            });

            folderReference.child("chartDone.PNG").getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.i(TAG,"chartDone link was gotten: " + uri.toString());
                    practiceList.get(index).setChartDoneUrl(uri.toString());
                    mAdapter.notifyItemChanged(index);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "chartDone link doesnt get",e);
                }
            });
        }
    }

    private void setChartInfo(List<StorageReference> listResult, final List<Practice> practiceList){
        Log.i(TAG,"start setChartInfo method");

        //Загружаем инфо файл из каждой папки
        for (StorageReference folderReference : listResult){
            try {
                final int index = listResult.indexOf(folderReference);
                final File infoFile = File.createTempFile("info","txt");
                folderReference.child("info.txt").getFile(infoFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                //Когза загрузка завершена читаем строки из файла,
                                // добавляем в Practice из ранее созданного листа объектов
                                readInfoData(infoFile,practiceList.get(index));
                                mAdapter.notifyItemChanged(index);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            } catch (IOException e) {
                Log.e(TAG,"Failed info file loading",e);
            }
        }
    }

    private void readInfoData(File infoFile, Practice practice){
        Log.i(TAG,"start readInfoData method");

        //Читает info файл по строкам и добавляет данные в нужные поля Practice

        BufferedReader reader = null;
        try{
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(infoFile)));
            String line;
            List<String> signals = new ArrayList<>();
            for (int i = 0; (line = reader.readLine()) != null; i++){
                Log.i(TAG,"info file line: " + line);
                switch (i){
                    case 0:
                        int id = Integer.parseInt(line);
                        practice.setId(id);
                        break;
                    case 1:
                        practice.setTicker(line);
                        break;
                    case 2:
                        practice.setDate(line);
                        break;
                    default:
                        signals.add(line);
                        break;
                }

            }
            practice.setSignals(signals);
        }catch (IOException e){
            Log.e(TAG,"info file parsing");
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.e(TAG,"signInAnonymously:SUCCESS");
                //Загружаем данные с FireBase
                loadPracticeList();

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
