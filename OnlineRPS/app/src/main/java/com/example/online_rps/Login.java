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
    public Button register;
    private UserLoginTask mAuthTask = null;
    public final String PATH = "https://cs.binghamton.edu/~jsuhr2/";
    //public final String PATH = "https://cs.binghamton.edu/~jsuhr2/cs441/Online-Rock-Paper-Scissors/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
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

        username.setError(null);
        password.setError(null);

        String tempU = username.getText().toString();
        String tempP = password.getText().toString();

        mAuthTask = new UserLoginTask(tempU, tempP);
        mAuthTask.execute((Void) null);
    }
    void switchActivities(String user)
    {
        Intent intent = new Intent(Login.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("User", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String login_user;
        private final String login_password;

        UserLoginTask(String user, String password) {
            login_user = user;
            login_password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
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
                String s = "username=" + login_user + "&password=" + login_password;
                os.write(s.getBytes());
                os.close();

                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                if (line.equals("Success")) {
                    //switch activities
                    System.out.println("Login Success");
                    switchActivities(login_user);
                    return true;
                }
                else if(line.equals("Password"))
                {
                    System.out.println("Wrong Password");
                    mAuthTask = null;
                    return false;
                }
                else
                {
                    System.out.println("Wrong username, registering the user");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            // TODO: register the new account here.
            try {
                System.out.println("Registering user");
                URL url = new URL(PATH + "register.php");
                HttpURLConnection connect = (HttpURLConnection) url
                        .openConnection();
                connect.setReadTimeout(15000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("POST");
                connect.setDoInput(true);
                connect.setDoOutput(true);

                OutputStream os = connect.getOutputStream();
                String s = "username=" + login_user + "&password=" + login_password;
                os.write(s.getBytes());
                os.close();

                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                System.out.println(line);
                if (line.equals("Success")) {
                    System.out.println("Register Success");
                    switchActivities(login_user);
                    return true;
                } else {
                    System.out.println("Register Fail");
                    mAuthTask = null;
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}