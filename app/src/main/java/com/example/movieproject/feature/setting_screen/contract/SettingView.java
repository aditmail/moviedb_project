package com.example.movieproject.feature.setting_screen.contract;

import android.widget.EditText;

import java.util.HashMap;

public interface SettingView {

    void showDataPreferences(HashMap<String, String> user);

    void showAlertDialog(String message, String title, int id);

    void setTextData(EditText editText, String string);
}
