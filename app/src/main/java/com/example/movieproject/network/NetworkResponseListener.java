package com.example.movieproject.network;

public interface NetworkResponseListener<Response> {

    void onResponseReceived(Response response);

    void onError(String errorMsg);
}
