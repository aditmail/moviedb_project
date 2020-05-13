package com.example.movieproject.feature.login_screen.contract;

import android.content.Intent;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public interface LoginView {

    void showLoading();

    void hideLoading();

    void moveToIntent(Intent intent, boolean isFinished);

    void showMessage(String message);

    void setErrorFocused(int widget);

    void inputError(TextInputLayout inputLayout, String errorMsg, EditText editText);

    void inputErrorNull(TextInputLayout inputLayout);
}
