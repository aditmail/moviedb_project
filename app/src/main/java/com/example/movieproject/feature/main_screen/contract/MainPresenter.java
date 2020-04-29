package com.example.movieproject.feature.main_screen.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.movieproject.adapter.PopularAdapter;
import com.example.movieproject.adapter.TopRatedAdapter;
import com.example.movieproject.adapter.UpcomingAdapter;
import com.example.movieproject.base.ui.BasePresenter;
import com.example.movieproject.feature.detail_screen.DetailActivity;
import com.example.movieproject.feature.list_screen.ListMovieActivity;
import com.example.movieproject.models.popular.Popular;
import com.example.movieproject.models.popular.ResultsItemPopular;
import com.example.movieproject.models.top_rated.ResultsItemTopRated;
import com.example.movieproject.models.top_rated.TopRated;
import com.example.movieproject.models.upcoming.ResultsItemUpcoming;
import com.example.movieproject.models.upcoming.Upcoming;
import com.example.movieproject.network.NetworkResponseListener;

import java.util.List;

public class MainPresenter extends BasePresenter<MainView> {

    private Context context;

    public MainPresenter(MainView view, Context context) {
        this.context = context;
        super.attachView(view, context);
    }

    public void callAPITopRated(int position, int currentPage, TopRatedAdapter adapter) {
        apiClient.getTopRated(currentPage, apiStores, new NetworkResponseListener<TopRated>() {
            @Override
            public void onResponseReceived(TopRated topRated) {
                if (topRated != null) {
                    List<ResultsItemTopRated> results = topRated.getResults();
                    adapter.addAll(results);

                    if (view != null)
                        view.showListTopRated(adapter, position);
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (view != null)
                    view.showMessage(errorMsg, 2);
            }
        });
    }

    public void callAPIPopular(int position, int currentPage, PopularAdapter adapter) {
        apiClient.getPopular(currentPage, apiStores, new NetworkResponseListener<Popular>() {
            @Override
            public void onResponseReceived(Popular popular) {
                if (popular != null) {
                    List<ResultsItemPopular> results = popular.getResults();
                    adapter.addAll(results);

                    if (view != null)
                        view.showListPopular(adapter, position);
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (view != null)
                    view.showMessage(errorMsg, 1);
            }
        });
    }

    public void callAPIUpcoming(int position, int currentPage, UpcomingAdapter adapter) {
        apiClient.getUpcoming(currentPage, apiStores, new NetworkResponseListener<Upcoming>() {
            @Override
            public void onResponseReceived(Upcoming upcoming) {
                if (upcoming != null) {
                    List<ResultsItemUpcoming> results = upcoming.getResults();
                    adapter.addAll(results);

                    if (view != null)
                        view.showListUpcoming(adapter, position);
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (view != null)
                    view.showMessage(errorMsg, 3);
            }
        });
    }

    public void getItemPopular(ResultsItemPopular itemPopular, Activity activity, Class activityTo) {
        Intent intent = new Intent(activity, activityTo);
        intent.putExtra(DetailActivity.extraModel, 1);
        intent.putExtra(DetailActivity.extraMoviesData, itemPopular);
        if (view != null)
            view.moveToIntent(intent);
    }

    public void getItemTopRated(ResultsItemTopRated itemTopRated, Activity activity, Class activityTo) {
        Intent intent = new Intent(activity, activityTo);
        intent.putExtra(DetailActivity.extraModel, 2);
        intent.putExtra(DetailActivity.extraMoviesData, itemTopRated);
        if (view != null)
            view.moveToIntent(intent);
    }

    public void getItemUpcoming(ResultsItemUpcoming itemUpcoming, Activity activity, Class activityTo) {
        Intent intent = new Intent(activity, activityTo);
        intent.putExtra(DetailActivity.extraModel, 3);
        intent.putExtra(DetailActivity.extraMoviesData, itemUpcoming);
        if (view != null)
            view.moveToIntent(intent);
    }

    public void goToIntent(Activity activity, Class activityTo, int type) {
        Intent intent = new Intent(activity, activityTo);
        if (type != 0) {
            intent.putExtra(ListMovieActivity.IntentType, type);
            if (view != null)
                view.moveToIntent(intent);
        } else {
            if (view != null)
                view.moveToIntent(intent);
        }
    }
}
