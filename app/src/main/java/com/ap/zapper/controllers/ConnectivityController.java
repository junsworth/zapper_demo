package com.ap.zapper.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ap.zapper.MainActivity;

/**
 * Created by jonathanunsworth on 2017/01/24.
 */

public class ConnectivityController {
    private static ConnectivityController ourInstance = null;

    public static ConnectivityController getInstance() {

        if(ourInstance == null) {
            ourInstance = new ConnectivityController();
        }

        return ourInstance;
    }

    public boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager cm =
                (ConnectivityManager)mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
