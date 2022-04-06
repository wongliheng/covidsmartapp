package com.covidsmartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterStep1 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button register, backToLogIn;
    private TextInputEditText email, pw;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step1);

        mAuth = FirebaseAuth.getInstance();

        register = findViewById(R.id.register);
        email = findViewById(R.id.emailEditText);
        pw = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.progressBar);
        backToLogIn = findViewById(R.id.backToLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        backToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registerUser() {
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
        else if (pwString.length() < 6) {
            pw.setError("Password needs to be at least 6 characters");
            pw.requestFocus();
        }
        else {
            verified = true;
        }

        if (verified) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(emailString, pwString)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                // Send verification email
                                user.sendEmailVerification();

                                Intent i = new Intent(RegisterStep1.this, RegisterStep2.class);
                                i.putExtra("email", emailString);
                                i.putExtra("pw", pwString);
                                startActivity(i);
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    new AlertDialog.Builder(RegisterStep1.this)
                                            .setTitle(task.getException().getMessage())
                                            .setMessage("If you have not fully completed registration, please log in to do so.")
                                            .setCancelable(true)
                                            .show();
                                } else {
                                    new AlertDialog.Builder(RegisterStep1.this)
                                            .setMessage(task.getException().getMessage())
                                            .setCancelable(true)
                                            .show();
                                }
                            }
                        }
                    });
        }
    }
}