package com.example.movieproject.feature.main_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieproject.R;
import com.example.movieproject.adapter.PopularAdapter;
import com.example.movieproject.adapter.TopRatedAdapter;
import com.example.movieproject.adapter.UpcomingAdapter;
import com.example.movieproject.base.mvp.MvpActivity;
import com.example.movieproject.feature.detail_screen.activity_screen.DetailsActivity;
import com.example.movieproject.feature.list_screen.ListMovieActivity;
import com.example.movieproject.feature.main_screen.contract.MainPresenter;
import com.example.movieproject.feature.main_screen.contract.MainView;
import com.example.movieproject.feature.setting_screen.SettingActivity;
import com.example.movieproject.utils.NetworkUtil;
import com.example.movieproject.utils.PaginationAdapterCallback;
import com.example.movieproject.utils.RecyclerItemClickListener;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView, View.OnClickListener, PaginationAdapterCallback {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.linearNoInternet)
    LinearLayout linearNoInternet;
    @BindView(R.id.btnRefresh)
    Button btnRefresh;

    //Popular Segmentation
    @BindView(R.id.framePopular)
    LinearLayout framePopular;
    @BindView(R.id.tvLoadMorePopular)
    TextView tvLoadMorePopular;
    @BindView(R.id.rvPopularList)
    RecyclerView rvPopularList;
    @BindView(R.id.progressBarPopular)
    ProgressBar progressBarPopular;
    @BindView(R.id.tvErrorPopular)
    TextView tvErrorPopular;

    //Top Rated Segmentation
    @BindView(R.id.frameTopRated)
    LinearLayout frameTopRated;
    @BindView(R.id.tvLoadMoreTopR)
    TextView tvLoadMoreTopR;
    @BindView(R.id.rvTopRatedList)
    RecyclerView rvTopRatedList;
    @BindView(R.id.progressBarTopR)
    ProgressBar progressBarTopR;
    @BindView(R.id.tvErrorTopR)
    TextView tvErrorTopR;

    //Upcoming Segmentation
    @BindView(R.id.frameUpcoming)
    LinearLayout frameUpcoming;
    @BindView(R.id.tvLoadMoreUpcoming)
    TextView tvLoadMoreUpcoming;
    @BindView(R.id.rvUpcomingList)
    RecyclerView rvUpcomingList;
    @BindView(R.id.progressBarUpcoming)
    ProgressBar progressBarUpcoming;
    @BindView(R.id.tvErrorUpcoming)
    TextView tvErrorUpcoming;

    private PopularAdapter adapterPopular;
    private TopRatedAdapter adapterTopRated;
    private UpcomingAdapter adapterUpcoming;

    private int currentPage = 1;
    private boolean isLoggedIn = false;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapterPopular = new PopularAdapter(this, 1);
        adapterTopRated = new TopRatedAdapter(this, 1);
        adapterUpcoming = new UpcomingAdapter(this, 1);

        //Load More
        tvLoadMorePopular.setOnClickListener(this);
        tvLoadMoreTopR.setOnClickListener(this);
        tvLoadMoreUpcoming.setOnClickListener(this);

        //Connection Error - Reload
        tvErrorPopular.setOnClickListener(this);
        tvErrorTopR.setOnClickListener(this);
        tvErrorUpcoming.setOnClickListener(this);

        //Refresh
        btnRefresh.setOnClickListener(this);

        mSwipeRefresh.setOnRefreshListener(() -> {
            callAPIMovies();
            mSwipeRefresh.setRefreshing(false);
        });

        isLoggedIn = presenter.isLoggedIn();

        callAPIMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isLoggedIn) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.view_setting:
                presenter.goToIntent(activity, SettingActivity.class, 0);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void callAPIMovies() {
        if (NetworkUtil.hasNetwork(this)) {
            linearNoInternet.setVisibility(View.GONE);
            mSwipeRefresh.setVisibility(View.VISIBLE);

            presenter.callAPIPopular(1, currentPage, adapterPopular);
            presenter.callAPITopRated(1, currentPage, adapterTopRated);
            presenter.callAPIUpcoming(1, currentPage, adapterUpcoming);
        } else {
            linearNoInternet.setVisibility(View.VISIBLE);
            mSwipeRefresh.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLoadMorePopular:
                presenter.goToIntent(this, ListMovieActivity.class, 1);
                break;

            case R.id.tvLoadMoreTopR:
                presenter.goToIntent(this, ListMovieActivity.class, 2);
                break;

            case R.id.tvLoadMoreUpcoming:
                presenter.goToIntent(this, ListMovieActivity.class, 3);
                break;

            case R.id.btnRefresh:
                callAPIMovies();
                break;

            default:
                break;
        }
    }

    @Override
    public void showListPopular(PopularAdapter adapter, int position) {
        if (position == 1) {
            rvPopularList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvPopularList.setItemAnimator(new DefaultItemAnimator());
            rvPopularList.setPadding(10, 0, 5, 15);
            rvPopularList.addOnItemTouchListener(selectedItem(1));

            //Parse the Data to show in RecyclerView
            progressBarPopular.setVisibility(View.GONE);
            rvPopularList.setAdapter(adapter);
        }
    }

    @Override
    public void showListUpcoming(UpcomingAdapter adapter, int position) {
        if (position == 1) {
            rvUpcomingList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvUpcomingList.setItemAnimator(new DefaultItemAnimator());
            rvUpcomingList.setPadding(10, 0, 5, 15);
            rvUpcomingList.addOnItemTouchListener(selectedItem(3));

            progressBarUpcoming.setVisibility(View.GONE);
            rvUpcomingList.setAdapter(adapter);
        }

    }

    @Override
    public void showListTopRated(TopRatedAdapter adapter, int position) {
        if (position == 1) {
            rvTopRatedList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvTopRatedList.setItemAnimator(new DefaultItemAnimator());
            rvTopRatedList.setPadding(10, 0, 5, 15);
            rvTopRatedList.addOnItemTouchListener(selectedItem(2));

            progressBarTopR.setVisibility(View.GONE);
            rvTopRatedList.setAdapter(adapter);
        }
    }

    private RecyclerItemClickListener selectedItem(int position) {
        RecyclerItemClickListener listener = null;
        switch (position) {
            case 1: //For Popular Movie
                listener = new RecyclerItemClickListener(this, rvPopularList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        presenter.getItemPopular(adapterPopular.getItem(position),
                                MainActivity.this, DetailsActivity.class);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                });
                break;

            case 2: //For TopRated Movie
                listener = new RecyclerItemClickListener(this, rvTopRatedList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        presenter.getItemTopRated(adapterTopRated.getItem(position),
                                MainActivity.this, DetailsActivity.class);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                });
                break;
            case 3: //For Upcoming Movie
                listener = new RecyclerItemClickListener(this, rvUpcomingList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        presenter.getItemUpcoming(adapterUpcoming.getItem(position),
                                MainActivity.this, DetailsActivity.class);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                });
                break;
        }

        return listener;
    }

    @Override
    public void moveToIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        progressDialog.showProgress(activity, false);
    }

    @Override
    public void hideLoading() {
        progressDialog.hideProgress();
    }

    @Override
    public void showMessage(String message, int position) {
        switch (position) {
            case 1: //Error Handling for Popular Movie List
                tvErrorPopular.setVisibility(View.VISIBLE);
                progressBarPopular.setVisibility(View.GONE);
                break;

            case 2: //Error Handling for Top Rated Movie List
                tvErrorTopR.setVisibility(View.VISIBLE);
                progressBarTopR.setVisibility(View.GONE);
                break;

            case 3: //Error Handling for Upcoming Movie List
                tvErrorUpcoming.setVisibility(View.VISIBLE);
                progressBarUpcoming.setVisibility(View.GONE);
                break;

            default:
                break;
        }

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void retryPageLoad() {
        //Use for Pagination Process
    }

    @Override
    public void isNoConnection(boolean status) {

    }
}
