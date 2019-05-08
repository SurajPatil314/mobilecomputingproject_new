package com.example.mcproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    private TextView emailSS;
    private  TextView passwordSS;
    private FirebaseAuth fauthSS;

    // private DatabaseReference dataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        passwordSS = (TextView) findViewById(R.id.passwordS);
        emailSS= (TextView) findViewById(R.id.emailS);
        FirebaseApp.initializeApp(this);
    }

    public void signinS(View view) {
        String passss= "pratik"; //passwordSS.getText().toString().trim();
        String emailss= "pracshi@gmail.com";//emailSS.getText().toString().trim();

        fauthSS= FirebaseAuth.getInstance();
        fauthSS.signInWithEmailAndPassword(emailss,passss).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid(); //getting current user ID



                    Toast.makeText(MainActivity.this, "Successfully signed in", Toast.LENGTH_LONG).show();


                    Log.i("uuid::",currentuser);


                    Intent i = new Intent(getBaseContext(), create_join_game.class);

                    startActivity(i);

                } else {
                    Toast.makeText(MainActivity.this, "sign in Failed. Invalid credentials?", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public void registerS(View view) {
        Intent i = new Intent(getBaseContext(), RegisterUsers.class);
        startActivity(i);
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }
}
