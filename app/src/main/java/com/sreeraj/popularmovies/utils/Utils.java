package com.sreeraj.popularmovies.utils;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.sreeraj.popularmovies.R;

/**
 * Created by Sreeraj on 2/17/16.
 */
public class Utils {

    public static void showToast(int messageId, Context context) {
        Toast.makeText(context, context.getResources().getString(messageId), Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Dialog showProgressDialog(Context context) {
        Dialog progressDialog = new Dialog(context, android.R.style.Theme_Black);
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_borderless, null);

        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent_progress_dialog);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }
}
