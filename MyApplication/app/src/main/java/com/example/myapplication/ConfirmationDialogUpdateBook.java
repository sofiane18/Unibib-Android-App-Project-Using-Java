package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmationDialogUpdateBook extends AppCompatDialogFragment {

    private ConfirmationDialogUpdateBookListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Update...")
                .setIcon(R.drawable.ic_baseline_edit_24)
                .setMessage("Are you sure you want to edit this?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onUpdateBookYesClicked();
                    }
                });

        return builder.create();
    }

    public interface ConfirmationDialogUpdateBookListener{
        void onUpdateBookYesClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmationDialogUpdateBookListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement ConfirmationDialogUpdateBookListener");
        }
    }
}

