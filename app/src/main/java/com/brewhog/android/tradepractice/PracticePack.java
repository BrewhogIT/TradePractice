package com.brewhog.android.tradepractice;

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
import java.util.List;

import androidx.annotation.NonNull;
/*

public class PracticePack {
    public static final String TAG = "PracticePack";
    private List<StorageReference> foldersList;
    private boolean loadingDone = false;

    public PracticePack() {
        foldersList = getAllFolders();
    }

    private List<StorageReference> getAllFolders(){
        Log.i(TAG,"start getAllFolders method");
        final List<StorageReference> foldersList = new ArrayList<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference chartsRef = storage.getReference().child("charts");

        chartsRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        foldersList.addAll(listResult.getPrefixes());
                        loadingDone = true;
                        Log.i(TAG, "OnSuccess in getAllFolders, list foldersList size is " + foldersList.size());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"FAILED GET LIST ",e);
                        loadingDone = true;
                    }
                });
        while(!loadingDone){
            //wait while loading done
        }

        return foldersList;
    }
    private List<Practice> сreatePracticeList(){
        List <Practice> practiceList = new ArrayList<>();
        for(int i = 0; i < foldersList.size();i++){
            practiceList.add(new Practice());
        }
        Log.i(TAG,"createPracticeList called, practiceList size is " + practiceList.size());
        return practiceList;
    }

    public List<Practice> getPracticeList(){
        List<Practice> practiceList = сreatePracticeList();

        setChartLinks(practiceList);
        setChartDoneLinks(practiceList);
        setInfoData(practiceList);

        Log.i(TAG,"getPracticeList called, practiceList size is " + practiceList.size());
        return practiceList;
    }

    private File getInfoFile(StorageReference folderReference){
        File infoFile = null;
        try {
            infoFile = File.createTempFile("info","txt");
            folderReference.child("info.txt").getFile(infoFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } catch (IOException e) {
            Log.e(TAG,"Failed info file loading",e);
        }
        return infoFile;
    }
    private String getChartLink(StorageReference folderReference){
        StorageReference imageReference = folderReference.child("chart.PNG");
        final String[] chartLink = new String[1];

        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i(TAG,"getChartLink method is called, link is " +uri.toString());
                chartLink[0] = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"fail to get image url",e);
            }
        });
        return chartLink[0];
    }
    private String getChartDoneLink(StorageReference folderReference){
        StorageReference imageReference = folderReference.child("chartDone.PNG");
        final String[] chartLink = new String[1];

        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i(TAG,"getChartDoneLink method is called, link is "+ uri.toString());
                chartLink[0] = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"fail to get image url",e);
            }
        });
        return chartLink[0];
    }
    private void setChartLinks(List<Practice> practiceList){
        for (Practice practice : practiceList){
            int index = practiceList.indexOf(practice);
            String link = getChartLink(foldersList.get(index));
            practice.setChartUrl(link);

            Log.i(TAG,"setChartLink Method call, link is: " + link);
        }
    }
    private void setChartDoneLinks(List<Practice> practiceList){
        for (Practice practice : practiceList){
            int index = practiceList.indexOf(practice);
            String link = getChartDoneLink(foldersList.get(index));
            practice.setChartUrl(link);
            Log.i(TAG,"setChartDoneLink Method call, link is: " + link);

        }
    }
    private void setInfoData(List<Practice> practiceList){
        for (Practice practice : practiceList){
            int index = practiceList.indexOf(practice);
            File infoFile = getInfoFile(foldersList.get(index));

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
}

*/
