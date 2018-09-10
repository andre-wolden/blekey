package com.seffyo.kandaapptesting.activities.node;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.activities.fragment.adapters.SimpleFragmentPagerAdapter;
import com.seffyo.kandaapptesting.activities.main.MainActivity;
import com.seffyo.kandaapptesting.activities.network.DownloadCallback;
import com.seffyo.kandaapptesting.common.Alert;
import com.seffyo.kandaapptesting.common.NetworkFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class NodeActivity extends MainActivity implements DownloadCallback{
    private TextView mDataText;
    private TextView mStatusText;
    private NetworkFragment mNetworkFragment;
    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);

        mDataText = (TextView) findViewById(R.id.mDataText);
        mStatusText = (TextView) findViewById(R.id.mStatusText);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("NODE");
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "http://seffyo.synology.me/api/Sensor/GetSensorValue/15/1");
    }

    public void fetch(View view) {
        startDownload();
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
        Log.d("Node", "updateFromDownload: Result: " + result);
        if (result != null) {
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(result);
                ArrayList<String> arrayList = getArrayListFromJsonArray(jsonArray);
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
                ListView listView = (ListView) findViewById(R.id.grocery_list);
                listView.setAdapter(itemsAdapter);
                mStatusText.setText("Success!");
            }
            catch (final JSONException e) {
                Alert alert = new Alert();
                alert.ShowAlert(this, "Error", "Something went wrong! " + e.getMessage() + "\r\nMore Info:" + result);
            }


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
