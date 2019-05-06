package com.example.mcproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUsers extends AppCompatActivity {
    private TextView password;
    private  TextView email;
    private FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_users);
        password = (TextView) findViewById(R.id.passwordR);
        email= (TextView) findViewById(R.id.emailR);
       FirebaseApp.initializeApp(this);
        fauth=FirebaseAuth.getInstance();
    }

    public void reigsterR(View view) {

        String passs= password.getText().toString().trim();
        String emails= email.getText().toString().trim();
        System.out.println("in registerR button::"+emails+"::password::"+passs);

        if(passs.isEmpty()||emails.isEmpty())
        {
            Toast.makeText(this, "enter valid information", Toast.LENGTH_LONG).show();
        }
        else{

            System.out.println("in rsuccessful ::"+emails+"::password::"+passs);
            fauth.createUserWithEmailAndPassword(emails,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(RegisterUsers.this, "Successfully Registered. Now sign in", Toast.LENGTH_LONG).show();
                        // finish();
                        //startActivity(new Intent(register.this, MainActivity.class));
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                      //  i.putExtra("Username",names);
                        startActivity(i);
                    }else{
                        Toast.makeText(RegisterUsers.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public void signinR(View view) {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }
}
