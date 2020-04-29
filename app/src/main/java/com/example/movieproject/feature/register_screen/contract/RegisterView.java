package com.example.movieproject.feature.register_screen.contract;

import android.content.Intent;

public interface RegisterView {
    void setErrorFocused(int widget);

    void showMessage(String message);

    void showLoading();

    void hideLoading();

    void moveToIntent(Intent intent);

    void showAlertDialog(String message, String title, int id);
}
