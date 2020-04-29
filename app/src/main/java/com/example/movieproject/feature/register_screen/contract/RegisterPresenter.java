package com.example.movieproject.feature.register_screen.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Patterns;

import com.example.movieproject.R;
import com.example.movieproject.base.ui.BasePresenter;
import com.example.movieproject.models.register_form.RegisterForm;
import com.example.movieproject.utils.DBHelper;

public class RegisterPresenter extends BasePresenter<RegisterView> {

    private Context context;

    public RegisterPresenter(RegisterView view, Context context) {
        this.context = context;
        super.attachView(view, context);
    }

    public void validateData(RegisterForm form) {
        if (form.getPhoneNumber().isEmpty()) {
            view.setErrorFocused(1);
        } else if (form.getPhoneNumber().length() < 10 || form.getPhoneNumber().length() > 16) {
            view.setErrorFocused(2);
        } else if (!form.getPhoneNumber().startsWith("08")) {
            view.setErrorFocused(3);
        } else if (form.getFullName().isEmpty()) {
            view.setErrorFocused(4);
        } else if (!form.getEmail().isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(form.getEmail()).matches()) {
            view.setErrorFocused(5);
        } else if (form.getPassword().isEmpty()) {
            view.setErrorFocused(6);
        } else if (form.getPassword().length() < 8) {
            view.setErrorFocused(7);
        } else if (form.getConfirmPassword().isEmpty()) {
            view.setErrorFocused(8);
        } else if (!form.getPassword().equals(form.getConfirmPassword())) {
            view.setErrorFocused(0);
            view.showMessage(context.getString(R.string.msg_password_not_match));
        } else {
            view.showLoading();
            registerUser(form);
        }
    }

    private void registerUser(RegisterForm form) {
        DBHelper dbHelper = new DBHelper(context);

        if (form != null) {
            dbHelper.addRecord(form);
            view.hideLoading();

            view.showAlertDialog(context.getString(R.string.registration_success),
                    context.getString(R.string.registration_title), 1);
        } else {
            view.showMessage(context.getString(R.string.registration_failed));
        }
    }

    public void goToLogin(Activity activity, Class data) {
        Intent intent = new Intent(activity, data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        view.moveToIntent(intent);
    }
}
