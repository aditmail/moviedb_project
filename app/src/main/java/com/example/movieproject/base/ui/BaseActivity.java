package com.example.movieproject.base.ui;

import android.app.Activity;
import android.content.Context;

import com.example.movieproject.classes.CustomProgressDialog;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    public Activity activity;
    public Context getBaseContext;
    public CustomProgressDialog progressDialog;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.activity = this;
        this.getBaseContext = this;

        progressDialog = CustomProgressDialog.getInstance();
        ButterKnife.bind(this);
    }
}
