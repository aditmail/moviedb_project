package com.example.movieproject.feature.detail_screen.contract;

import com.example.movieproject.models.movies_video.MovieVideos;

public interface DetailView {
    void showLoading();

    void hideLoading();

    void showMessage(String message, int position);

    void showListData(MovieVideos movieVideos);
}
