package com.tiendas3b.almacen.shipment.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tiendas3b.almacen.R;

public class DialogFactory {

    public static AlertDialog getSuccessDialog(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_dialog_success, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        builder.setCancelable(false)
                .setTitle("Mensaje")
                .setView(view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public static AlertDialog getErrorDialog(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_dialog_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        builder.setCancelable(false)
                .setTitle("Error")
                .setView(view)
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
        return builder.create();
    }

    public static AlertDialog getErrorDialogListener(Context context, String message, AlertDialogListener alertDialogListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_dialog_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        builder.setCancelable(false)
                .setTitle("Error")
                .setView(view)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    dialog.dismiss();
                    alertDialogListener.execute();
                });
        return builder.create();
    }

    public static AlertDialog getProgressDialog(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_progress, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setView(view)
                .setTitle(title);

        return builder.create();
    }
}
