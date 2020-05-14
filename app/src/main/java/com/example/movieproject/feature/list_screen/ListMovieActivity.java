package com.example.movieproject.feature.list_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieproject.R;
import com.example.movieproject.adapter.PopularAdapter;
import com.example.movieproject.adapter.TopRatedAdapter;
import com.example.movieproject.adapter.UpcomingAdapter;
import com.example.movieproject.base.mvp.MvpActivity;
import com.example.movieproject.feature.detail_screen.activity_screen.DetailsActivity;
import com.example.movieproject.feature.list_screen.contract.ListMoviePresenter;
import com.example.movieproject.feature.list_screen.contract.ListMovieView;
import com.example.movieproject.models.popular.Popular;
import com.example.movieproject.models.top_rated.TopRated;
import com.example.movieproject.models.upcoming.Upcoming;
import com.example.movieproject.utils.EndlessScrollAnotherListener;
import com.example.movieproject.utils.NetworkUtil;
import com.example.movieproject.utils.PaginationAdapterCallback;
import com.example.movieproject.utils.RecyclerItemClickListener;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class ListMovieActivity extends MvpActivity<ListMoviePresenter>
        implements ListMovieView, PaginationAdapterCallback {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rvMovieList)
    RecyclerView rvMovieList;
    @BindView(R.id.linearNoInternet)
    LinearLayout linearNoInternet;
    @BindView(R.id.btnRefresh)
    Button btnRefresh;
    @BindView(R.id.cardPages)
    CardView cardPages;
    @BindView(R.id.tvPageOf)
    TextView tvPageOf;

    private int listType;
    public static String IntentType = "MOVIE_CATEGORY";

    private int currentPage = 1;
    private int TOTAL_PAGES = 0;

    private boolean isLastPage = false;
    private boolean isNoConnection = false;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private PopularAdapter adapterPopular;
    private TopRatedAdapter adapterTopRated;
    private UpcomingAdapter adapterUpcoming;

    @Override
    protected ListMoviePresenter createPresenter() {
        return new ListMoviePresenter(this, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        rvMovieList.setLayoutManager(staggeredGridLayoutManager);
        rvMovieList.hasFixedSize();
        rvMovieList.setItemAnimator(new DefaultItemAnimator());
        rvMovieList.setPadding(10, 0, 5, 15);

        Intent getIntent = getIntent();
        if (getIntent != null) {
            listType = getIntent.getIntExtra(IntentType, 1);
            switch (listType) {
                case 1:
                    setTitleToolbar(getString(R.string.popular));
                    adapterPopular = new PopularAdapter(this, 2);
                    break;

                case 2:
                    setTitleToolbar(getString(R.string.top_rated_title));
                    adapterTopRated = new TopRatedAdapter(this, 2);
                    break;

                case 3:
                    setTitleToolbar(getString(R.string.upcoming_movie_title));
                    adapterUpcoming = new UpcomingAdapter(this, 2);
                    break;

                default:
                    break;
            }

            //Refresh Button
            mSwipeRefresh.setOnRefreshListener(() -> {
                currentPage = 1;
                callAPIMovies(listType);
                mSwipeRefresh.setRefreshing(false);
            });

            //RecyclerView on Click
            rvMovieList.addOnItemTouchListener(selectedItem(listType));

            //Call API
            callAPIMovies(listType);
        }
    }

    private void callAPIMovies(int type) {
        if (NetworkUtil.isConnected(this)) {
            mSwipeRefresh.setVisibility(View.VISIBLE);
            linearNoInternet.setVisibility(View.GONE);
            switch (type) {
                case 1:
                    setClearData(adapterPopular); //Clearing All Data First
                    presenter.callAPIPopular(1, currentPage, adapterPopular);
                    break;
                case 2:
                    setClearData(adapterTopRated);
                    presenter.callAPITopRated(1, currentPage, adapterTopRated);
                    break;
                case 3:
                    setClearData(adapterUpcoming);
                    presenter.callAPIUpcoming(1, currentPage, adapterUpcoming);
                    break;
                default:
                    break;
            }
        } else {
            mSwipeRefresh.setVisibility(View.GONE);
            linearNoInternet.setVisibility(View.VISIBLE);
            btnRefresh.setOnClickListener(view -> callAPIMovies(listType));
        }
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
    public void showMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showListTopRated(TopRated topRated, TopRatedAdapter adapter, int position) {
        if (position == 1) {
            rvMovieList.setAdapter(adapter);
            TOTAL_PAGES = topRated.getTotalPages();

            loadMoreRecycler();
        } else if (position == 2) { //Recycler OnScrollListener
            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                adapter.removeLoadingFooter();
            } else {
                adapter.addLoadingFooter();
            }
        }

        showPageOf(getString(R.string.page_of, String.valueOf(currentPage),
                String.valueOf(TOTAL_PAGES)));
    }

    @Override
    public void showListPopular(Popular popular, PopularAdapter adapter, int position) {
        if (position == 1) {
            //NEW MODELS
            rvMovieList.setAdapter(adapter);
            TOTAL_PAGES = popular.getTotalPages(); //Get Total Pages
            loadMoreRecycler();

        } else if (position == 2) { //Recycler OnScrollListener
            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                adapter.removeLoadingFooter();
            } else {
                adapter.addLoadingFooter();
            }
        }

        showPageOf(getString(R.string.page_of, String.valueOf(currentPage),
                String.valueOf(TOTAL_PAGES)));
    }

    @Override
    public void showListUpcoming(Upcoming upcoming, UpcomingAdapter adapter, int position) {
        if (position == 1) {
            rvMovieList.setAdapter(adapter);
            TOTAL_PAGES = upcoming.getTotalPages();
            loadMoreRecycler();

        } else if (position == 2) { //Recycler OnScrollListener
            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                adapter.removeLoadingFooter();
            } else {
                adapter.addLoadingFooter();
            }
        }

        showPageOf(getString(R.string.page_of, String.valueOf(currentPage),
                String.valueOf(TOTAL_PAGES)));
    }

    private void loadMoreRecycler() {
        rvMovieList.addOnScrollListener(new EndlessScrollAnotherListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount, RecyclerView view) {
                currentPage = page;
                loadNextData(listType);

                Log.d("LOAD", "Page: " + page + "\n" + "CurrentPage: " + currentPage + "\n" + "Total: " + TOTAL_PAGES);
            }
        });
    }

    private RecyclerItemClickListener selectedItem(int position_movie) {
        return new RecyclerItemClickListener(this, rvMovieList, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                switch (position_movie) {
                    case 1: //For Popular Movie
                        if (!isNoConnection)
                            presenter.getItemPopular(adapterPopular.getItem(position),
                                    ListMovieActivity.this, DetailsActivity.class);
                        break;

                    case 2: //For Top Rated Movie
                        if (!isNoConnection)
                            presenter.getItemTopRated(adapterTopRated.getItem(position),
                                    ListMovieActivity.this, DetailsActivity.class);
                        break;

                    case 3: //For Upcoming Movie
                        if (!isNoConnection)
                            presenter.getItemUpcoming(adapterUpcoming.getItem(position),
                                    ListMovieActivity.this, DetailsActivity.class);
                        break;
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        });
    }

    private void loadNextData(int position) {
        if (!isLastPage) {
            new Handler().postDelayed(() -> {
                if (position == 1) {
                    presenter.callAPIPopular(2, currentPage, adapterPopular);
                } else if (position == 2) {
                    presenter.callAPITopRated(2, currentPage, adapterTopRated);
                } else if (position == 3) {
                    presenter.callAPIUpcoming(2, currentPage, adapterUpcoming);
                }
            }, 3000);
        } else {
            //Last PAGE OF List
            showMessage(getString(R.string.end_page));
        }
    }

    @Override
    public void moveToIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showPageOf(String string) {
        tvPageOf.setText(string);
    }

    @Override
    public void setTitleToolbar(String string) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(string);
        }
    }

    @Override
    public void setClearData(RecyclerView.Adapter adapter) {
        if (adapter == adapterPopular) {
            adapterPopular.getAllItem().clear();
        } else if (adapter == adapterTopRated) {
            adapterTopRated.getAllItem().clear();
        } else if (adapter == adapterUpcoming) {
            adapterUpcoming.getAllItem().clear();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void retryPageLoad() {
        loadNextData(listType);
    }

    @Override
    public void isNoConnection(boolean status) {
        isNoConnection = status;
    }
}
