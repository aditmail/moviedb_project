package com.example.movieproject.feature.detail_screen.fragment_screen.contract.presenter;

import android.content.Context;
import android.util.Log;

import com.example.movieproject.adapter.ReviewsAdapter;
import com.example.movieproject.base.ui.BasePresenter;
import com.example.movieproject.feature.detail_screen.fragment_screen.contract.FragmentView;
import com.example.movieproject.models.movies_review.MoviesReview;
import com.example.movieproject.models.movies_review.ResultsItemReview;
import com.example.movieproject.network.NetworkResponseListener;

import java.util.List;

public class ReviewsPresenter extends BasePresenter<FragmentView.ReviewsView> {

    private Context context;

    public ReviewsPresenter(FragmentView.ReviewsView view, Context context) {
        this.context = context;
        super.attachView(view, context);
    }

    public void getReviewList(int position, int currentPage, ReviewsAdapter adapter, String movieID) {
        apiClient.getMovieReviewList(currentPage, movieID, apiStores, new NetworkResponseListener<MoviesReview>() {
            @Override
            public void onResponseReceived(MoviesReview moviesReview) {
                if (moviesReview != null) {
                    List<ResultsItemReview> results = moviesReview.getResults();
                    if (position == 1) {
                        adapter.addAll(results);
                    } else if (position == 2) {
                        adapter.removeLoadingFooter();
                        adapter.addAll(results);
                    }

                    if (view != null) {
                        view.showListData(moviesReview, adapter, position);
                    }
                }
            }

            @Override
            public void onError(String errorMsg) {
                view.showMessage(errorMsg, 1);
            }
        });
    }
}
