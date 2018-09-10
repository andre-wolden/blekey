package com.seffyo.kandaapptesting.activities.networkOkHttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.common.Alert;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity {
    private static final String HTTP_URL = "http://seffyo.synology.me/api/Grocery/GetAllGroceries";
    private TextView httpResult;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);

        client = new OkHttpClient();

        httpResult = (TextView) findViewById(R.id.okHttpResult);
    }

    public void FetchData(View view){
        GetData(HTTP_URL);
    }

    private void GetData(String url){
        httpResult.setText("");
        try {
            httpResult.append(DownloadFromUrl(url));
        } catch (IOException e) {
            Alert.ShowAlert(this, "ERROR", e.getMessage());
        }
    }

    private String DownloadFromUrl(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
