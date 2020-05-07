package com.example.movieproject.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieproject.R;
import com.example.movieproject.models.movies_review.ResultsItemReview;
import com.example.movieproject.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int position;

    //Loading
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<ResultsItemReview> reviewList;
    private boolean isLoadingAdded = false;
    private boolean isRetryPageLoaded = false;
    private boolean isShowComment = true;

    private PaginationAdapterCallback mCallback;

    public ReviewsAdapter(Context context) {
        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        this.reviewList = new ArrayList<>();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAuthor)
        TextView tvAuthor;
        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindView(R.id.tvLinkedURL)
        TextView tvLinkedURL;

        public ListViewHolder(@NonNull View itemView) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_list, parent, false);
            viewHolder = new ReviewsAdapter.ListViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_more, parent, false);
            viewHolder = new ReviewsAdapter.LoadingViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReviewsAdapter.ListViewHolder) {
            populateItemHolder((ReviewsAdapter.ListViewHolder) holder, position);
        } else if (holder instanceof ReviewsAdapter.LoadingViewHolder) {
            populateLoadingHolder((ReviewsAdapter.LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == reviewList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void populateItemHolder(ReviewsAdapter.ListViewHolder holder, int position) {
        ResultsItemReview review = reviewList.get(position);

        String author = review.getAuthor();
        String comment = review.getContent();
        String linked = review.getUrl();

        if (author != null && !author.equalsIgnoreCase("null")) {
            holder.tvAuthor.setText(author);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }

        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowComment) {
                    isShowComment = false;
                    holder.tvComment.setMaxLines(Integer.MAX_VALUE);
                } else {
                    isShowComment = true;
                    holder.tvComment.setMaxLines(3);
                }
            }
        });

        holder.tvComment.setText(comment);
        holder.tvLinkedURL.setText(linked);
        Linkify.addLinks(holder.tvLinkedURL, Linkify.ALL);
    }

    private void populateLoadingHolder(ReviewsAdapter.LoadingViewHolder viewHolder, int position) {
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
        notifyItemChanged(reviewList.size() - 1);
    }

    public void addAll(List<ResultsItemReview> resultsItemReviews) {
        for (ResultsItemReview results : resultsItemReviews) {
            add(results);
        }
    }

    private void add(ResultsItemReview results) {
        if (results != null) {
            reviewList.add(results);
            notifyItemInserted(reviewList.size() - 1);
        }
    }

    public void remove(ResultsItemReview resultsItemReview) {
        int position = reviewList.indexOf(resultsItemReview);
        if (position > -1) {
            reviewList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ResultsItemReview getItem(int position) {
        return reviewList.get(position);
    }

    public List<ResultsItemReview> getAllItem() {
        return reviewList;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ResultsItemReview());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = reviewList.size() - 1;
        ResultsItemReview itemReview = getItem(position);

        if (itemReview != null) {
            reviewList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
