package com.covidsmartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoggedIn extends AppCompatActivity {

    private Button signout, datatest;
    private FirebaseAuth mAuth;

    DatabaseReference reference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mAuth = FirebaseAuth.getInstance();


        signout = findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(LoggedIn.this, LoginPage.class));
                finish();
            }
        });

        datatest = findViewById(R.id.datatest);
        datatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testInsert();
            }
        });
    }

    private void testInsert() {
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("booking");

        String userID = mAuth.getCurrentUser().getUid();
        reference.child(userID).setValue(new BookingClass(23, 2, 2004));
    }
}