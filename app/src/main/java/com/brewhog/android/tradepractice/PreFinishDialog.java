package com.brewhog.android.tradepractice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PreFinishDialog extends DialogFragment {
    private static final String ARG_MESSAGE = "message";

    public static PreFinishDialog newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE,message);

        PreFinishDialog fragment = new PreFinishDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String message = getArguments().getString(ARG_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onCancel(dialogInterface);
                    }
                })
                .create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        getActivity().finish();
    }
}