package com.example.mcproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Leaderboardactivity extends AppCompatActivity {
    private FirebaseAuth fauthSS;
    private DatabaseReference dataref;
    private TextView gamename;
    int gamefound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboardactivity);
        dataref= FirebaseDatabase.getInstance().getReference().child("GsmeDatabase");
        gamename= (TextView)findViewById(R.id.gamenametextbox);
    }

    public void searchgame(View view) {
        final String gameseached= gamename.getText().toString();

        System.out.println("game name for leaderbaord"+gameseached);
        if(gameseached==null)
        {
            Toast.makeText(Leaderboardactivity.this, "please enter game name to search laederboard", Toast.LENGTH_LONG).show();
        }
        else
        {
            gamefound=0;
            dataref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren())
                    {
                        System.out.println("data in for loop"+data.getKey());
                        if(data.getKey().equals(gameseached))
                        {
                            System.out.println("data in for loop in if loop"+data.getKey());
                            gamefound=1;
                            break;
                        }

                    }

                    if(gamefound==1)
                    {
                        Toast.makeText(Leaderboardactivity.this, "searched game record found successfully", Toast.LENGTH_LONG).show();
                        //your logic comes here Ashlesha

                    }
                    else
                    {
                        System.out.println("gamefound value is 0 at the end");
                        Toast.makeText(Leaderboardactivity.this, "searched game record not found", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Leaderboardactivity.this, "Server error, please try again", Toast.LENGTH_LONG).show();

                }
            });
        }

    }

    public void goback(View view) {
        Intent i = new Intent(getBaseContext(), create_join_game.class);
        startActivity(i);
    }
}
