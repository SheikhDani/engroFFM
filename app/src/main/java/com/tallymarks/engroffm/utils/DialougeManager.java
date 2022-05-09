package com.tallymarks.engroffm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Settings;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tallymarks.engroffm.R;


public class DialougeManager{

    public static void showSettingsDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.needpermission));
        builder.setMessage(context.getResources().getString(R.string.permissionmessage));
        builder.setPositiveButton(context.getResources().getString(R.string.gotosettings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(context);
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private static void openSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        ((Activity) context).startActivityForResult(intent, 101);
    }

    public static void gpsNotEnabledPopup(final Context context) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom_alert_dialouge, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                //ImageView ivClose = promptsView.findViewById(R.id.iv_close);
                TextView tvTitle = promptsView.findViewById(R.id.tv_option);
                TextView tvBody = promptsView.findViewById(R.id.tv_body);
                Button btnNo = promptsView.findViewById(R.id.btn_no);
                Button btnYes = promptsView.findViewById(R.id.btn_yes);
               // ivClose.setVisibility(View.GONE);
                btnNo.setVisibility(View.GONE);
                tvTitle.setText(R.string.message);
                tvBody.setText(R.string.gps_not_enabled);
                btnYes.setText(context.getResources().getString(R.string.enable));
                tvBody.setTextColor(context.getResources().getColor(R.color.black));
                tvBody.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        ((Activity) context).startActivity(intent);
                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    public static void showMockLocationDialog(final Context context) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom_alert_dialouge, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                //ImageView ivClose = promptsView.findViewById(R.id.iv_close);
                TextView tvTitle = promptsView.findViewById(R.id.tv_option);
                TextView tvBody = promptsView.findViewById(R.id.tv_body);
                Button btnNo = promptsView.findViewById(R.id.btn_no);
                Button btnYes = promptsView.findViewById(R.id.btn_yes);

                btnNo.setVisibility(View.GONE);
                tvTitle.setText(R.string.message);
                tvBody.setText(R.string.mock_location_enabled);
                btnYes.setText(context.getResources().getString(R.string.disable));
                tvBody.setTextColor(context.getResources().getColor(R.color.black));
                tvBody.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        LocationHelper.mockLocationIntent(context);
                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    public static void showInfoDialog(final Context context, final String title, final String body, final boolean refreshActivity) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom_alert_dialouge, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                //ImageView ivClose = promptsView.findViewById(R.id.iv_close);
                TextView tvTitle = promptsView.findViewById(R.id.tv_option);
                TextView tvBody = promptsView.findViewById(R.id.tv_body);
                Button btnNo = promptsView.findViewById(R.id.btn_no);
                Button btnYes = promptsView.findViewById(R.id.btn_yes);
                //ivClose.setVisibility(View.VISIBLE);
                btnNo.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
                tvBody.setText(body);
                btnYes.setText(context.getResources().getString(R.string.ok));
                tvBody.setTextColor(context.getResources().getColor(R.color.black));
                tvBody.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                //ivClose.setBackground(context.getResources().getDrawable(R.drawable.ic_info));

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        if (refreshActivity) {
                            ((Activity) context).finish();
                            context.startActivity(((Activity) context).getIntent());
                        }
                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialog.show();
            }
        });
    }
    public static void invalidCredentialsPopup(Context context,String title,String message)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



}
