package com.brewhog.android.tradepractice;

import android.app.job.JobService;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PracticePack {
    public static final String TAG = "PracticePack";
    private List<Practice> mPracticeList;
    private RecyclerView.Adapter mAdapter;
    private int filesLoaded;
    private boolean loadDone;

    public PracticePack(RecyclerView.Adapter adapter, List<Practice> list) {
        mAdapter = adapter;
        mPracticeList = list;
    }

    public PracticePack(List<Practice> list) {
        mPracticeList = list;
    }

    public void loadPracticeList(){
        Log.i(TAG,"start getAllFolders method");
        mPracticeList.clear();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference chartsRef = storage.getReference().child("charts");

        // Получаем список подпапок из каталога
        chartsRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        //Для каждой папки создается объект и заполняется данными
                        //предварительно переворачивается список, чтобы новые данные отображались первыми
                        List<StorageReference> folderList = listResult.getPrefixes();
                        Collections.reverse(folderList);

                        setChartReferences(folderList,mPracticeList);
                        setChartInfo(folderList,mPracticeList);

                        if (mAdapter != null){
                            mAdapter.notifyDataSetChanged();
                        }
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

            loadChart(folderReference, index);
            loadChartDone(folderReference, index);
        }
    }

    private void loadChartDone(StorageReference folderReference, final int index) {
        //загружаем график с разметкой

        folderReference.child("chartDone.PNG").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i(TAG,"chartDone link was gotten: " + uri.toString());
                        mPracticeList.get(index).setChartDoneUrl(uri.toString());

                        if (mAdapter != null){
                            mAdapter.notifyItemChanged(index);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "chartDone link doesnt get",e);
            }
        });
    }

    private void loadChart(StorageReference folderReference, final int index) {
        //загружаем график без разметки

        folderReference.child("chart.PNG").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i(TAG,"Chart link was gotten: " + uri.toString());
                        mPracticeList.get(index).setChartUrl(uri.toString());

                        if (mAdapter != null){
                            mAdapter.notifyItemChanged(index);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Chart link doesnt get",e);
            }
        });
    }

    private void setChartInfo(final List<StorageReference> listResult, final List<Practice> practiceList){
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

                                if (mAdapter != null){
                                    mAdapter.notifyItemChanged(index);
                                }

                                //Считаем все ли инфо-файлы загрузились,
                                //индикатор будет использоваться для проверки в других классах
                                //(например при выводе уведомлений)
                                filesLoaded++;
                                if (filesLoaded == listResult.size()){
                                    loadDone = true;
                                    Log.i(TAG,"load info files is done");
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadDone = true;
                        Log.e(TAG,"load info files is fail",e);
                    }
                });
            } catch (IOException e) {
                Log.e(TAG,"Failed info file loading",e);
            }
        }
    }

    public boolean isLoadDone() {
        return loadDone;
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
}

