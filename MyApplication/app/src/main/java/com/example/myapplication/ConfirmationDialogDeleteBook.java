package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmationDialogDeleteBook extends AppCompatDialogFragment {

    private ConfirmationDialogDeleteBookListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Delete...")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)
                .setMessage("Are you sure you want to delete this book?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDeleteBookYesClicked();
                    }
                });

        return builder.create();
    }

    public interface ConfirmationDialogDeleteBookListener{
        void onDeleteBookYesClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmationDialogDeleteBookListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement ConfirmationDialogDeleteBookListener");
        }
    }
}
