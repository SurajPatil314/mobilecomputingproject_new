package com.example.mcproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
    Double latitudedata,datalatitudefinal,datalongitudefinal;
    LocationManager locationManager;
    Integer questionnumber = 0,questionnumberfinal;
    String lastq,hintdfinal,questiondfinal,creatorofgamefinal;
    Button btn_yes ;
    Button btn_no;



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
            questiondfinal=questiond;
            String hintd = hintstore.getText().toString();
            hintdfinal=hintd;
            questions que = new questions();
            que.setQuestion(questiond);

            que.setHint(hintd);
            que.setLatitude(datalatitude);
            datalatitudefinal=datalatitude;
            que.setLongitude(datalongitude);
            datalongitudefinal=datalongitude;
            que.setCreator(creatorofgame);
            creatorofgamefinal=creatorofgame;
            que.setQno(questionnumber);
            questionnumberfinal=questionnumber;
            lastq=questionnumber.toString();
            dataref.child(gamename).child(questionnumber.toString()).setValue(que);
            Toast.makeText(new_game_creation_page.this, "Successfully saved your question", Toast.LENGTH_LONG).show();

            alertboxfunction();
            // dataref.push().setValue(newuser1);
        }
        else
        {
            Toast.makeText(new_game_creation_page.this, "to creategame you need to enter game name.Enter game name first in previous screen", Toast.LENGTH_LONG).show();
        }
    }

    void alertboxfunction() {
        final AlertDialog.Builder newQalert = new AlertDialog.Builder(new_game_creation_page.this);
        View mview = getLayoutInflater().inflate(R.layout.nextquestionset, null);
         btn_yes = (Button) mview.findViewById(R.id.yesQ);
         btn_no = (Button) mview.findViewById(R.id.noQ);


        final AlertDialog adilog = newQalert.create();
        adilog.setView(mview);

        adilog.setCanceledOnTouchOutside(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionstore.setText(" ");
                hintstore.setText(" ");
                adilog.dismiss();


            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastq!=null && hintdfinal!=null && questiondfinal!=null && datalatitudefinal!=null && datalongitudefinal!=null && creatorofgamefinal!=null)
                {
                    System.out.println("lastq value in no button"+lastq);
                    fauth = FirebaseAuth.getInstance();
                    questions que = new questions();
                    que.setQuestion(questiondfinal);

                    que.setHint(hintdfinal);
                    que.setLatitude(datalatitudefinal);
                    que.setLongitude(datalongitudefinal);
                    que.setCreator(creatorofgamefinal);
                    que.setFinalquestion("yes");
                    Toast.makeText(new_game_creation_page.this, "game saved successfully", Toast.LENGTH_LONG).show();
                    dataref.child(gamename).child(lastq).setValue(que);
                    Intent i = new Intent(getBaseContext(), create_join_game.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(new_game_creation_page.this, "Some data is missing for last question", Toast.LENGTH_LONG).show();
                    adilog.dismiss();
                }





            }
        });
        adilog.show();


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

