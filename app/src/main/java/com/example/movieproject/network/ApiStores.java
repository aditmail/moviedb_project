package com.example.movieproject.network;

import com.example.movieproject.models.movies_review.MoviesReview;
import com.example.movieproject.models.movies_video.MovieVideos;
import com.example.movieproject.models.popular.Popular;
import com.example.movieproject.models.top_rated.TopRated;
import com.example.movieproject.models.upcoming.Upcoming;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiStores {

    @GET("movie/top_rated")
    Call<TopRated> getTopRated(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/popular")
    Call<Popular> getPopular(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/upcoming")
    Call<Upcoming> getUpcoming(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/{movie_id}/videos")
    Call<MovieVideos> getVideoList(@Path("movie_id") String movieID, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<MoviesReview> getReviewList(@Path("movie_id") String movieID, @Query("api_key") String apiKey, @Query("page") int pageIndex);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("movie/top_rated")
    Call<ResponseBody> getTopsRated(@Query("api_key") String apiKey); //Success
    //Call<ResponseBody> getTopsRated(@Header("Authorization") String auth);
    //Call<ResponseBody> getTopsRated();
}
