package com.example.movieproject.feature.login_screen.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.movieproject.R;
import com.example.movieproject.base.ui.BasePresenter;
import com.example.movieproject.models.register_form.RegisterForm;
import com.example.movieproject.utils.DBHelper;
import com.example.movieproject.utils.PreferencesManager;

import java.util.HashMap;

public class LoginPresenter extends BasePresenter<LoginView> {

    private Context context;
    private DBHelper dbHelper;

    public LoginPresenter(LoginView view, Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);

        super.attachView(view, context);
    }

    public void goToIntent(Activity activity, Class activityTo, boolean isFinished) {
        Intent intent = new Intent(activity, activityTo);
        view.moveToIntent(intent, isFinished);
    }

    public void validateData(String phone, String password,
                             Activity activity, Class activityTo) {
        if (phone.isEmpty()) {
            view.setErrorFocused(1);
        } else if (phone.length() < 10 || phone.length() > 16) {
            view.setErrorFocused(2);
        } else if (!phone.startsWith("08")) {
            view.setErrorFocused(3);
        } else if (password.isEmpty()) {
            view.setErrorFocused(4);
        } else {
            view.setErrorFocused(0);
            view.showLoading();
            getDataLogin(phone, password, activity, activityTo);
        }
    }

    private void getDataLogin(String phone, String password,
                              Activity activity, Class activityTo) {
        RegisterForm form = dbHelper.getLoginData(phone, password);
        view.hideLoading();

        if (form != null) {
            String userID = form.getIdUser();
            String phoneNo = form.getPhoneNumber();
            String username = form.getFullName();
            String email = form.getEmail();

            view.showMessage(context.getString(R.string.welcome_message, username));
            preferencesManager.loginSession(userID, phoneNo, username, email);
            goToIntent(activity, activityTo, true);
        } else {
            view.showMessage(context.getString(R.string.error_login));
        }
    }

    public boolean checkLoggedIn() {
        if (preferencesManager.isLoggedIn()) {
            HashMap<String, String> user = preferencesManager.getUserDetails();
            String username = user.get(PreferencesManager.KEY_NAMA);

            view.showMessage(context.getString(R.string.welcome_message, username));
            return true;
        } else {
            return false;
        }
    }
}
