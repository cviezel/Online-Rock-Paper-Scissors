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

public class GameWindow extends AppCompatActivity {
    public Button rock;
    public Button paper;
    public Button scissors;
    public Button refresh;
    public TextView result;
    public String user1, user2;
    public int number;
    public int move = 0;
    public Boolean won = false;

    private GameTask mAuthTask = null;
    public final String PATH = "https://cs.binghamton.edu/~jsuhr2/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_window);

        rock = findViewById(R.id.rock);
        paper = findViewById(R.id.paper);
        scissors = findViewById(R.id.scissors);
        result = findViewById(R.id.result);
        refresh = findViewById(R.id.refresh);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(!bundle.isEmpty())
        {
            user1 = bundle.getString("User1");
            user2 = bundle.getString("User2");
            number = bundle.getInt("Number");
        }
        refresh.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                playGame();
            }
        });
        rock.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                move = 1;
                playGame();
                rock.setClickable(false);
                paper.setClickable(false);
                scissors.setClickable(false);
            }
        });
        paper.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                move = 2;
                playGame();
                rock.setClickable(false);
                paper.setClickable(false);
                scissors.setClickable(false);
            }
        });
        scissors.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                move = 3;
                playGame();
                rock.setClickable(false);
                paper.setClickable(false);
                scissors.setClickable(false);
            }
        });
    }

    private void playGame() {
        if (mAuthTask != null) {
            return;
        }

        mAuthTask = new GameTask(user1, user2, number, move);
        mAuthTask.execute((Void) null);

    }
    public class GameTask extends AsyncTask<Void, Void, Boolean> {

        private final String user1;
        private final String user2;
        private final int number;
        private final int move;

        GameTask(String user1, String user2, int number, int move) {
            this.user1 = user1;
            this.user2 = user2;
            this.number = number;
            this.move = move;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (move == 0)
                {
                    mAuthTask = null;
                    return false;
                }
                URL url = new URL(PATH + "game.php");
                HttpURLConnection connect = (HttpURLConnection) url
                        .openConnection();
                connect.setReadTimeout(15000);
                connect.setConnectTimeout(15000);
                connect.setRequestMethod("POST");
                connect.setDoInput(true);
                connect.setDoOutput(true);

                OutputStream os = connect.getOutputStream();
                String s = "user1=" + user1 + "&move=" + move + "&user2=" + user2 + "&number=" + number;
                os.write(s.getBytes());
                os.close();

                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                System.out.println(line);

                //ex: 1 2
                //extract 1 and 2, change screen to win/loes if both there
                //if either has a 0 in it, return false and check again in refresh function

                if (line.equals("Failed")) {
                    mAuthTask = null;
                    result.setText("Awaiting other move");
                    return false;
                } else {
                    //do logic for winner and update screen textbox
                    String[] splitted = line.split("\\s+");
                    int move1 = Integer.parseInt(splitted[0]);
                    int move2 = Integer.parseInt(splitted[1]);

                    if(move1 == 0 || move2 == 0)
                    {
                        result.setText("Awaiting other move");
                        return true;
                    }

                    if (move1 == move2)
                    {
                        result.setText("You tied!");
                        return true;
                    }

                    if(move1 == 1 && move2 == 2)
                    {
                        if(number == 2)
                        {
                            won = true;
                        }
                    }
                    else if(move1 == 1 && move2 == 3)
                    {
                        if(number == 1)
                        {
                            won = true;
                        }
                    }
                    else if(move1 == 2 && move2 == 1)
                    {
                        if(number == 1)
                        {
                            won = true;
                        }
                    }
                    else if(move1 == 2 && move2 == 3)
                    {
                        if(number == 2)
                        {
                            won = true;
                        }
                    }
                    else if(move1 == 3 && move2 == 1)
                    {
                        if(number == 2)
                        {
                            won = true;
                        }
                    }
                    else if(move1 == 3 && move2 == 2)
                    {
                        if(number == 1)
                        {
                            won = true;
                        }
                    }
                    if(won)
                    {
                        result.setText("You won!");
                    }
                    else
                    {
                        result.setText("You lost!");
                    }
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
