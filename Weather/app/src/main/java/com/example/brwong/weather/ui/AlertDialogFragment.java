package com.example.brwong.weather.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.example.brwong.weather.R;

/**
 * Created by Brwong on 5/8/15.
 */
public class AlertDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        //We don't need this line above because it generates a default dialog for us - we want to make our own AlertDialog
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error_title))
                .setMessage(context.getString(R.string.error_message))
                .setPositiveButton(context.getString(R.string.error_ok_button), null);//CHAINS ALL THIS TOGETHER - even with the new alertdialog instance
        AlertDialog dialog = builder.create();//ACTUALLY builds the dialog
        return dialog;//Return to the method call in our MainActivity

    }
}
