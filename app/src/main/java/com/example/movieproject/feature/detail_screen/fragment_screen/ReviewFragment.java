package com.example.movieproject.feature.detail_screen.fragment_screen;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieproject.R;
import com.example.movieproject.adapter.ReviewsAdapter;
import com.example.movieproject.base.mvp.MvpFragment;
import com.example.movieproject.feature.detail_screen.activity_screen.DetailsActivity;
import com.example.movieproject.feature.detail_screen.fragment_screen.contract.FragmentView;
import com.example.movieproject.feature.detail_screen.fragment_screen.contract.presenter.ReviewsPresenter;
import com.example.movieproject.models.movies_review.MoviesReview;
import com.example.movieproject.utils.EndlessRecyclerViewScrollListener;
import com.example.movieproject.utils.NetworkUtil;
import com.example.movieproject.utils.PaginationAdapterCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ReviewFragment extends MvpFragment<ReviewsPresenter>
        implements FragmentView.ReviewsView, PaginationAdapterCallback {

    //Review
    @BindView(R.id.tvErrorReview)
    TextView tvErrorReview;
    @BindView(R.id.tvNoReview)
    TextView tvNoReview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rvReviewList)
    RecyclerView rvReviewList;

    private ReviewsPresenter reviewsPresenter; //Init Presenter
    private String movieID; //Init String

    //Pagination
    private int currentPage = 1;
    private int TOTAL_PAGE = 0;

    private boolean isLastPage = false;
    private boolean isNoConnection = false;

    private LinearLayoutManager layoutManager;
    private ReviewsAdapter adapter;

    public ReviewFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        reviewsPresenter = new ReviewsPresenter(this, context);
    }

    @Override
    protected ReviewsPresenter createPresenter() {
        return reviewsPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.movieID = DetailsActivity.movieID;

        adapter = new ReviewsAdapter(getActivity());
        layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvReviewList.setLayoutManager(layoutManager);
        rvReviewList.hasFixedSize();
        rvReviewList.setItemAnimator(new DefaultItemAnimator());
        rvReviewList.setPadding(10, 0, 5, 10);

        callAPIReview();
    }

    private void callAPIReview() {
        if (NetworkUtil.hasNetwork(activity)) {
            tvErrorReview.setVisibility(View.GONE);
            presenter.getReviewList(1, currentPage, adapter, movieID);
        } else {
            progressBar.setVisibility(View.GONE);
            tvErrorReview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(String message, int position) {
        if (position == 1) {
            progressBar.setVisibility(View.GONE);
            tvErrorReview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showListData(MoviesReview review, ReviewsAdapter adapter, int position) {
        progressBar.setVisibility(View.GONE);
        if (!review.getResults().toString().equalsIgnoreCase("[]")) {
            if (position == 1) {
                rvReviewList.setAdapter(adapter);
                TOTAL_PAGE = review.getTotalPages();
                if (currentPage != TOTAL_PAGE) {
                    loadMoreData();
                }

            } else if (position == 2) {
                if (currentPage == TOTAL_PAGE) {
                    isLastPage = true;
                    adapter.removeLoadingFooter();
                } else {
                    adapter.addLoadingFooter();
                }
            }
            Toast.makeText(activity, getString(R.string.page_of, String.valueOf(currentPage),
                    String.valueOf(TOTAL_PAGE)), Toast.LENGTH_SHORT).show();
        } else {
            tvNoReview.setVisibility(View.VISIBLE);
        }
    }

    private void loadMoreData() {
        rvReviewList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount, RecyclerView view) {
                currentPage = page;
                loadNextData();
            }
        });
    }

    private void loadNextData() {
        if (!isLastPage) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.getReviewList(2, currentPage, adapter, movieID);
                }
            }, 3000);
        } else {
            //Last Page
        }
    }


    @Override
    public void retryPageLoad() {
        loadNextData();
    }

    @Override
    public void isNoConnection(boolean status) {
        isNoConnection = status;
    }
}
