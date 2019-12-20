package com.brewhog.android.tradepractice;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PracticeListFragment extends Fragment {
    private static final String TAG = "PracticeListFragment";
    private ImageView lessonKindIllustration;
    private RecyclerView practiceListRecyclerView;
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            PracticePack.getAllFolders();
        }else{
            signInAnonymously();
        }

        return view;
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.e(TAG,"signInAnonymously:SUCCESS");

                PracticePack.getAllFolders();
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
