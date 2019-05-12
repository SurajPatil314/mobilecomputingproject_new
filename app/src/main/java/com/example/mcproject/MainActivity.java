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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
    private TextView emailSS;
    private  TextView passwordSS;
    public static FirebaseAuth fauthSS;
    public static GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;

    // private DatabaseReference dataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compareValues();
        setContentView(R.layout.activity_main);
        checkPermission();
        passwordSS = (TextView) findViewById(R.id.passwordS);
        emailSS= (TextView) findViewById(R.id.emailS);
        FirebaseApp.initializeApp(this);
        configureSignIn();
        signInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        signInButton.setOnClickListener(this);
        fauthSS= FirebaseAuth.getInstance();
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

    @Override
    public void onClick(View v) {
        signS();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signS() {
        Intent i = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(i, 101);
    }

    public void configureSignIn(){
        // Configure sign-in to request the user's basic profile like name and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("17444640663-mgir102iuk520qrfqt0hf32st6lrumvl.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {                // Google Sign In failed
                Toast.makeText(getApplicationContext(), "Failed2 to login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fauthSS.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fauthSS.getCurrentUser();
                            Intent i = new Intent(getApplicationContext(), create_join_game.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "User Logged in Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Failed1 to login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void compareValues(){
        Map<String, Integer> olympic2012 = new HashMap<String, Integer>();

        olympic2012.put("England", 3);
        olympic2012.put("USA", 1);
        olympic2012.put("China", 2);
        olympic2012.put("Russia", 4);
        olympic2012.put("xyz", 2);

        List<Map.Entry<String, Integer>> list = new LinkedList(olympic2012.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        System.out.println("Hashmap - ");
        for (String name: result.keySet()){
            String key = name.toString();
            String value = result.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    public void leader(View view) {
        Intent i = new Intent(getApplicationContext(), leaderboard_afterlastquestion.class);
        startActivity(i);
    }
}
