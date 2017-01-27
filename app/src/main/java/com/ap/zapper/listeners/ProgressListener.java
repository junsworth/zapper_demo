package com.ap.zapper.listeners;

/**
 * Created by jonathanunsworth on 2017/01/24.
 */

public interface ProgressListener {
    void showProgressDialog(CharSequence message);
    void dismissProgressDialog();
}
