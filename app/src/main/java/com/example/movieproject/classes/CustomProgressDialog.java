package com.example.movieproject.classes;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.movieproject.R;

public class CustomProgressDialog {

    public static CustomProgressDialog dialog = null;
    private Dialog mDialog;

    public static CustomProgressDialog getInstance() {
        if (dialog == null) {
            dialog = new CustomProgressDialog();
        }

        return dialog;
    }

    public void showProgress(Context context, boolean cancelable) {
        mDialog = new Dialog(context);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_progress_dialog);

        if (mDialog.getWindow() != null)
            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvLoading = mDialog.findViewById(R.id.tvLoading);
        tvLoading.setVisibility(View.VISIBLE);

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.cancel();
            mDialog = null;
        }
    }


}
