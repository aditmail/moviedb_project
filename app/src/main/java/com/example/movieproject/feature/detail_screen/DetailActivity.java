package com.example.movieproject.feature.detail_screen;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.movieproject.R;
import com.example.movieproject.adapter.MovieVideosAdapter;
import com.example.movieproject.base.mvp.MvpActivity;
import com.example.movieproject.feature.detail_screen.contract.DetailPresenter;
import com.example.movieproject.feature.detail_screen.contract.DetailView;
import com.example.movieproject.models.movies_video.MovieVideos;
import com.example.movieproject.models.popular.ResultsItemPopular;
import com.example.movieproject.models.top_rated.ResultsItemTopRated;
import com.example.movieproject.models.upcoming.ResultsItemUpcoming;
import com.example.movieproject.utils.NetworkUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class DetailActivity extends MvpActivity<DetailPresenter> implements DetailView, View.OnClickListener {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.imgPosterBackground)
    ImageView imgPosterBackground;
    @BindView(R.id.imgPoster)
    ImageView imgPoster;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
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

    //Videos
    @BindView(R.id.tvErrorVideo)
    TextView tvErrorVideo;
    @BindView(R.id.tvNoVideo)
    TextView tvNoVideo;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rvMoviesList)
    RecyclerView rvMoviesList;

    //Data Extras
    public static String extraModel = "MOVIE_MODEL";
    public static String extraMoviesData = "MOVIE_DATA";

    //Data ResultItem
    private ResultsItemPopular popular;
    private ResultsItemTopRated topRated;
    private ResultsItemUpcoming upcoming;

    private DecimalFormat decimalFormat;
    private SimpleDateFormat inputFormat, outputFormat;

    private String movieID;
    private String title, overview, releaseDate, posterPath, posterBackgroundPath;
    private double voteAvg, voteCount, votePopularity;

    @Override
    protected DetailPresenter createPresenter() {
        return new DetailPresenter(this, getApplicationContext());
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

        } else {
            finish();
        }
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
        btnBack.setOnClickListener(this);

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

        mSwipeRefresh.setOnRefreshListener(() -> {
            callAPIVideo();
            mSwipeRefresh.setRefreshing(false);
        });
    }

    private void callAPIVideo() {
        if (NetworkUtil.hasNetwork(this)) {
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
            rvMoviesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvMoviesList.setItemAnimator(new DefaultItemAnimator());
            rvMoviesList.setAdapter(new MovieVideosAdapter(movieVideos, this));
        } else {
            tvNoVideo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnBack) {
            finish();
        }
    }
}
