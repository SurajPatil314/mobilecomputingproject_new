package com.example.mcproject;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import static com.example.mcproject.R.layout.activity_listview;

public class leaderboard_afterlastquestion extends AppCompatActivity {
private TextView gameId;
    ListView simpleList;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};
    Map<String, Long> map = new HashMap<String, Long>();
    ArrayList<String> winner = new ArrayList<String>();
    private DatabaseReference dataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_afterlastquestion);
        gameId = (TextView) findViewById(R.id.gameId);
        gameId.setText("qwe");
        dataref = FirebaseDatabase.getInstance().getReference().child("leaderboard").child("qwe");
        simpleList = (ListView)findViewById(R.id.simpleListView);
        try {
            getData();
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    compare();
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(leaderboard_afterlastquestion.this, R.layout.activity_listview, R.id.textView, winner);
                    simpleList.setAdapter(arrayAdapter);
                }
            }, 5000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void getData() throws InterruptedException {
       //DatabaseReference d1 = dataref.
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
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


        System.out.println("Hashmap - ");
        for (String name: result.keySet()){
            String key = name.toString();
            winner.add(key);
            String value = result.get(name).toString();
            System.out.println(key + " " + value);
        }

        System.out.println("Length of winner - " + winner.size());
    }

}
