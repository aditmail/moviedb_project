package com.example.movieproject.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

//*Another Model.. Still Finds some Bugs
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    //Minimum amount items before loading more
    private int visibleThreshold = 5;

    //current offset index data loaded
    private int currentPage = 1;

    //total number items in dataset
    private int previousTotalItemCount = 0;

    //True -> waiting last set data to be load
    private boolean loading = true;

    //Set starting page index
    private int startingPageIndex = 1;

    private int visibleItemCount;

    private RecyclerView.LayoutManager layoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        //Log.d("TAG_GRID", ("Span Count: " + layoutManager.getSpanCount()));
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        //Log.d("TAG_STAGGERED", ("Span Count: " + layoutManager.getSpanCount() + "\n" + "Multiply: " + visibleThreshold));
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        //Log.d("TAG", "length: " + lastVisibleItemPositions.length);

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

        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();
        visibleItemCount = layoutManager.getChildCount();

        //Log.d("TAG", "LayoutManager Item Count: " + totalItemCount);

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager)
                    layoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions); //Get Max element within the list
            //Log.d("TAG_STAGGERED", "Last Visible: " + lastVisibleItemPosition);
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();

        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;

            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        /*if (loading) {
            if ((visibleItemCount + previousTotalItemCount) >= totalItemCount) {
                loading = false;
                previousTotalItemCount = totalItemCount;
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
        if (!loading && (totalItemCount - visibleThreshold) <= (previousTotalItemCount + visibleItemCount)) { // ERROR HERE..
            //if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount
            //        && recyclerView.getAdapter().getItemCount() > visibleThreshold) { // ERROR HERES..

            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            loading = true;

            //Log.d("TAG2", "LastVisible+Treshold: " + (lastVisibleItemPosition + visibleThreshold) + "\n" +
            // "itemCount: " + totalItemCount);
        }

        /*if (!loading && (totalItemCount <= lastVisibleItemPosition + visibleThreshold)) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            loading = true;
        }*/
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
