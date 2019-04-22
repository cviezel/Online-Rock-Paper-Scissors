package com.example.online_rps;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class Login extends AppCompatActivity {

    public TextView username;
    public TextView password;
    public Button login;
    public Button register;
    private UserLoginTask mAuthTask = null;
    public final String PATH = "https://cs.binghamton.edu/~jsuhr2/cs441/Online-Rock-Paper-Scissors/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                attemptLogin();
            }
        });
    }
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        username.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String tempU = username.getText().toString();
        String tempP = password.getText().toString();

        mAuthTask = new UserLoginTask(tempU, tempP);
        mAuthTask.execute((Void) null);
    }
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        // Reuse php files from assignment 4 - database name: kfranke1_assignment5
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            /*
            try {
                URL url = new URL(PATH + "login.php");
                HttpURLConnection connect = (HttpURLConnection) url
                        .openConnection();
                connect.setReadTimeout(15000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("POST");
                connect.setDoInput(true);
                connect.setDoOutput(true);

                OutputStream os = connect.getOutputStream();
                String s = "username=" + mEmail + "&password=" + mPassword;
                os.write(s.getBytes());
                os.close();

                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                if (line.equals("Success")) {
                    Log.d("Success", "success");
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            */
            // TODO: register the new account here.
            try {
                URL url = new URL(PATH + "register.php");
                HttpURLConnection connect = (HttpURLConnection) url
                        .openConnection();
                connect.setReadTimeout(15000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("POST");
                connect.setDoInput(true);
                connect.setDoOutput(true);

                OutputStream os = connect.getOutputStream();
                String s = "username=" + mEmail + "&password=" + mPassword;
                os.write(s.getBytes());
                os.close();

                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                if (line.equals("Success")) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}