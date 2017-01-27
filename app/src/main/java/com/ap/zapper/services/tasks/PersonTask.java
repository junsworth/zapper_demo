package com.ap.zapper.services.tasks;

import com.ap.zapper.controllers.PersonController;
import com.ap.zapper.listeners.PersonTaskListener;
import com.ap.zapper.listeners.ProgressListener;
import com.ap.zapper.services.ApiCall;
import com.ap.zapper.services.requests.BaseRequest;
import com.ap.zapper.services.requests.PersonRequest;
import com.ap.zapper.services.responses.BaseResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class PersonTask extends BaseAsyncTask {

    private int mStatus = -1;
    private PersonTaskListener listener;

    public PersonTask(OkHttpClient client, PersonTaskListener listener, ProgressListener progressListener) {
        super(client, progressListener);
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(progressListener!=null) {
            progressListener.showProgressDialog("Fetching person details...");
        }

    }

    @Override
    protected BaseResponse doInBackground(BaseRequest... params) {

        PersonRequest request = (PersonRequest) params[0];

        try {

            Response httpResponse = ApiCall.GET(this.client, request.getUrl() + "/" + request.getPerson().getId());

            mStatus = httpResponse.code();

            switch (mStatus) {
                case 200:

                    String body = httpResponse.body().string();

                    System.out.println(body);

                    try {

                        JSONObject object = new JSONObject(body);

                        if(!object.isNull("id")) {
                            PersonController.getInstance().getPerson().setId(object.getInt("id"));
                        }

                        if(!object.isNull("firstName")) {
                            PersonController.getInstance().getPerson().setFirstName(object.getString("firstName"));
                        }

                        if(!object.isNull("lastName")) {
                            PersonController.getInstance().getPerson().setLastName(object.getString("lastName"));
                        }

                        if(!object.isNull("age")) {
                            PersonController.getInstance().getPerson().setAge(object.getInt("age"));
                        }

                        if(!object.isNull("favouriteColour")) {
                            PersonController.getInstance().getPerson().setFavouriteColour(object.getString("favouriteColour"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
            listener.personTaskFinished();
        }

        if(progressListener!=null) {
            progressListener.dismissProgressDialog();
        }
    }
}
