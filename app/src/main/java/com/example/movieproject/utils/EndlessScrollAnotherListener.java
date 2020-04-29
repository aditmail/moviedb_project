package com.example.movieproject.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class EndlessScrollAnotherListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 5; //Minimum amount items before loading more
    private int currentPage = 1; //current offset index data loaded
    private int previousTotalItemCount = 0; //total number items in dataset
    private int startingPageIndex = 1;//Set starting page index

    private boolean loading = true; //True -> waiting last set data to be load

    private RecyclerView.LayoutManager layoutManager;

    public EndlessScrollAnotherListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EndlessScrollAnotherListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();

        //Log.d("TAG_GRID", ("Span Count: " + layoutManager.getSpanCount()));
    }

    public EndlessScrollAnotherListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();

        //Log.d("TAG_STAGGERED", ("Span Count: " + layoutManager.getSpanCount() + "\n" + "Total: " + visibleThreshold));
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];

                //Log.d("TAG", "if 0 Ke- " + i + ":" + maxSize);
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];

                //Log.d("TAG", "if not 0 Ke- " + i + ":" + maxSize);
            }
        }

        //Log.d("TAG_MAX_SIZED", ("Max Size: " + maxSize));

        return maxSize;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //If Scroll Up.. Return Scrolled
        if (dy < 1) {
            return;
        }

        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();

        //Log.d("TAG", "LayoutManager Item Count: " + totalItemCount);

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager)
                    layoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions); //Get Max element within the list

            //Log.d("TAG_STAGGERED", "Last Visible: " + lastVisibleItemPosition);
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager)
                    layoutManager).findLastVisibleItemPosition();

        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager)
                    layoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        /*if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;

            if (totalItemCount == 0) {
                this.loading = true;
            }
        }*/

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            //Log.d("TAG", "itemCount: " + totalItemCount + "\n" + "prevTotalItem:" + previousTotalItemCount);

            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            loading = true;

            //Log.d("TAG2", "LastVisible+Treshold: " + (lastVisibleItemPosition + visibleThreshold)
            // + "\n" + "itemCount: " + totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // supply a positive number to recyclerView.canScrollVertically(int direction)
            // to check if scrolling down.
            boolean canScrollDownMore = recyclerView.canScrollVertically(1);

            // If recyclerView.canScrollVertically(1) returns false
            // it means you're at the end of the list.
            if (!canScrollDownMore) {
                onScrolled(recyclerView, 0, 1);
            } else {
                onScrolled(recyclerView, 0, 0);
            }
        }
    }

    //For Performing New Searches
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    //Process for actual loading more based on page
    public abstract void onLoadMore(int page, int totalItemCount, RecyclerView view);
}
