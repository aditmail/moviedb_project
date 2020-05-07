package com.example.movieproject.feature.detail_screen.fragment_screen.contract;

import com.example.movieproject.adapter.ReviewsAdapter;
import com.example.movieproject.models.movies_review.MoviesReview;
import com.example.movieproject.models.movies_video.MovieVideos;

public interface FragmentView {

    interface VideosView {
        void showLoading();

        void hideLoading();

        void showMessage(String message, int position);

        void showListData(MovieVideos movieVideos);
    }

    interface ReviewsView {
        void showMessage(String message, int position);

        void showListData(MoviesReview review, ReviewsAdapter adapter, int position);
    }
}
