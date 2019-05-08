package com.example.mcproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class create_join_game extends AppCompatActivity {

LinearLayout creategame;
LinearLayout searchgame;

private TextView gamename;
private TextView searchgame_tostart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_game);
        creategame= (LinearLayout) findViewById(R.id.hiddencreate);
        searchgame= (LinearLayout) findViewById(R.id.hiddensearch);
        gamename= (TextView)findViewById(R.id.game_name);
        searchgame_tostart=(TextView)findViewById(R.id.search_game);
    }

    public void setnewgame(View view) {
        creategame.setVisibility(view.VISIBLE);
        searchgame.setVisibility(view.INVISIBLE);
    }

    public void setsearchgame(View view) {
        searchgame.setVisibility(view.VISIBLE);
        creategame.setVisibility(view.INVISIBLE);
        startGame(view);
    }

    public void go_create_game(View view) {
        String nameofgame = gamename.getText().toString().trim();
        Intent i = new Intent(getBaseContext(), new_game_creation_page.class);
        i.putExtra("gamename",nameofgame);
        startActivity(i);
    }

    public void go_search_game(View view) {
        String gameseached= searchgame_tostart.getText().toString().trim();
        Intent i = new Intent(getBaseContext(), searched_game_page.class);
        i.putExtra("gamesearched",gameseached);
        startActivity(i);
    }

    public void startGame(View view) {
        Intent intent = new Intent(getApplicationContext(), GameOn.class);
        Bundle b = new Bundle();
        b.putString("GameName", searchgame_tostart.getText().toString().trim());
        intent.putExtras(b);
        startActivity(intent);
    }
}