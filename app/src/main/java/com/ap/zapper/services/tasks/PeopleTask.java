package com.ap.zapper.services.tasks;

import android.content.Context;
import android.os.Environment;

import com.ap.zapper.listeners.PeopleTaskListener;
import com.ap.zapper.listeners.ProgressListener;
import com.ap.zapper.services.ApiCall;
import com.ap.zapper.services.requests.BaseRequest;
import com.ap.zapper.services.requests.PeopleRequest;
import com.ap.zapper.services.responses.BaseResponse;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class PeopleTask extends BaseAsyncTask {

    private int mStatus = -1;
    private Context context;
    private PeopleTaskListener listener;

    public PeopleTask(OkHttpClient client, Context context, PeopleTaskListener listener, ProgressListener progressListener) {
        super(client, progressListener);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressListener!=null) {
            progressListener.showProgressDialog("Fetching people list...");
        }
    }

    @Override
    protected BaseResponse doInBackground(BaseRequest... params) {

        PeopleRequest request = (PeopleRequest)params[0];

        try {
            Response httpResponse = ApiCall.GET(this.client, request.getUrl());

            mStatus = httpResponse.code();

            switch (mStatus) {
                case 200:

                    String body = httpResponse.body().string();

                    final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";

                    final String fileDir = Environment.getExternalStorageDirectory().getPath() + cacheDir;

                    File file = new File(fileDir + File.separator + "people.txt");

                    if(file.exists()) {
                        file.createNewFile();
                    }

                    FileUtils.writeStringToFile(file, body);

                    break;
                case 204:
                    break;
                default:
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(progressListener!=null) {
            progressListener.dismissProgressDialog();
        }
    }

    @Override
    protected void onPostExecute(BaseResponse baseResponse) {
        super.onPostExecute(baseResponse);

        if(listener!=null) {
            listener.peopleTaskFinished();
        }

        if(progressListener!=null) {
            progressListener.dismissProgressDialog();
        }

    }
}
