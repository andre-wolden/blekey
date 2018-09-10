package com.seffyo.kandaapptesting.activities.loader;

import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.activities.network.DownloadCallback;
import com.seffyo.kandaapptesting.activities.utils.NetworkUtils;

import org.w3c.dom.Text;

public class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, DownloadCallback{

    /**
     * Constant value for the loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final String LOG_TAG = LoaderActivity.class.getName();
    private static final int LOADER_ID = 1;
    private static final String GROCERY_URL = "http://seffyo.synology.me/api/Grocery/GetAllGroceries";

    private ProgressBar progressBar;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("LOADER");
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        resultView = (TextView) findViewById(R.id.textview_result);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        onGetResult();
    }

    public void onGetResult(){
        LoaderManager loaderManager = getLoaderManager();
        progressBar.setVisibility(View.VISIBLE);

        loaderManager.initLoader(LOADER_ID, null, this);
    }

    public void getResult(View view){
        onGetResult();
    }

    public void clearResult(View view) {
        resultView.setText("");
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new myLoader(this, GROCERY_URL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        progressBar.setVisibility(View.GONE);
        resultView.setText("");
        resultView.append(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        Log.d(LOG_TAG, "Loader reset");
    }


    @Override
    public void updateFromDownload(String result) {

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        return null;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {

    }
}
