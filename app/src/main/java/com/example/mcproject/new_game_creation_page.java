package com.example.mcproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class new_game_creation_page extends AppCompatActivity implements LocationListener {
    private FirebaseAuth fauth;
    private TextView creatorname;
    String creatorofgame;
    String gamename;
    private DatabaseReference dataref;
    private TextView questionstore;
    private TextView hintstore;
    Double longitudedata;
    Double latitudedata;
    LocationManager locationManager;
    Integer questionnumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_creation_page);
        getLocation();
        FirebaseApp.initializeApp(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        dataref = FirebaseDatabase.getInstance().getReference().child("GsmeDatabase");
        creatorname = (TextView) findViewById(R.id.creatorname);
        questionstore = (TextView) findViewById(R.id.question);
        hintstore = (TextView) findViewById(R.id.hint);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gamename = extras.getString("gamename");  //getting game name
            dataref.child(gamename).setValue(gamename);
        }
    }

    public void getcreatornamebuttonaction(View view) {

        creatorofgame = creatorname.getText().toString().trim();
        Toast.makeText(new_game_creation_page.this, "Successfully saved creator name", Toast.LENGTH_LONG).show();

    }

    public void submitquestonbuttoactin(View view) {

        createnewquestion();


    }

    void createnewquestion() {
        questionnumber++;
        System.out.println("questionnumber" + questionnumber);
        System.out.println("user location::"+longitudedata+"::"+latitudedata);
        double datalongitude = longitudedata;
        double datalatitude = latitudedata;
        // Log.i("username::",usernamess);
        System.out.printf("Current Location: " + datalatitude + ", " + datalongitude);
        fauth = FirebaseAuth.getInstance();

        if (gamename != null) {

            System.out.println("longitude::" + datalongitude + "::latitude" + datalatitude);
            String questiond = questionstore.getText().toString();
            String hintd = hintstore.getText().toString();
            questions que = new questions();
            que.setQuestion(questiond);
            que.setHint(hintd);
            que.setLatitude(datalatitude);
            que.setLongitude(datalongitude);
            que.setCreator(creatorofgame);
            que.setQno(questionnumber);
            dataref.child(gamename).child(questionnumber.toString()).setValue(que);
            Toast.makeText(new_game_creation_page.this, "Successfully saved your question", Toast.LENGTH_LONG).show();

            alertboxfunction();
            // dataref.push().setValue(newuser1);
        }
    }

    void alertboxfunction() {
        final AlertDialog.Builder newQalert = new AlertDialog.Builder(new_game_creation_page.this);
        View mview = getLayoutInflater().inflate(R.layout.nextquestionset, null);
        Button btn_yes = (Button) mview.findViewById(R.id.yesQ);
        Button btn_no = (Button) mview.findViewById(R.id.noQ);

        newQalert.setView(mview);
        final AlertDialog adilog = newQalert.create();
        adilog.setCanceledOnTouchOutside(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adilog.dismiss();


            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), create_join_game.class);
                startActivity(i);


            }
        });

        newQalert.show();
    }


    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.printf("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        latitudedata = location.getLatitude();
        longitudedata = location.getLongitude();
        System.out.printf("Current Location in onLocationChanged: " + latitudedata + ", " + longitudedata);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(new_game_creation_page.this, "Please Enable GPS and Internet", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        questionstore.setText("");
        hintstore.setText("");
    }
}

