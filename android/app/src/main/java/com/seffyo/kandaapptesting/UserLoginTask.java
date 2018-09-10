package com.seffyo.kandaapptesting;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by renkar on 14.09.2017.
 */

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
    private final String mEmail;
    private final String mPassword;
    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Intent intent;

    private final LoginActivity loginActivity;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    UserLoginTask(String email, String password, LoginActivity loginActivityObject, Intent intentObject) {
        intent = intentObject;
        loginActivity = loginActivityObject;
        mEmail = email;
        mPassword = password;
        mEmailView = (AutoCompleteTextView) loginActivity.findViewById(R.id.email);

        mPasswordView = (EditText) loginActivity.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    loginActivity.attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        String param1 = "2222";
        String param2 = "1111";

        String query = null;
        try {
            query = String.format("username=%s&password=%s",
                    URLEncoder.encode(param1, charset),
                    URLEncoder.encode(param2, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String urlStr = "http://10.0.2.2:5000/checklogin";
        String postParameters = "username=1111&password=1111";
        String server_response;
        URL url;
        URLConnection connection;

        try {
            connection = new URL(urlStr).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.setConnectTimeout(5000);

            OutputStream output = connection.getOutputStream();
            output.write(query.getBytes(charset));

            InputStream response = connection.getInputStream();
            String result = response.toString();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mEmail)) {
                // Account exists, return true if the password matches.
                return pieces[1].equals(mPassword);
            }
        }

        // TODO: register the new account here.
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mAuthTask = null;
        loginActivity.showProgress(false);

        if (success) {
            loginActivity.startMain();
        } else {
            mPasswordView.setError(loginActivity.getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mAuthTask = null;
        loginActivity.showProgress(false);
    }

}
