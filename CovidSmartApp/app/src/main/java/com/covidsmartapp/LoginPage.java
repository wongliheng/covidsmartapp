package com.covidsmartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button registerBtn, loginBtn;
    private TextInputEditText email, pw;
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            startActivity(new Intent(LoginPage.this, LoggedIn.class));
        }

        email = findViewById(R.id.emailEditText);
        pw = findViewById(R.id.passwordEditText);

        registerBtn = findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, RegisterPage.class));
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
                                updateUI(user);
                                startActivity(new Intent(LoginPage.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
    }
}