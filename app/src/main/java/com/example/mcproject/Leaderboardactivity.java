package com.example.mcproject;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Leaderboardactivity extends AppCompatActivity {
    private FirebaseAuth fauthSS;
    private DatabaseReference dataref, dataref1;
    public static TextView gamename;
    int gamefound;
    private TextView gameId;
    private ProgressBar progressBar;

    ListView simpleList;
    //String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    Map<String, Long> map = new HashMap<String, Long>();
    ArrayList<String> winner = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboardactivity);
        dataref= FirebaseDatabase.getInstance().getReference().child("GsmeDatabase");
        gamename= (TextView)findViewById(R.id.gamenametextbox);
        dataref1 = FirebaseDatabase.getInstance().getReference().child("leaderboard");
        simpleList = (ListView)findViewById(R.id.simpleListView);
        gameId= (TextView)findViewById(R.id.gameId);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                        progressBar.setVisibility(View.VISIBLE);
                        dataref1 = dataref1.child(gameseached);
                        simpleList = (ListView)findViewById(R.id.simpleListView);
                        try {
                            getData();
                            (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    compare();
                                    gameId.setText(gameseached);
                                    if(winner.size()>0) {
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Leaderboardactivity.this, R.layout.activity_listview, R.id.textView, winner);
                                        simpleList.setAdapter(arrayAdapter);
                                    }
                                    else{
                                        Toast.makeText(Leaderboardactivity.this, "No one played this game yet", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 5000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }



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

    public void getData() throws InterruptedException {
        dataref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = "";
                long value = 0;
                int i = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot ds1 : ds.getChildren()){
                        System.out.println("Key- "+ds1.getKey()+" Value - " + ds1.getValue());
                        if(ds1.getKey().equals("emailid"))
                            key = (String) ds1.getValue();
                        if(ds1.getKey().equals("finishtime"))
                            value = (long) ds1.getValue();
                        map.put(key,value);
                        System.out.println("Inserted in map = " + i++ + " Map size =  " + map.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void compare(){
        System.out.println("Length of map - " + map.size());
        List<Map.Entry<String, Long>> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Long> result = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        ArrayList<String> keys = new ArrayList<String>(result.keySet());
        Map<String, Long> res = new LinkedHashMap<>();
        String k = "";
        for(int i = result.size()-1;i>=0;i--){
            System.out.println("Keys - " + keys.get(i));
            k = keys.get(i);
            System.out.println("Res key - " + k);
            res.put(k,result.get(k));
        }

        result.clear();
        result = res;

        System.out.println("Hashmap - ");
        int i = 1;
        for (String name: result.keySet()){
            String key = name.toString();
            String value = result.get(name).toString();
            long time = result.get(name);
            long min = time / 60;
            long sec = time % 60;
            String temp = "" + i + ". " + key + "   " + min + ":" + sec ;
            winner.add(temp);
            System.out.println(key + " " + value);
            i++;
        }



        System.out.println("Length of winner - " + winner.size());
    }


}
