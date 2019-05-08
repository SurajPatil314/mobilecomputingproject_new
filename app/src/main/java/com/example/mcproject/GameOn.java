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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GameOn extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    SupportMapFragment mapFragment;
    GoogleMap mmap;
    SupportMapFragment mapFrag;
    LocationManager locationManager;
    Double longitudedata;
    Double latitudedata;

    private TextView question;
    static  int questionnumber = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameon);
        getLocation();
        question = (TextView)findViewById(R.id.Question);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        startgame();
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

    private void startgame() {

    }
}
