package com.ap.zapper.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class CacheMonitorInterceptor implements Interceptor {
    private final String cacheControlValue;

    private static final String TAG = CacheMonitorInterceptor.class.getSimpleName();

    public CacheMonitorInterceptor(CacheControl cacheControl) {
        cacheControlValue = cacheControl.toString();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Log.d(TAG, "response: " + response);
        Log.d(TAG, "response cache: " + response.cacheResponse());
        Log.d(TAG, "response network: " + response.networkResponse());

        return response.newBuilder()
                .header("Cache-Control", cacheControlValue)
                .build();

    }
}
