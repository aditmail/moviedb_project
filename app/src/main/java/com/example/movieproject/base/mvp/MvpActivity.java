package com.example.movieproject.base.mvp;

import android.os.Bundle;

import com.example.movieproject.base.ui.BaseActivity;
import com.example.movieproject.base.ui.BasePresenter;

import androidx.annotation.Nullable;

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P presenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.presenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null)
            presenter.dettachView();

        super.onDestroy();
    }
}
