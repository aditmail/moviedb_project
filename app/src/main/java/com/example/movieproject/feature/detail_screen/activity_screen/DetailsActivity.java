package com.example.movieproject.feature.detail_screen.activity_screen;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.movieproject.R;
import com.example.movieproject.base.mvp.MvpActivity;
import com.example.movieproject.feature.detail_screen.activity_screen.contract.DetailsPresenter;
import com.example.movieproject.feature.detail_screen.activity_screen.contract.DetailsView;
import com.example.movieproject.feature.detail_screen.fragment_screen.ReviewFragment;
import com.example.movieproject.feature.detail_screen.fragment_screen.VideosFragment;
import com.example.movieproject.models.popular.ResultsItemPopular;
import com.example.movieproject.models.top_rated.ResultsItemTopRated;
import com.example.movieproject.models.upcoming.ResultsItemUpcoming;
import com.example.movieproject.utils.PaginationAdapterCallback;
import com.example.movieproject.utils.TabViewPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class DetailsActivity extends MvpActivity<DetailsPresenter> implements DetailsView,
        View.OnClickListener, PaginationAdapterCallback {

    //Toolbar
    @BindView(R.id.toolbars)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedSccroll;
    @BindView(R.id.fabUpward)
    FloatingActionButton fabUpward;
    @BindView(R.id.imgPosterBackground)
    ImageView imgPosterBackground;
    @BindView(R.id.imgPoster)
    ImageView imgPoster;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvVoteAverage)
    TextView tvVoteAverage;
    @BindView(R.id.tvVoteCount)
    TextView tvVoteCount;

    @BindView(R.id.tvOverview)
    TextView tvOverview;
    @BindView(R.id.tvReleaseDate)
    TextView tvReleaseDate;
    @BindView(R.id.tvPopularity)
    TextView tvPopularity;

    //View Pager
    @BindView(R.id.tabs)
    TabLayout tabs_kantong;
    @BindView(R.id.viewpager_content)
    ViewPager viewpager_content;

    //Data Extras
    public static String extraModel = "MOVIE_MODEL";
    public static String extraMoviesData = "MOVIE_DATA";

    //Data ResultItem
    private ResultsItemPopular popular;
    private ResultsItemTopRated topRated;
    private ResultsItemUpcoming upcoming;

    private DecimalFormat decimalFormat;
    private SimpleDateFormat inputFormat, outputFormat;

    public static String movieID;
    private String title, overview, releaseDate, posterPath, posterBackgroundPath;
    private double voteAvg, voteCount, votePopularity;

    @Override
    protected DetailsPresenter createPresenter() {
        return new DetailsPresenter(this, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.decimalFormat = new DecimalFormat(",###.##");
        this.inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

        Intent getIntent = getIntent();
        if (getIntent.hasExtra(extraModel)) {
            int modelMovie = getIntent.getIntExtra(extraModel, 0);
            switch (modelMovie) {
                case 1: //For Popular
                    popular = (ResultsItemPopular)
                            getIntent.getSerializableExtra(extraMoviesData);
                    break;

                case 2: //For Top Rated
                    topRated = (ResultsItemTopRated)
                            getIntent.getSerializableExtra(extraMoviesData);
                    break;

                case 3: //For Upcmoing
                    upcoming = (ResultsItemUpcoming)
                            getIntent.getSerializableExtra(extraMoviesData);
                    break;

                default:
                    Toast.makeText(activity, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                    break;
            }

            generateDataMovie(modelMovie);
            initCollapsingToolbar();

            //Setting ViewPager
            setupViewPager(viewpager_content);
            tabs_kantong.setupWithViewPager(viewpager_content);

        } else {
            finish();
        }
    }

    private void initCollapsingToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout.setTitle("");
        appbar.setExpanded(true);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                    fabUpward.setVisibility(View.VISIBLE);
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                    fabUpward.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());
        presenter.setupViewPager(viewPager, adapter);
    }

    private void generateDataMovie(int model) {
        switch (model) {
            case 1: //Data Popular
                movieID = String.valueOf(popular.getId());
                Log.d("TAG", "ID: " + movieID);

                title = popular.getTitle();
                overview = popular.getOverview();
                releaseDate = popular.getReleaseDate();
                posterPath = popular.getPosterPath();
                posterBackgroundPath = popular.getBackdropPath();

                voteAvg = popular.getVoteAverage();
                voteCount = popular.getVoteCount();
                votePopularity = popular.getPopularity();

                initData();
                break;

            case 2: //Data Top Rated
                movieID = String.valueOf(topRated.getId());
                Log.d("TAG", "ID: " + movieID);

                title = topRated.getTitle();
                overview = topRated.getOverview();
                releaseDate = topRated.getReleaseDate();
                posterPath = topRated.getPosterPath();
                posterBackgroundPath = topRated.getBackdropPath();

                voteAvg = topRated.getVoteAverage();
                voteCount = topRated.getVoteCount();
                votePopularity = topRated.getPopularity();

                initData();
                break;

            case 3: //Data Upcoming
                movieID = String.valueOf(upcoming.getId());
                Log.d("TAG", "ID: " + movieID);

                title = upcoming.getTitle();
                overview = upcoming.getOverview();
                releaseDate = upcoming.getReleaseDate();
                posterPath = upcoming.getPosterPath();
                posterBackgroundPath = upcoming.getBackdropPath();

                voteAvg = upcoming.getVoteAverage();
                voteCount = upcoming.getVoteCount();
                votePopularity = upcoming.getPopularity();

                initData();
                break;

            default:
                break;
        }
    }

    private void initData() {
        fabUpward.setOnClickListener(this);

        tvTitle.setText(title);
        tvOverview.setText(overview);

        tvVoteAverage.setText((decimalFormat.format(voteAvg)));
        tvVoteCount.setText((decimalFormat.format(voteCount)));
        tvPopularity.setText((decimalFormat.format(votePopularity)));

        if (releaseDate != null && !releaseDate.equalsIgnoreCase("null")) {
            try {
                Date date = inputFormat.parse(releaseDate);
                String dateFixed = outputFormat.format(date);
                tvReleaseDate.setText(dateFixed);
            } catch (ParseException ignored) {
            }
        }

        Glide.with(this)
                .load(getString(R.string.imagePath) + posterPath)
                .apply(new RequestOptions().centerCrop()
                        .circleCrop()
                        .placeholder(R.mipmap.ic_launcher_round))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imgPoster.setImageResource(R.drawable.ic_broken_image_xml);
                        imgPoster.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imgPoster.setPadding(10, 10, 10, 10);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imgPoster);

        Glide.with(this)
                .load(getString(R.string.imagePath) + posterBackgroundPath)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imgPosterBackground.setImageResource(R.drawable.ic_broken_image_xml);
                        imgPosterBackground.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        imgPosterBackground.setPadding(10, 10, 10, 10);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imgPosterBackground);

        //Refresh Fragment
        mSwipeRefresh.setOnRefreshListener(() -> {
            setupViewPager(viewpager_content);
            tabs_kantong.setupWithViewPager(viewpager_content);

            mSwipeRefresh.setRefreshing(false);
        });

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabUpward) {
            nestedSccroll.fullScroll(ScrollView.FOCUS_UP);
            appbar.setExpanded(true);
        }
    }

    @Override
    public void retryPageLoad() {

    }

    @Override
    public void isNoConnection(boolean status) {

    }
}
