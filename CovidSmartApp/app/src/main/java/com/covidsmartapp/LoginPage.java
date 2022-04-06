package com.covidsmartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String userID;

    private Button registerBtn, loginBtn;
    private TextInputEditText email, pw;
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null)
            mAuth.signOut();


        email = findViewById(R.id.emailEditText);
        pw = findViewById(R.id.passwordEditText);

        registerBtn = findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, RegisterStep1.class));
            }
        });

        loginBtn = findViewById(R.id.logIn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });
    }

    private void logIn() {
        String emailString = email.getText().toString().trim();
        String pwString = pw.getText().toString().trim();

        boolean verified = false;

        if (emailString.isEmpty()) {
            email.setError("Please enter your email");
            email.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
        }
        else if (pwString.isEmpty()) {
            pw.setError("Please enter your password");
            pw.requestFocus();
        }
        else {
            verified = true;
        }
        if (verified) {
            mAuth.signInWithEmailAndPassword(emailString, pwString)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                userID = user.getUid();
                                user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // Check email verified
                                        if (user.isEmailVerified()){
                                            // Check whether user has filled up details
                                            db.collection("users")
                                                    .document(userID)
                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            startActivity(new Intent(LoginPage.this, MainActivity.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(LoginPage.this, "Please complete your registration", Toast.LENGTH_SHORT).show();
                                                            Intent i = new Intent(LoginPage.this, RegisterStep3.class);
                                                            i.putExtra("email", emailString);
                                                            i.putExtra("pw", pwString);
                                                            startActivity(i);
                                                        }
                                                    } else {
                                                        Log.d("DEBUG", "get failed with ", task.getException());
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(LoginPage.this, "Please complete your registration", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(LoginPage.this, RegisterStep2.class);
                                            i.putExtra("email", emailString);
                                            i.putExtra("pw", pwString);
                                            startActivity(i);
                                        }
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                email.setError("There is no account registered with this email");
                                email.requestFocus();
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                pw.setError("Incorrect password");
                                pw.requestFocus();
                            } else
                                Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
            });
        }
    }
}