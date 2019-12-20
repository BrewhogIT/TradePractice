package com.brewhog.android.tradepractice;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;

public class PracticePack {
    public static final String TAG = "PracticePack";

    public PracticePack() {
    }

    public static void getAllFolders(){
        Log.i(TAG,"START getAllFolders METHOD");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference chartsRef = storage.getReference().child("charts");

        chartsRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()){
                            Log.i(TAG,"prefix: " + prefix.toString());
                        }

                        for (StorageReference items : listResult.getItems()){
                            Log.i(TAG,"item: " + items.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,"FAILED GET LIST ");
                    }
                });
    }
}
