package com.example.mcproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.mcproject.MainActivity.fauthSS;
import static com.example.mcproject.MainActivity.mGoogleSignInClient;

public class create_join_game extends AppCompatActivity {

    LinearLayout creategame;
    LinearLayout searchgame;
    private FirebaseAuth fauthSS;
    private DatabaseReference dataref;
    private TextView gamename;
    private TextView searchgame_tostart;
    private Switch locswitch;

    int gamefound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_game);
        creategame= (LinearLayout) findViewById(R.id.hiddencreate);
        searchgame= (LinearLayout) findViewById(R.id.hiddensearch);
        gamename= (TextView)findViewById(R.id.game_name);
        searchgame_tostart=(TextView)findViewById(R.id.search_game);
        locswitch = (Switch) findViewById(R.id.switchlocation);
        dataref= FirebaseDatabase.getInstance().getReference().child("GsmeDatabase");
    }

    public void setnewgame(View view) {
        creategame.setVisibility(view.VISIBLE);
        searchgame.setVisibility(view.INVISIBLE);
    }

    public void setsearchgame(View view) {
        searchgame.setVisibility(view.VISIBLE);
        creategame.setVisibility(view.INVISIBLE);
        //startGame(view);
    }

    public void go_create_game(View view) {
        String nameofgame = gamename.getText().toString().trim();
        if(nameofgame!=null)
        {
            Intent i = new Intent(getBaseContext(), new_game_creation_page.class);
            i.putExtra("gamename",nameofgame);
            startActivity(i);
        }
        else
        {

        }

    }

    public void go_search_game(View view) {
        gamefound=0;
        String locationallow;
        if (locswitch.isChecked())
        {
            locationallow = locswitch.getTextOn().toString();
        }

        else
        {
            locationallow = locswitch.getTextOff().toString();
        }
        System.out.println("location switch value in search button"+locationallow);

         String gameseached= searchgame_tostart.getText().toString();
         final String gamesearched1=gameseached;
         final String locationallowed1=locationallow;


        System.out.println("gamesearched1 value found"+gamesearched1);
        if(gamesearched1==null)
        {
            Toast.makeText(create_join_game.this, "please enter game name to search and join", Toast.LENGTH_LONG).show();
        }
        else
        {
            System.out.println("game name for search by user::"+gamesearched1);
            dataref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren())
                    {
                        System.out.println("data in for loop"+data.getKey());
                        if(data.getKey().equals(gamesearched1))
                        {
                            System.out.println("data in for loop in if loop"+data.getKey());
                            gamefound=1;
                            break;
                        }

                    }

                    if(gamefound==1)
                    {
                        Intent i = new Intent(getBaseContext(), GameOn.class);
                        i.putExtra("gamesearched",gamesearched1);
                        i.putExtra("locationpermssion",locationallowed1);
                        startActivity(i);

                    }
                    else
                    {
                        System.out.println("gamefound value is 0 at the end");
                        Toast.makeText(create_join_game.this, "to creategame you need to enter game name.Enter game name first", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(create_join_game.this, "Server error, please try again", Toast.LENGTH_LONG).show();

                }
            });



        }


    }
/*
    public void startGame(View view) {
        String gamename = searchgame_tostart.getText().toString().trim();
        gamename = "firstgame";
        if(gamename.isEmpty())
            return;

        Intent intent = new Intent(getApplicationContext(), GameOn.class);
        Bundle b = new Bundle();
        b.putString("GameName", gamename);
        intent.putExtras(b);
        startActivity(intent);
    }
    commented by Suraj
    */

    public void logout(View view) {
        if(fauthSS != null)
            fauthSS.signOut();
        mGoogleSignInClient.signOut();
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }

    public void go_to_leaderboard(View view) {
        Intent i = new Intent(getBaseContext(), Leaderboardactivity.class);
        startActivity(i);
    }
}