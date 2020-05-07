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

public class ListMovieActivity extends MvpActivity<ListMoviePresenter> implements ListMovieView, PaginationAdapterCallback {

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
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.popular);
                    }

                    adapterPopular = new PopularAdapter(this, 2);
                    mSwipeRefresh.setOnRefreshListener(() -> {
                        currentPage = 1;
                        callAPIMovies(listType);
                        mSwipeRefresh.setRefreshing(false);
                    });
                    break;

                case 2:
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.top_rated_title);
                    }

                    adapterTopRated = new TopRatedAdapter(this, 2);
                    mSwipeRefresh.setOnRefreshListener(() -> {
                        currentPage = 1;
                        callAPIMovies(listType);
                        mSwipeRefresh.setRefreshing(false);
                    });
                    break;

                case 3:
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.upcoming_movie_title);
                    }

                    adapterUpcoming = new UpcomingAdapter(this, 2);
                    mSwipeRefresh.setOnRefreshListener(() -> {
                        currentPage = 1;
                        callAPIMovies(listType);
                        mSwipeRefresh.setRefreshing(false);
                    });
                    break;

                default:
                    break;
            }

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
                    //Clearing All Data First
                    adapterPopular.getAllItem().clear();
                    adapterPopular.notifyDataSetChanged();

                    presenter.callAPIPopular(1, currentPage, adapterPopular);
                    break;
                case 2:
                    adapterTopRated.getAllItem().clear();
                    adapterTopRated.notifyDataSetChanged();

                    presenter.callAPITopRated(1, currentPage, adapterTopRated);
                    break;
                case 3:
                    adapterUpcoming.getAllItem().clear();
                    adapterUpcoming.notifyDataSetChanged();

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

            //NEW MODEL
            rvMovieList.setAdapter(adapter);
            TOTAL_PAGES = topRated.getTotalPages();

            rvMovieList.addOnItemTouchListener(selectedItem(2));
            loadMoreRecycler();

            /*
            adapterTopRated.addAll(items);
            rvMovieList.setLayoutManager(staggeredGridLayoutManager);
            rvMovieList.hasFixedSize();

            //rvMovieList.setLayoutManager(gridLayoutManager);
            rvMovieList.setItemAnimator(new DefaultItemAnimator());
            rvMovieList.setPadding(10, 0, 5, 15);

            //Parse the Data to show in RecyclerView
            rvMovieList.setAdapter(adapterTopRated);*/

            //Recycler OnScrollListener
        } else if (position == 2) {
            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                adapter.removeLoadingFooter();
            } else {
                adapter.addLoadingFooter();
            }

            /*adapterTopRated.addAll(items);
            adapterTopRated.notifyItemRangeInserted(adapterTopRated.getItemCount(), items.size() - 1);

            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                adapterTopRated.removeLoadingFooter();
            } else {
                //Add Data to Adapter List
                adapterTopRated.addLoadingFooter();
            }*/
        }

        tvPageOf.setText(getString(R.string.page_of, String.valueOf(currentPage),
                String.valueOf(TOTAL_PAGES)));
    }

    @Override
    public void showListPopular(Popular popular, PopularAdapter adapter, int position) {
        if (position == 1) {
            //NEW MODELS
            rvMovieList.setAdapter(adapter);
            TOTAL_PAGES = popular.getTotalPages(); //Get Total Pages

            rvMovieList.addOnItemTouchListener(selectedItem(1));
            loadMoreRecycler(); //Recycler OnScrollListener

            //Add Data to Popular Adapter
            /*adapterPopular.addAll(items);

            //Setting the RecyclerView
            rvMovieList.setLayoutManager(staggeredGridLayoutManager);
            //rvMovieList.setLayoutManager(gridLayoutManager);

            rvMovieList.hasFixedSize();
            rvMovieList.setItemAnimator(new DefaultItemAnimator());
            rvMovieList.setPadding(10, 0, 5, 15);

            //Parse the Data to show in RecyclerView
            rvMovieList.setAdapter(adapterPopular);*/

        } else if (position == 2) {

            //NEW MODEL
            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                adapter.removeLoadingFooter();
            } else {
                adapter.addLoadingFooter();
            }

            //OLD MODEL
            /*if (items != null) {
                adapterPopular.addAll(items);
                adapterPopular.notifyItemRangeInserted(adapterPopular.getItemCount(), items.size() - 1);

                if (currentPage == TOTAL_PAGES) {
                    isLastPage = true;
                    adapterPopular.removeLoadingFooter();
                } else {
                    //Add Data to Adapter List
                    adapterPopular.addLoadingFooter();
                }
            } else {
                adapterPopular.showRetry(true, "");
            }*/
        }

        tvPageOf.setText(getString(R.string.page_of, String.valueOf(currentPage),
                String.valueOf(TOTAL_PAGES)));
    }

    @Override
    public void showListUpcoming(Upcoming upcoming, UpcomingAdapter adapter, int position) {
        if (position == 1) {
            //NEW MODEL
            rvMovieList.setAdapter(adapter);
            TOTAL_PAGES = upcoming.getTotalPages();

            rvMovieList.addOnItemTouchListener(selectedItem(3));
            loadMoreRecycler();

            //Add Data to Popular Adapter
            /*adapterUpcoming.addAll(items);

            //Setting the RecyclerView
            rvMovieList.setLayoutManager(staggeredGridLayoutManager);
            //rvMovieList.setLayoutManager(gridLayoutManager);

            rvMovieList.hasFixedSize();
            rvMovieList.setItemAnimator(new DefaultItemAnimator());
            rvMovieList.setPadding(10, 0, 5, 15);

            //Parse the Data to show in RecyclerView
            rvMovieList.setAdapter(adapterUpcoming);*/

            //Recycler OnScrollListener
        } else if (position == 2) {
            //NEW MODEL
            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                adapter.removeLoadingFooter();
            } else {
                adapter.addLoadingFooter();
            }

            //OLD MODEL
            /*adapterUpcoming.addAll(items);
            if (currentPage == TOTAL_PAGES) {
                isLastPage = true;
                adapterUpcoming.removeLoadingFooter();
            } else {
                //Add Data to Adapter List
                adapterUpcoming.addLoadingFooter();
            }*/
        }

        tvPageOf.setText(getString(R.string.page_of, String.valueOf(currentPage),
                String.valueOf(TOTAL_PAGES)));
    }

    private void loadMoreRecycler() {
        /*scrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemCount, RecyclerView view) {
                currentPage = page;
                Log.d("LOAD", "Page: " + page + "\n" + "CurrentPage: " + currentPage + "\n" + "Total: " + TOTAL_PAGES);

                //if (!isLastPage) {
                //if (isBottomPage) {

                //if (!view.canScrollVertically(1)) {

                //adapterPopular.addLoadingFooter();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position == 1) {
                            adapterPopular.removeLoadingFooter();
                            presenter.callAPIPopular(2, currentPage);
                        } else if (position == 2) {
                            adapterTopRated.removeLoadingFooter();
                            presenter.callAPITopRated(2, currentPage);
                        } else if (position == 3) {
                            adapterUpcoming.removeLoadingFooter();
                            presenter.callAPIUpcoming(2, currentPage);
                        }
                    }
                }, 3000);
                // }
                //}
            }
            rvMovieList.addOnScrollListener(scrollListener);
            */

        rvMovieList.addOnScrollListener(new EndlessScrollAnotherListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount, RecyclerView view) {
                currentPage = page;
                Log.d("LOAD", "Page: " + page + "\n" + "CurrentPage: " + currentPage + "\n" + "Total: " + TOTAL_PAGES);
                loadNextData(listType);
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (position == 1) {
                        presenter.callAPIPopular(2, currentPage, adapterPopular);
                    } else if (position == 2) {
                        presenter.callAPITopRated(2, currentPage, adapterTopRated);
                    } else if (position == 3) {
                        presenter.callAPIUpcoming(2, currentPage, adapterUpcoming);
                    }
                }
            }, 3000);
        } else {
            //Last PAGE OF List
        }
    }

    @Override
    public void moveToIntent(Intent intent) {
        startActivity(intent);
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
