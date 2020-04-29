package com.example.movieproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtil {


    public static boolean hasNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) isConnected = true;
        return isConnected;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network capabilities = null;

            if (conMgr != null) {
                capabilities = conMgr.getActiveNetwork();
            }

            NetworkCapabilities capabilities1 = conMgr.getNetworkCapabilities(capabilities);
            if (capabilities1 != null) {
                if (capabilities1.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                } else if (capabilities1.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            NetworkInfo info = conMgr.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            } else {
                return false;
            }
        }
    }
}
