package com.ap.zapper.controllers;

import android.content.Context;

import com.ap.zapper.interceptors.CacheMonitorInterceptor;
import com.ap.zapper.utililites.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class HttpClientController {
    private OkHttpClient mClient;

    private CacheControl mCacheControl;

    private static HttpClientController ourInstance = null;

    public static HttpClientController getInstance(Context context) {

        if(ourInstance == null) {
            ourInstance = new HttpClientController(context);
        }

        return ourInstance;
    }

    private HttpClientController (Context context) {

        mCacheControl = new CacheControl.Builder()
                .maxStale(22, TimeUnit.DAYS)
                .maxAge(22, TimeUnit.DAYS)
                .build();

        mClient = new OkHttpClient.Builder()
                .addInterceptor(new CacheMonitorInterceptor(mCacheControl))
                .connectTimeout(Constants.DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .followSslRedirects(false)
                .build();
    }

    public OkHttpClient client() {
        return mClient;
    }

    public CacheControl getCacheControl() {
        return mCacheControl;
    }
}
