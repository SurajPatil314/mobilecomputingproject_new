package com.example.mcproject;

import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
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
    private DatabaseReference dataref;
    private String gameName = "firstgame";
    GoogleMap mmap;
    SupportMapFragment mapFrag;
    LocationManager locationManager;
    Double longitudedata;
    Double latitudedata;

    private TextView hint;
    private TextView question;
    static Integer questionnumber = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameon);
        getLocation();
        question = (TextView)findViewById(R.id.Question);
        hint = (TextView) findViewById(R.id.showhinttext);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dataref = FirebaseDatabase.getInstance().getReference().child("GsmeDatabase");

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            return;
        gameName = extras.getString("GameName");
        showQuestion();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mmap = googleMap;
        LatLng ll = new LatLng(latitudedata, longitudedata);
        mmap.addMarker(new MarkerOptions().position(ll));
        mmap.moveCamera(CameraUpdateFactory.newLatLng(ll));
        mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18.0f));
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

    }

    private void showQuestion() {
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
}
