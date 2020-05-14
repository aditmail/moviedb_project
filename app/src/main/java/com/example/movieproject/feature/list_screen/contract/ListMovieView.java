package com.example.movieproject.feature.list_screen.contract;

import android.content.Intent;

import com.example.movieproject.adapter.PopularAdapter;
import com.example.movieproject.adapter.TopRatedAdapter;
import com.example.movieproject.adapter.UpcomingAdapter;
import com.example.movieproject.models.popular.Popular;
import com.example.movieproject.models.top_rated.ResultsItemTopRated;
import com.example.movieproject.models.top_rated.TopRated;
import com.example.movieproject.models.upcoming.ResultsItemUpcoming;
import com.example.movieproject.models.upcoming.Upcoming;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface ListMovieView {

    void showLoading();

    void hideLoading();

    void showMessage(String message);

    void showListTopRated(TopRated topRated, TopRatedAdapter adapter, int position);

    void showListPopular(Popular popular, PopularAdapter adapter, int position);

    void showListUpcoming(Upcoming upcoming, UpcomingAdapter adapter, int position);

    void moveToIntent(Intent intent);

    void showPageOf(String string);

    void setTitleToolbar(String string);

    void setClearData(RecyclerView.Adapter adapter);
}
