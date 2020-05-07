package com.example.movieproject.base.mvp;

import android.os.Bundle;
import android.view.View;

import com.example.movieproject.base.ui.BaseFragment;
import com.example.movieproject.base.ui.BasePresenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {

    protected P presenter;

    protected abstract P createPresenter();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dettachView();
        }
    }
}
