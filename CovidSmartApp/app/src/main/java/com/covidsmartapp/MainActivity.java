package com.covidsmartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginPage.class));
            finish();
        }
        else {
            userID = user.getUid();
            db.collection("userType")
                    .document(userID)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String userType = document.getString("userType");
                                    if (userType.equals("user")) {
                                        startActivity(new Intent(MainActivity.this, UserLoggedIn.class));
                                        finish();
                                    } else if (userType.equals("admin")) {
                                        mAuth.signOut();
                                        startActivity(new Intent(MainActivity.this, LoginPage.class));
                                        finish();
                                    } else if (userType.equals("doctor")) {
                                        startActivity(new Intent(MainActivity.this, DoctorHomeActivity.class));
                                        finish();
                                    } else if (userType.equals("tracer")) {
                                        startActivity(new Intent(MainActivity.this, CTHomeActivity.class));
                                        finish();
                                    } else if (userType.equals("suspended")) {
                                        mAuth.signOut();
                                        startActivity(new Intent(MainActivity.this, LoginPage.class));
                                        finish();
                                    }
                                } else {
                                    startActivity(new Intent(MainActivity.this, LoginPage.class));
                                    finish();
                                }
                            } else {
                                Log.d("DEBUG", "get failed with ", task.getException());
                            }
                        }
                    });
        }
    }
}