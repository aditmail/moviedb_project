package com.example.movieproject.classes.abstract_interface;

import java.util.List;

public interface IListMovies {

    interface LoadMoreList {
        void addLoadingFooter();

        void removeLoadingFooter();
    }

    interface AddList<V> {
        void addAllData(List<V> results);

        void addData(V items);
    }

    interface GetItemList<V> {
        V getItem(int position);

        List<V> getAllItem();
    }

    interface RemoveList<V> {
        void removeData(V items);

        void removeDataAt(int position);
    }
}
