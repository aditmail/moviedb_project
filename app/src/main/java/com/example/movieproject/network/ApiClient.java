package com.example.movieproject.network;

import android.content.Context;
import android.util.Log;

import com.example.movieproject.R;
import com.example.movieproject.models.movies_video.MovieVideos;
import com.example.movieproject.models.popular.Popular;
import com.example.movieproject.models.top_rated.TopRated;
import com.example.movieproject.models.upcoming.Upcoming;

import okhttp3.ResponseBody;

public class ApiClient {

    private Context context;
    private String apiKey;

    public ApiClient(Context context) {
        this.context = context;
        this.apiKey = context.getString(R.string.apiKeyV3);
        Log.d("TAG", apiKey);
    }

    //Client for Top Rated
    public void getTopRated(int page, ApiStores apiStores, NetworkResponseListener<TopRated> listener) {
        apiStores.getTopRated(apiKey, page).enqueue(new NetworkResponse<>(listener));
    }

    //Client for Popular
    public void getPopular(int page, ApiStores apiStores, NetworkResponseListener<Popular> listener) {
        apiStores.getPopular(apiKey, page).enqueue(new NetworkResponse<>(listener));
    }

    //Client for Popular
    public void getUpcoming(int page, ApiStores apiStores, NetworkResponseListener<Upcoming> listener) {
        apiStores.getUpcoming(apiKey, page).enqueue(new NetworkResponse<>(listener));
    }

    //Client for Movie Videos
    public void getMovieVideoList(String movieID, ApiStores apiStores, NetworkResponseListener<MovieVideos> listener) {
        apiStores.getVideoList(movieID, apiKey).enqueue(new NetworkResponse<>(listener));
    }

    public void getTopsRated(ApiStores apiStores, NetworkResponseListener<ResponseBody> listener) {
        String TOKEN = "Bearer " +
                "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NzIxMjk2MWUzZWUxMjllYjc2Y2QwYTc0NTY5YzUyMSIsInN1YiI6IjVlOWZkNjBiMDE3NTdmMDAxZWQxMDJkNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hbgeczGhqoXkHiany-XUcj6pHf7B50DPajN0dzZVamk";
        //apiStores.getTopsRated().enqueue(new NetworkResponse<>(listener));
        //apiStores.getTopsRated(String.valueOf(R.string.apiKey)).enqueue(new NetworkResponse<ResponseBody>(listener)); //Gagal
        apiStores.getTopsRated(TOKEN).enqueue(new NetworkResponse<ResponseBody>(listener));
        //apiStores.getTopsRated("57212961e3ee129eb76cd0a74569c521").enqueue(new NetworkResponse<ResponseBody>(listener)); //Success
    }
}
