package com.example.chattingapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class StartUp extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        FirebaseUser currentuser = mAuth.getCurrentUser();
        Intent intent;
        if(currentuser!=null)
         {
             intent = new Intent(StartUp.this, MainActivity.class);
             finish();
         }
         else {
            intent = new Intent(StartUp.this, SignUp.class);
        }
        startActivity(intent);
        super.onStart();
    }
}