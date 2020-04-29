package com.example.movieproject.base.ui;

import android.content.Context;

import com.example.movieproject.network.ApiClient;
import com.example.movieproject.network.ApiStores;
import com.example.movieproject.network.NetworkClient;
import com.example.movieproject.utils.PreferencesManager;

public class BasePresenter<V> {

    public V view;
    public Context context;

    protected ApiStores apiStores;
    protected ApiClient apiClient;
    protected PreferencesManager preferencesManager;

    public void attachView(V view, Context context) {
        this.view = view;
        this.context = context;

        preferencesManager = new PreferencesManager(context);
        apiStores = NetworkClient.getRetrofit(context).create(ApiStores.class);
        apiClient = new ApiClient(context);
    }

    public void dettachView() {
        this.view = null;
        this.context = null;
    }
}
