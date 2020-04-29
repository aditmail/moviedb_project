package com.example.movieproject.network;

import android.content.Context;

import com.example.movieproject.utils.NetworkUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    private static Retrofit retrofit = null;
    private final static long CACHE_SIZE = 10 * 1024 * 1024; //10MB Cache Size

    private static OkHttpClient buildClient(Context context) {
        //Build Interceptor..
        final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
            Response originalResponse = chain.proceed(chain.request());
            if (NetworkUtil.hasNetwork(context)) {
                int maxAge = 60; //Read from Cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; //Tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        };

        //Create Cache
        Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);

        return new OkHttpClient
                .Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(cache)
                .build();
    }

    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            /*Interceptor headerAuth = chain -> {
                Request request = chain.request();
                Headers headers = request.headers().newBuilder()
                        //.add(String.valueOf(R.string.apiAuth), String.valueOf(R.string.apiKey))
                        .add("Authorization", TOKEN)
                        .build();
                request = request.newBuilder().headers(headers).build();
                return chain.proceed(request);
            };*/

            //okHttpClientBuilder.addInterceptor(headerAuth);
            //OkHttpClient okHttpClient = okHttpClientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(buildClient(context))
                    .build();
        }

        return retrofit;
    }
}
