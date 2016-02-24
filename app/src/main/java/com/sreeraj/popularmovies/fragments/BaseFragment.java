package com.sreeraj.popularmovies.fragments;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.sreeraj.popularmovies.R;

/**
 * Base Fragment.
 */
public class BaseFragment extends Fragment {

    public static int apiCallsInProgress;
    public static Dialog progressDialog;

    public static void showProgressDialog(Context context) {
        apiCallsInProgress++;
        if (apiCallsInProgress == 1) {
            progressDialog = new Dialog(context, android.R.style.Theme_Black);
            View view = LayoutInflater.from(context).inflate(
                    R.layout.dialog_borderless, null);

            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    R.color.transparent_progress_dialog);
            progressDialog.setContentView(view);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    public static void hideProgressDialog() {
        apiCallsInProgress--;
        if (apiCallsInProgress == 0 && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
