package com.ap.zapper.services.requests;

/**
 * Created by jonathanunsworth on 2017/01/23.
 */

public class BaseRequest {
    private String url;

    public BaseRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
