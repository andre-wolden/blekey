package com.seffyo.kandaapptesting.activities.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.seffyo.kandaapptesting.activities.adapter.adapters.Grocery;
import com.seffyo.kandaapptesting.activities.utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by renkar on 17.10.2017.
 */

public class myLoader extends AsyncTaskLoader<String> {

    /** Tag for log messages */
    private static final String LOG_TAG = myLoader.class.getName();

    /** Query URL */
    private String mUrl;

    private NetworkUtils networkUtils;

    public myLoader(Context context, String url) {
        super(context);
        networkUtils = new NetworkUtils(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        else {
            StringBuilder result = new StringBuilder();
            URL url;
            try {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                url = new URL(mUrl);
                result.append(networkUtils.downloadUrl(url));
            }
            catch (MalformedURLException e){

            }
            catch (IOException e){

            }
            return result.toString();
        }
    }
}
