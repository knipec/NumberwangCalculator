package com.knipec.numberwangcalc.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by maegereg on 4/7/14.
 */
public class SettingsDialogue extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Why on earth would you need settings?")
               .setPositiveButton("I recognize my mistake.", new DialogInterface.OnClickListener(){

                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       //There is nothing to do
                   }
               });
        return builder.create();
    }
}
