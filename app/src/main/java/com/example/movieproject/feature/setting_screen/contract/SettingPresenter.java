package com.example.movieproject.feature.setting_screen.contract;

import android.content.Context;

import com.example.movieproject.R;
import com.example.movieproject.base.ui.BasePresenter;

import java.util.HashMap;

public class SettingPresenter extends BasePresenter<SettingView> {

    private Context context;

    public SettingPresenter(SettingView view, Context context) {
        this.context = context;
        super.attachView(view, context);
    }

    public void getUserData() {
        if (preferencesManager != null) {
            HashMap<String, String> user = preferencesManager.getUserDetails();
            view.showDataPreferences(user);
        }
    }

    public void dialogLogout() {
        view.showAlertDialog(context.getString(R.string.log_out_message),
                context.getString(R.string.log_out), 1);
    }

    public void logout() {
        if (preferencesManager != null) {
            preferencesManager.logoutUser();
        }
    }
}
