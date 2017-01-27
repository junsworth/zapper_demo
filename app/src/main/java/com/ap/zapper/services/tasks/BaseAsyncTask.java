package com.ap.zapper.services.tasks;

import android.os.AsyncTask;

import com.ap.zapper.listeners.ProgressListener;
import com.ap.zapper.services.requests.BaseRequest;
import com.ap.zapper.services.responses.BaseResponse;

import okhttp3.OkHttpClient;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public abstract class BaseAsyncTask extends AsyncTask <BaseRequest, Void, BaseResponse> {

    protected OkHttpClient client;
    protected ProgressListener progressListener;

    public BaseAsyncTask(OkHttpClient client, ProgressListener listener) {
        super();
        this.client = client;
        this.progressListener = listener;
    }
}
