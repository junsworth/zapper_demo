package com.ap.zapper.services;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public final class ApiCall {

    //GET network request
    public static Response GET(OkHttpClient client, String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

        return response;
    }

}
