package com.ryanliu.hw9_v13;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by RyanLiu on 11/21/17.
 */

public class MyToolbox {

    public static void show_alert(Context context, String message ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }



}
