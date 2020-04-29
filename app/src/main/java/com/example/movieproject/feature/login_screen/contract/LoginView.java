package com.example.movieproject.feature.login_screen.contract;

import android.content.Intent;

public interface LoginView {

    void showLoading();

    void hideLoading();

    void moveToIntent(Intent intent, boolean isFinished);

    void showMessage(String message);

    void setErrorFocused(int widget);
}
