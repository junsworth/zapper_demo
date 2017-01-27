package com.ap.zapper.services;

import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class RequestBuilder {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String TAG = RequestBuilder.class.getSimpleName();
}
