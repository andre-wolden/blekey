package com.seffyo.kandaapptesting.activities.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.common.Alert;
import com.seffyo.kandaapptesting.common.NetworkFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class NetworkActivity extends FragmentActivity implements DownloadCallback {

    // Reference to the TextView showing fetched data, so we can clear it with a button
    // as necessary.
    private TextView mDataText;
    private TextView mStatusText;
    private ListView mGroceryList;

    // Keep a reference to the NetworkFragment which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        mDataText = (TextView) findViewById(R.id.data_text);
        mStatusText = (TextView) findViewById(R.id.statusTextView);
        mGroceryList = (ListView) findViewById(R.id.grocery_list);
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "http://seffyo.synology.me/api/Grocery/GetAllGroceries");
    }

    public void fetch(View view) {
        startDownload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            case R.id.fetch_action:
                startDownload();
                return true;
            // Clear the text and cancel download.
            /*
            case R.id.clear_action:
                finishDownloading();
                mStatusText.setText("Cleared");
                mDataText.setText("");
                return true;
                */
        }
        return false;
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    ArrayList<String> getArrayListFromJsonArray(JSONArray jsonArray){
        ArrayList<String> list = new ArrayList<String>();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i=0;i<len;i++){
                try {
                    list.add(jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(result);
                ArrayList<String> arrayList = getArrayListFromJsonArray(jsonArray);
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
                ListView listView = (ListView) findViewById(R.id.grocery_list);
                listView.setAdapter(itemsAdapter);

                /*
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject oneItem = jsonArray.getJSONObject(i);
                    String name = oneItem.getString("name");
                    mDataText.append(name);

                    Button btnName = new Button(this);
                    btnName.setText(name);
                    btnName.setLayoutParams(new ActionBar.LayoutParams(
                            ActionBar.LayoutParams.WRAP_CONTENT,
                            ActionBar.LayoutParams.WRAP_CONTENT,
                            ActionBar.LayoutParams.MATCH_PARENT));
                    mGroceryList.addView(btnName);
                    }
                    */
            }
                catch (final JSONException e) {
                Alert alert = new Alert();
                    alert.ShowAlert(this, "Error", "Something went wrong!" + e.getMessage());
            }

            mStatusText.setText("Success!");

        } else {
            mStatusText.setText("Error!");
            mDataText.setText(getString(R.string.connection_error));
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                mDataText.setText("" + percentComplete + "%");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }
}
