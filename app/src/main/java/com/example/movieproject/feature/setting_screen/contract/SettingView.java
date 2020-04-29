package com.example.movieproject.feature.setting_screen.contract;

import java.util.HashMap;

public interface SettingView {

    void showDataPreferences(HashMap<String, String> user);

    void showAlertDialog(String message, String title, int id);
}
