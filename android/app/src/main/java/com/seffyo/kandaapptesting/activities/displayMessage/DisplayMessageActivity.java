package com.seffyo.kandaapptesting.activities.displayMessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.activities.main.MainActivity;

public class DisplayMessageActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String messageToShow = settings.getString("Name", "default");

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(messageToShow);
    }
}
