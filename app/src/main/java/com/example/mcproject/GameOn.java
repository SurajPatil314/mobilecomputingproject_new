package com.example.mcproject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GameOn extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    SupportMapFragment mapFragment;
    private FirebaseAuth fauth;
    private DatabaseReference dataref,datarefleader;
    private String gameName,locationpermission;
    GoogleMap mmap;
    SupportMapFragment mapFrag;
    LocationManager locationManager;
    Double longitudedata;
    Double latitudedata;

    private TextView hint;
    private TextView question;
    static Integer questionnumber = 1;


    private Handler customHandler = new Handler();
    private TextView timerValue;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    public static int totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameon);
        getLocation();
        questionnumber = 1;
        question = (TextView)findViewById(R.id.Question);
        question.setMovementMethod(new ScrollingMovementMethod());
        hint = (TextView) findViewById(R.id.showhinttext);
        hint.setMovementMethod(new ScrollingMovementMethod());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dataref = FirebaseDatabase.getInstance().getReference().child("GsmeDatabase");
        datarefleader= FirebaseDatabase.getInstance().getReference().child("leaderboard");

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;
        gameName = extras.getString("gamesearched");
        locationpermission=extras.getString("locationpermssion");  //added by Suraj for location permission check....05/09
        Log.d("pratik-location", locationpermission);
        showQuestion();

        timerValue = (TextView) findViewById(R.id.timerValue);
        starttimer();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mmap = googleMap;
        if(locationpermission.equals("Public") && mmap != null) {
            LatLng ll = new LatLng(latitudedata, longitudedata);
            mmap.clear();
            mmap.addMarker(new MarkerOptions().position(ll));
            mmap.moveCamera(CameraUpdateFactory.newLatLng(ll));
            mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18.0f));
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitudedata = loc.getLatitude();
            longitudedata = loc.getLongitude();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitudedata = location.getLatitude();
        longitudedata = location.getLongitude();
        if(locationpermission.equals("Public") && mmap != null) {
            LatLng ll = new LatLng(latitudedata, longitudedata);
            System.out.println("Latlag new SP"+ll);
            mmap.clear();
            mmap.addMarker(new MarkerOptions().position(ll));
            mmap.moveCamera(CameraUpdateFactory.newLatLng(ll));
            mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18.0f));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void showQuestion() {
        System.out.println("game name recieved from intent"+gameName);
        DatabaseReference ref = dataref.child(gameName).child(questionnumber.toString());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals("question")) {
                        question.setText(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error",databaseError.toString());
            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    public void showHints(View view) {
        DatabaseReference ref = dataref.child(gameName).child(questionnumber.toString());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals("hint")) {
                        hint.setText(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error",databaseError.toString());
            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    private void verifyAnswer(Location ansLocation, boolean islast) {
        getLocation();
        Location myLoc = new Location("myLoc");
        myLoc.setLatitude(latitudedata);
        myLoc.setLongitude(longitudedata);

        Log.d("Pratik-MyLocation:", latitudedata.toString()+"/"+longitudedata.toString());
        double distance = myLoc.distanceTo(ansLocation);
        Log.d("Pratik-", String.valueOf(distance));
        if(distance <= 5.00) {
            //It is correct, Show next question
            Toast.makeText(GameOn.this, "Correct answer, heading towards next question", Toast.LENGTH_LONG).show();
            if(islast) {
                //timer()
                System.out.println("in islast loop");
                stoptimer();
                //totaltime
                Log.d("message", "Congratulations! You won!");
                Toast.makeText(GameOn.this, "Congratulations! You finished the game", Toast.LENGTH_LONG).show();
                fauth = FirebaseAuth.getInstance();
                leaderboard le = new leaderboard();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email = user.getEmail();
                System.out.println("email::"+email);
                le.setEmailid(email);
                le.setFinishtime(totalTime);
                System.out.println("totaltime::"+totalTime);
                le.setGamename(gameName);

                datarefleader.child(gameName).child(uuid).setValue(le);
                //start new activity
                Intent i = new Intent(getBaseContext(), leaderboard_afterlastquestion.class);
                i.putExtra("gamename_lastq",gameName);
                startActivity(i);
            }
            questionnumber++;
            showQuestion();
        }
        else {
            Toast.makeText(GameOn.this, "Wrong answer. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    public void verifyAnswer(View view) {
        DatabaseReference ref = dataref.child(gameName).child(questionnumber.toString());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double ansLatitude = null;
                Double ansLongitude = null;
                boolean islast = false;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals("latitude"))
                         ansLatitude = (Double) ds.getValue();
                    if(ds.getKey().equals("longitude"))
                        ansLongitude = (Double)ds.getValue();
                    if(ds.getKey().equals("finalquestion"))
                        islast = true;
                }
                if(ansLatitude != null && ansLongitude != null) {
                    Log.d("Pratik-", ansLatitude.toString() + "/" + ansLongitude.toString());
                    Location anslocation = new Location("Answer");
                    anslocation.setLatitude(ansLatitude);
                    anslocation.setLongitude(ansLongitude);
                    verifyAnswer(anslocation, islast);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error",databaseError.toString());
            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);

    }

    public void starttimer() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void stoptimer() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int hours = mins/60;
            //put this value in database.
            totalTime = secs + (mins*60*60) + (hours*60*60);
            //totalTime = hours + ":" + mins + ":" + secs;
            timerValue.setText("" + hours + ":" + mins + ":"
                    + String.format("%02d", secs) );
            customHandler.postDelayed(this, 0);

        }

    };


}
