package com.example.movieproject.feature.detail_screen.fragment_screen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieproject.R;
import com.example.movieproject.adapter.MovieVideosAdapter;
import com.example.movieproject.base.mvp.MvpFragment;
import com.example.movieproject.feature.detail_screen.activity_screen.DetailsActivity;
import com.example.movieproject.feature.detail_screen.fragment_screen.contract.FragmentView;
import com.example.movieproject.feature.detail_screen.fragment_screen.contract.presenter.VideosPresenter;
import com.example.movieproject.models.movies_video.MovieVideos;
import com.example.movieproject.utils.NetworkUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class VideosFragment extends MvpFragment<VideosPresenter>
        implements FragmentView.VideosView {

    //Videos
    @BindView(R.id.tvErrorVideo)
    TextView tvErrorVideo;
    @BindView(R.id.tvNoVideo)
    TextView tvNoVideo;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rvMoviesList)
    RecyclerView rvMoviesList;

    //Init Presenter
    private VideosPresenter videosPresenter;

    private String movieID;

    public VideosFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        videosPresenter = new VideosPresenter(this, context);
    }

    @Override
    protected VideosPresenter createPresenter() {
        return videosPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_videos, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.movieID = DetailsActivity.movieID;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMoviesList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rvMoviesList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        callAPIVideo();
    }

    private void callAPIVideo() {
        if (NetworkUtil.hasNetwork(activity)) {
            tvErrorVideo.setVisibility(View.GONE);
            presenter.getMovieList(movieID);

        } else {
            progressBar.setVisibility(View.GONE);
            tvErrorVideo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message, int position) {
        if (position == 1) {
            progressBar.setVisibility(View.GONE);
            tvErrorVideo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showListData(MovieVideos movieVideos) {
        progressBar.setVisibility(View.GONE);

        if (!movieVideos.getResults().toString().equalsIgnoreCase("[]")) {
            rvMoviesList.setAdapter(new MovieVideosAdapter(movieVideos, activity));
        } else {
            tvNoVideo.setVisibility(View.VISIBLE);
        }
    }
}
