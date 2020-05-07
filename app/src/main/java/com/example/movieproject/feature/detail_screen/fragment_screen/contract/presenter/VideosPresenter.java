package com.example.movieproject.feature.detail_screen.fragment_screen.contract.presenter;

import android.content.Context;

import com.example.movieproject.base.ui.BasePresenter;
import com.example.movieproject.feature.detail_screen.fragment_screen.contract.FragmentView;
import com.example.movieproject.models.movies_video.MovieVideos;
import com.example.movieproject.network.NetworkResponseListener;

public class VideosPresenter extends BasePresenter<FragmentView.VideosView> {

    private Context context;

    public VideosPresenter(FragmentView.VideosView view, Context context) {
        this.context = context;
        super.attachView(view, context);
    }

    public void getMovieList(String movieID) {
        apiClient.getMovieVideoList(movieID, apiStores, new NetworkResponseListener<MovieVideos>() {
            @Override
            public void onResponseReceived(MovieVideos movieVideos) {
                if (movieVideos != null) {
                    view.showListData(movieVideos);
                }
            }

            @Override
            public void onError(String errorMsg) {
                view.showMessage(errorMsg, 1);
            }
        });
    }


}
