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
import com.example.movieproject.models.top_rated.ResultsItemTopRated;
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

public class TopRatedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int position;

    //Loading
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<ResultsItemTopRated> topRatedList;
    private boolean isLoadingAdded = false;
    private boolean isRetryPageLoaded = false;

    private PaginationAdapterCallback mCallback;

    private DecimalFormat decimalFormat;
    private SimpleDateFormat inputFormat, outputFormat;

    public TopRatedAdapter(Context context, int position) {
        this.context = context;
        this.position = position;
        this.mCallback = (PaginationAdapterCallback) context;

        this.topRatedList = new ArrayList<>();

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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
                viewHolder = new TopRatedAdapter.ListViewHolder(view);
            } else if (position == 2) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list_full, parent, false);
                viewHolder = new TopRatedAdapter.ListViewHolder(view);
            }
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more, parent, false);
            viewHolder = new TopRatedAdapter.LoadingViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopRatedAdapter.ListViewHolder) {
            populateItemHolder((TopRatedAdapter.ListViewHolder) holder, position);
        } else if (holder instanceof TopRatedAdapter.LoadingViewHolder) {
            populateLoadingHolder((TopRatedAdapter.LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return topRatedList == null ? 0 : topRatedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == topRatedList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
        //Getting the ID for List Recycler..
    }

    private void populateItemHolder(TopRatedAdapter.ListViewHolder holder, int position) {
        ResultsItemTopRated topRated = topRatedList.get(position);

        String title = topRated.getTitle();
        String dateRelease = topRated.getReleaseDate();
        String posterPath = topRated.getPosterPath();

        double averageVote = topRated.getVoteAverage();
        int countVote = topRated.getVoteCount();

        if (title != null && !title.equalsIgnoreCase("null")) {
            holder.tvTitle.setText(title);
        } else {
            holder.itemView.setVisibility(View.GONE);
            //removeAt(position);
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

    private void populateLoadingHolder(TopRatedAdapter.LoadingViewHolder viewHolder, int position) {
        //Data ProgressBar
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
        notifyItemChanged(topRatedList.size() - 1);
    }

    //Adapter Helper -------------------------
    //Adding All Data from API to List..
    //For Many result are.. Added one by one
    public void addAll(List<ResultsItemTopRated> resultsItemTopRateds) {
        for (ResultsItemTopRated results : resultsItemTopRateds) {
            add(results);
        }
    }

    //Insert Data One By One to the List
    //Also Notified to adapter if data are inserted
    public void add(ResultsItemTopRated item) {
        if (item != null) {
            topRatedList.add(item);
            notifyItemInserted(topRatedList.size() - 1);
        }
    }

    //Remove Selected Data
    public void remove(ResultsItemTopRated resultsItemTopRated) {
        int position = topRatedList.indexOf(resultsItemTopRated);
        if (position > -1) {
            topRatedList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAt(int position) {
        topRatedList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, topRatedList.size());
    }

    //Get or Add List Data At Position...?
    public ResultsItemTopRated getItem(int position) {
        return topRatedList.get(position);
    }

    public List<ResultsItemTopRated> getAllItem() {
        return topRatedList;
    }

    //Showing the Loading
    //And adding the data
    public void addLoadingFooter() {
        isLoadingAdded = true; //Showing the Loading Screen
        add(new ResultsItemTopRated());
    }

    //Hide the Loading Screen
    public void removeLoadingFooter() {
        isLoadingAdded = false; //Hide the Loading Screen

        int position = topRatedList.size() - 1;
        ResultsItemTopRated item = getItem(position);

        if(item != null){
            topRatedList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
