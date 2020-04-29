package com.example.movieproject.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.movieproject.feature.login_screen.LoginActivity;

import java.util.HashMap;

public class PreferencesManager {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MovieProject_Pref";

    //Set Default Login
    private static final String IS_LOGGED_IN = "isLoggedIn";

    //Key Preferences
    public static final String KEY_ID_USER = "pref_user_id";
    public static final String KEY_NO_HP = "pref_no_hp";
    public static final String KEY_NAMA = "pref_nama";
    public static final String KEY_EMAIL = "pref_email";
    public static final String KEY_PASSWORD = "pref_password";

    public PreferencesManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = preferences.edit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void loginSession(String userID, String phoneNumber, String username, String email) {
        editor.putBoolean(IS_LOGGED_IN, true);

        editor.putString(KEY_ID_USER, userID);
        editor.putString(KEY_NO_HP, phoneNumber);
        editor.putString(KEY_NAMA, username);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    //Stored Session Data
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ID_USER, preferences.getString(KEY_ID_USER, null));
        user.put(KEY_NO_HP, preferences.getString(KEY_NO_HP, null));
        user.put(KEY_NAMA, preferences.getString(KEY_NAMA, null));
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        return user;
    }

    //Do Logout Activity
    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent intent_logout = new Intent(context, LoginActivity.class);
        intent_logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent_logout);
    }
}
