package com.example.movieproject.feature.main_screen.contract;

import android.content.Intent;

import com.example.movieproject.adapter.PopularAdapter;
import com.example.movieproject.adapter.TopRatedAdapter;
import com.example.movieproject.adapter.UpcomingAdapter;

public interface MainView {
    void showLoading();

    void hideLoading();

    void showMessage(String message, int position);

    void showListTopRated(TopRatedAdapter adapter, int position);

    void showListPopular(PopularAdapter adapter, int position);

    void showListUpcoming(UpcomingAdapter adapter, int position);

    void moveToIntent(Intent intent);
}
