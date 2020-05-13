package com.example.movieproject.feature.register_screen.contract;

import android.content.Intent;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public interface RegisterView {
    void setErrorFocused(int widget);

    void showMessage(String message);

    void showLoading();

    void hideLoading();

    void moveToIntent(Intent intent);

    void showAlertDialog(String message, String title, int id);

    void inputError(TextInputLayout inputLayout, String errorMsg, EditText editText);

    void inputErrorNull(TextInputLayout inputLayout);

    String getInputText(EditText editText);
}
