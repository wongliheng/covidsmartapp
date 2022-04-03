package com.covidsmartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button register, backToLogIn;
    private TextInputEditText fName, lName, phone, email, pw;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();

        register = findViewById(R.id.register);
        fName = findViewById(R.id.fNameEditText);
        lName = findViewById(R.id.lNameEditText);
        phone = findViewById(R.id.phoneNumEditText);
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
        String fNameString = fName.getText().toString().trim();
        String lNameString = lName.getText().toString().trim();
        String phoneNumString = phone.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String pwString = pw.getText().toString().trim();

        boolean verified = false;

        if (fNameString.isEmpty()) {
            fName.setError("Please enter your first name");
            fName.requestFocus();
        }
        else if (lNameString.isEmpty()) {
            lName.setError("Please enter your last name");
            lName.requestFocus();
        }
        else if (phoneNumString.isEmpty()) {
            phone.setError("Please enter your phone number");
            phone.requestFocus();
        }
        else if (emailString.isEmpty()) {
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
            int phoneNum = Integer.parseInt(phoneNumString);
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(emailString, pwString)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                Map<String, Object> user = new HashMap<>();
                                user.put("userID", userID);
                                user.put("fName", fNameString);
                                user.put("lName", lNameString);
                                user.put("email", emailString);
//                                No need to store user password
//                                user.put("pw", pwString);
                                user.put("phoneNum", phoneNum);

                                db.collection("users").document(userID)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterPage.this, "Account successfully created", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }
}