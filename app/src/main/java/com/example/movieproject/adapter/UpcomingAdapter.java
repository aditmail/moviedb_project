package com.example.movieproject.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.movieproject.R;
import com.example.movieproject.models.upcoming.ResultsItemUpcoming;
import com.example.movieproject.utils.PaginationAdapterCallback;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int position;

    //Loading
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<ResultsItemUpcoming> upcomingList;
    private boolean isLoadingAdded = false;
    private boolean isRetryPageLoaded = false;

    private PaginationAdapterCallback mCallback;

    private DecimalFormat decimalFormat;
    private SimpleDateFormat inputFormat, outputFormat;

    public UpcomingAdapter(Context context, int position) {
        this.context = context;
        this.position = position;
        this.mCallback = (PaginationAdapterCallback) context;
        this.upcomingList = new ArrayList<>();

        this.decimalFormat = new DecimalFormat(",###.##");
        this.inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.viewPoster)
        LinearLayout linearData;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvReleaseDate)
        TextView tvdateRelease;
        @BindView(R.id.tvVoteAverage)
        TextView tvAverageVote;
        @BindView(R.id.tvVoteCount)
        TextView tvCountVote;
        @BindView(R.id.imgPoster)
        ImageView imgPoster;
        @BindView(R.id.progressBar_Image)
        ProgressBar progressBar;

        private ListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.linearRetry)
        LinearLayout linearRetry;
        @BindView(R.id.btnReload)
        Button btnRetry;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_ITEM) {
            if (position == 1) {
                //Standard Model
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
                viewHolder = new UpcomingAdapter.ListViewHolder(view);
            } else if (position == 2) {
                //CardView Model
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list_full, parent, false);
                viewHolder = new UpcomingAdapter.ListViewHolder(view);
            }
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more, parent, false);
            viewHolder = new UpcomingAdapter.LoadingViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UpcomingAdapter.ListViewHolder) {
            populateItemHolder((UpcomingAdapter.ListViewHolder) holder, position);
        } else if (holder instanceof UpcomingAdapter.LoadingViewHolder) {
            populateLoadingHolder((UpcomingAdapter.LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return upcomingList == null ? 0 : upcomingList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == upcomingList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
        //Getting the ID for List Recycler..
    }

    private void populateItemHolder(UpcomingAdapter.ListViewHolder holder, int position) {
        ResultsItemUpcoming upcoming = upcomingList.get(position);

        String title = upcoming.getTitle();
        String dateRelease = upcoming.getReleaseDate();
        String posterPath = upcoming.getPosterPath();

        double averageVote = upcoming.getVoteAverage();
        int countVote = upcoming.getVoteCount();

        if (title != null && !title.equalsIgnoreCase("null")) {
            holder.tvTitle.setText(title);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }

        if (dateRelease != null && !dateRelease.equalsIgnoreCase("null")) {
            try {
                Date date = inputFormat.parse(dateRelease);
                String dateFixed = outputFormat.format(date);
                holder.tvdateRelease.setText(dateFixed);
            } catch (ParseException ignored) {
            }
        }

        if (posterPath != null && !posterPath.equalsIgnoreCase("null")) {
            String url = context.getString(R.string.imagePath) + posterPath;
            Glide.with(holder.itemView.getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);

                            holder.imgPoster.setImageResource(R.drawable.ic_broken_image_xml);
                            holder.imgPoster.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            holder.imgPoster.setPadding(10, 10, 10, 10);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.imgPoster);
        } else {
            holder.progressBar.setVisibility(View.GONE);

            holder.imgPoster.setImageResource(R.drawable.ic_broken_image_xml);
            holder.imgPoster.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.imgPoster.setPadding(10, 10, 10, 10);
        }

        holder.tvAverageVote.setText(String.valueOf(averageVote));
        holder.tvCountVote.setText((decimalFormat.format(countVote)));
    }

    private void populateLoadingHolder(UpcomingAdapter.LoadingViewHolder viewHolder, int position) {
        if (isRetryPageLoaded) {
            mCallback.isNoConnection(true);

            viewHolder.linearRetry.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setVisibility(View.GONE);

            viewHolder.btnRetry.setOnClickListener(view -> {
                showRetry(false, null);
                mCallback.retryPageLoad();
            });
        } else {
            viewHolder.linearRetry.setVisibility(View.GONE);
            viewHolder.progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void showRetry(boolean show, String errorMsg) {
        isRetryPageLoaded = show;
        notifyItemChanged(upcomingList.size() - 1);
    }

    //Adapter Helper -------------------------------------
    //Adding All Data from API to List..
    //For Many result are.. Added one by one
    public void addAll(List<ResultsItemUpcoming> resultsItemUpcomings) {
        for (ResultsItemUpcoming results : resultsItemUpcomings) {
            add(results);
        }
    }

    //Insert Data One By One to the List
    //Also Notified to adapter if data are inserted
    public void add(ResultsItemUpcoming item) {
        if (item != null) {
            upcomingList.add(item);
            notifyItemInserted(upcomingList.size() - 1);
        }
    }

    //Remove Selected Data
    public void remove(ResultsItemUpcoming resultsItemUpcoming) {
        int position = upcomingList.indexOf(resultsItemUpcoming);
        if (position > -1) {
            upcomingList.remove(position);
            notifyItemRemoved(position);
        }
    }

    //Get or Add List Data At Position...?
    public ResultsItemUpcoming getItem(int position) {
        return upcomingList.get(position);
    }

    public List<ResultsItemUpcoming> getAllItem() {
        return upcomingList;
    }

    //Showing the Loading
    //And adding the data
    public void addLoadingFooter() {
        isLoadingAdded = true; //Showing the Loading Screen
        add(new ResultsItemUpcoming());
    }

    //Hide the Loading Screen
    public void removeLoadingFooter() {
        isLoadingAdded = false; //Hide the Loading Screen

        int position = upcomingList.size() - 1;
        ResultsItemUpcoming item = getItem(position);

        if (item != null) {
            upcomingList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
