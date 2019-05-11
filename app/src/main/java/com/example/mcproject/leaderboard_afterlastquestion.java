package com.example.mcproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class leaderboard_afterlastquestion extends AppCompatActivity {
private String gameName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_afterlastquestion);
        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;
        else {
            gameName = extras.getString("gamename_lastq");
        }
    }
}
