package com.example.movieproject.network;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class NetworkResponse<ResponseType> implements Callback<ResponseType> {

    private WeakReference<NetworkResponseListener<ResponseType>> listener;

    NetworkResponse(NetworkResponseListener<ResponseType> listener) {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    public void onResponse(@NonNull Call<ResponseType> call, @NonNull Response<ResponseType> response) {
        if (listener != null && listener.get() != null) {
            listener.get().onResponseReceived(response.body());
        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseType> call, @NonNull Throwable t) {
        String codeError = "";

        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            codeError = String.valueOf(httpException.code());
            //Response response = httpException.response();
            codeError = codeError + ": ";
        }

        if (listener != null && listener.get() != null) {
            listener.get().onError(codeError + t.getMessage());
        }
    }
}
