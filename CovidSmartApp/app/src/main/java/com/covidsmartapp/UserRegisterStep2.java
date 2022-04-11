package com.covidsmartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRegisterStep2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Button verifyBtn, resendBtn;
    private TextView resendText;
    private String email, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_step2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            pw = extras.getString("pw");
        }

        verifyBtn = findViewById(R.id.verifyBtn);
        resendBtn = findViewById(R.id.resendBtn);
        resendText = findViewById(R.id.resendText);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.sendEmailVerification();
                resendBtn.setEnabled(false);
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long l) {
                        int seconds = (int) (l / 1000) % 60;
                        resendText.setText("You may request again in " + seconds + " seconds.");
                    }

                    @Override
                    public void onFinish() {
                        resendBtn.setEnabled(true);
                        resendText.setText("");
                    }
                }.start();
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user = mAuth.getCurrentUser();

                        if (user.isEmailVerified()){
                            Intent i = new Intent(UserRegisterStep2.this, UserRegisterStep3.class);
                            i.putExtra("email", email);
                            i.putExtra("pw", pw);
                            startActivity(i);
                            finish();
                        } else {
                            new AlertDialog.Builder(UserRegisterStep2.this)
                                    .setMessage("Email has not been verified, please try again.")
                                    .setCancelable(true)
                                    .show();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit the registration process?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AuthCredential credential = EmailAuthProvider.getCredential(email, pw);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete();
                                UserRegisterStep2.this.finish();
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}