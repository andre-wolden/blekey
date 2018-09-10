package com.seffyo.kandaapptesting.activities.main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.seffyo.kandaapptesting.activities.androidme.AndroidMeMainActivity;
import com.seffyo.kandaapptesting.activities.loader.LoaderActivity;
import com.seffyo.kandaapptesting.activities.networkOkHttp.OkHttpActivity;
import com.seffyo.kandaapptesting.activities.node.NodeActivity;
import com.seffyo.kandaapptesting.common.Alert;
import com.seffyo.kandaapptesting.activities.bluetooth.BluetoothActivity;
import com.seffyo.kandaapptesting.activities.displayMessage.DisplayMessageActivity;
import com.seffyo.kandaapptesting.activities.fragment.FragmentActivity;
import com.seffyo.kandaapptesting.MainMenuActivity;
import com.seffyo.kandaapptesting.activities.network.NetworkActivity;
import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.activities.adapter.adapters.AdapterActivity;

public class MainActivity extends MainMenuActivity {
    public static final String EXTRA_MESSAGE = "com.seffyo.kandaapptesting.MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    private Alert alert = new Alert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("MAIN");
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void fetchGroceries(View view) {
        Intent intent = new Intent(this, NetworkActivity.class);
        startActivity(intent);
    }

    public void startBluetoothAct(View view) {
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    public void runNodeAct(View view){
        Intent intent = new Intent(this, NodeActivity.class);
        startActivity(intent);
    }

    public void runAdapterAct(View view){
        Intent intent = new Intent(this, AdapterActivity.class);
        startActivity(intent);
    }

    public void runFragmentAct(View view){
        Intent intent = new Intent(this, FragmentActivity.class);
        startActivity(intent);
    }

    public void runAndroidMeAct(View view){
        Intent intent = new Intent(this, AndroidMeMainActivity.class);
        startActivity(intent);
    }

    public void runLoaderAct(View view){
        Intent intent = new Intent(this, LoaderActivity.class);
        startActivity(intent);
    }
    public void runOkHttpAct(View view){
        Intent intent = new Intent(this, OkHttpActivity.class);
        startActivity(intent);
    }


    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        }

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
            storeMessage(message);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
     }

    // Implement this here
     /*
    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]
    */
     private void storeMessage(String message){
         SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
         SharedPreferences.Editor editor = settings.edit();
         editor.putString("Name", "Name is " + message);
         editor.commit();
     }
}
