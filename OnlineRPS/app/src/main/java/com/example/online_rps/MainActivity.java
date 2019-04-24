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
public class MainActivity extends AppCompatActivity {
    public TextView opponent;
    public String user;
    public Button shoot;
    private MatchTask mAuthTask = null;
    public final String PATH = "https://cs.binghamton.edu/~jsuhr2/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty())
        {
            user = bundle.getString("User");
        }
        opponent = findViewById(R.id.search);
        shoot = findViewById(R.id.submit);
        shoot.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findMatch();
            }
        });
    }
    private void findMatch() {
        if (mAuthTask != null) {
            return;
        }
        opponent.setError(null);
        String tempU = user;
        String tempO = opponent.getText().toString();

        mAuthTask = new MatchTask(tempU, tempO);
        mAuthTask.execute((Void) null);
    }
    void switchActivities(String user1, String user2, int number)
    {
        Intent intent = new Intent(MainActivity.this, GameWindow.class);
        Bundle bundle = new Bundle();
        bundle.putString("User1", user1);
        bundle.putString("User2", user2);
        bundle.putInt("Number", number);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public class MatchTask extends AsyncTask<Void, Void, Boolean> {

        private final String user1;
        private final String user2;

        MatchTask(String user1, String user2) {
            this.user1 = user1;
            this.user2 = user2;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url = new URL(PATH + "match.php");
                HttpURLConnection connect = (HttpURLConnection) url
                        .openConnection();
                connect.setReadTimeout(15000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("POST");
                connect.setDoInput(true);
                connect.setDoOutput(true);

                OutputStream os = connect.getOutputStream();
                String s = "username1=" + user1 + "&username2=" + user2;
                os.write(s.getBytes());
                os.close();

                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                System.out.println(line);
                if (line.equals("1Success") || line.equals("2Success")) {
                    //switch activities
                    System.out.println("Game found");
                    if(line.equals("1Success"))
                    {
                        switchActivities(user1, user2, 1);
                    }
                    else
                    {
                        switchActivities(user2, user1, 2);
                    }
                    return true;
                } else {
                    System.out.println("Opponent doesn't exist");
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
