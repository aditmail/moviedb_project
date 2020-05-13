package com.example.movieproject.feature.list_screen.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.movieproject.adapter.PopularAdapter;
import com.example.movieproject.adapter.TopRatedAdapter;
import com.example.movieproject.adapter.UpcomingAdapter;
import com.example.movieproject.base.ui.BasePresenter;
import com.example.movieproject.feature.detail_screen.activity_screen.DetailsActivity;
import com.example.movieproject.models.popular.Popular;
import com.example.movieproject.models.popular.ResultsItemPopular;
import com.example.movieproject.models.top_rated.ResultsItemTopRated;
import com.example.movieproject.models.top_rated.TopRated;
import com.example.movieproject.models.upcoming.ResultsItemUpcoming;
import com.example.movieproject.models.upcoming.Upcoming;
import com.example.movieproject.network.NetworkResponseListener;

import java.util.List;

public class ListMoviePresenter extends BasePresenter<ListMovieView> {

    private Context context;

    public ListMoviePresenter(ListMovieView view, Context context) {
        this.context = context;
        super.attachView(view, context);
    }

    public void callAPITopRated(int position, int currentPage, TopRatedAdapter adapter) {
        if (position == 1)
            view.showLoading();

        apiClient.getTopRated(currentPage, apiStores, new NetworkResponseListener<TopRated>() {
            @Override
            public void onResponseReceived(TopRated topRated) {
                if (topRated != null) {
                    List<ResultsItemTopRated> results = topRated.getResults();
                    if (position == 1) {
                        adapter.addAllData(results);
                    } else if (position == 2) {
                        adapter.removeLoadingFooter();
                        adapter.addAllData(results);
                    }

                    if (view != null)
                        view.showListTopRated(topRated, adapter, position);
                }

                if (position == 1)
                    if (view != null)
                        view.hideLoading();
            }

            @Override
            public void onError(String errorMsg) {
                if (view != null)
                    view.showMessage(errorMsg);
                if (position == 1)
                    if (view != null)
                        view.hideLoading();
                    else
                        adapter.showRetry(true, "");
            }
        });
    }

    public void callAPIPopular(int position, int currentPage, PopularAdapter adapter) {
        if (position == 1)
            if (view != null)
                view.showLoading();

        apiClient.getPopular(currentPage, apiStores, new NetworkResponseListener<Popular>() {
            @Override
            public void onResponseReceived(Popular popular) {
                if (popular != null) {

                    List<ResultsItemPopular> results = popular.getResults();
                    if (position == 1) {
                        adapter.addAllData(results);
                    } else if (position == 2) {
                        adapter.removeLoadingFooter();
                        adapter.addAllData(results);
                    }

                    if (view != null)
                        view.showListPopular(popular, adapter, position);
                }

                if (position == 1)
                    if (view != null)
                        view.hideLoading();
            }

            @Override
            public void onError(String errorMsg) {
                if (view != null)
                    view.showMessage(errorMsg);
                if (position == 1) {
                    if (view != null) {
                        view.hideLoading();
                    }
                } else {
                    adapter.showRetry(true, "");
                }
            }
        });
    }

    public void callAPIUpcoming(int position, int currentPage, UpcomingAdapter adapter) {
        if (position == 1)
            if (view != null)
                view.showLoading();

        apiClient.getUpcoming(currentPage, apiStores, new NetworkResponseListener<Upcoming>() {
            @Override
            public void onResponseReceived(Upcoming upcoming) {
                if (upcoming != null) {
                    List<ResultsItemUpcoming> results = upcoming.getResults();

                    if (position == 1) {
                        adapter.addAllData(results);
                    } else if (position == 2) {
                        adapter.removeLoadingFooter();
                        adapter.addAllData(results);
                    }
                    if (view != null)
                        view.showListUpcoming(upcoming, adapter, position);
                }

                if (position == 1) {
                    if (view != null)
                        view.hideLoading();
                } else {
                    adapter.showRetry(true, "");
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (view != null)
                    view.showMessage(errorMsg);
                if (position == 1)
                    if (view != null)
                        view.hideLoading();
            }
        });
    }

    public void getItemPopular(ResultsItemPopular itemPopular, Activity activity, Class activityTo) {
        Intent intent = new Intent(activity, activityTo);
        intent.putExtra(DetailsActivity.extraModel, 1);
        intent.putExtra(DetailsActivity.extraMoviesData, itemPopular);
        if (view != null)
            view.moveToIntent(intent);
    }

    public void getItemTopRated(ResultsItemTopRated itemTopRated, Activity activity, Class activityTo) {
        Intent intent = new Intent(activity, activityTo);
        intent.putExtra(DetailsActivity.extraModel, 2);
        intent.putExtra(DetailsActivity.extraMoviesData, itemTopRated);
        if (view != null)
            view.moveToIntent(intent);
    }

    public void getItemUpcoming(ResultsItemUpcoming itemUpcoming, Activity activity, Class activityTo) {
        Intent intent = new Intent(activity, activityTo);
        intent.putExtra(DetailsActivity.extraModel, 3);
        intent.putExtra(DetailsActivity.extraMoviesData, itemUpcoming);
        if (view != null)
            view.moveToIntent(intent);
    }
}
