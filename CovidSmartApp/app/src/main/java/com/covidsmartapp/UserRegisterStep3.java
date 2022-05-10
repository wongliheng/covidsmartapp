package com.covidsmartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterStep3 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String email, pw;

    private TextInputEditText fName, lName, phone;
    private Button completeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_step3);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            pw = extras.getString("pw");
        }

        fName = findViewById(R.id.fNameEditText);
        lName = findViewById(R.id.lNameEditText);
        phone = findViewById(R.id.phoneNumEditText);
        completeBtn = findViewById(R.id.completeBtn);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    public void registerUser() {
        String fNameString = fName.getText().toString().trim();
        String lNameString = lName.getText().toString().trim();
        String phoneNumString = phone.getText().toString().trim();

        boolean verified = false;

        if (fNameString.isEmpty()) {
            fName.setError("Please enter your first name");
            fName.requestFocus();
        } else if (lNameString.isEmpty()) {
            lName.setError("Please enter your last name");
            lName.requestFocus();
        } else if (phoneNumString.isEmpty()) {
            phone.setError("Please enter your phone number");
            phone.requestFocus();
        } else if (phoneNumString.length() < 8) {
            phone.setError("Phone Number needs to be at least 8 digits");
            phone.requestFocus();
        }
        else {
            verified = true;
        }

        if (verified) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<String, Object> userObject = new HashMap<>();
            userObject.put("userID", userID);
            userObject.put("fName", fNameString);
            userObject.put("lName", lNameString);
            userObject.put("email", email);
            userObject.put("phoneNum", phoneNumString);
            userObject.put("type", "user");

            db.collection("users")
                    .document(userID)
                    .set(userObject)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Map<String, Object> userTypeObject = new HashMap<>();
                            userTypeObject.put("userType", "user");
                            db.collection("userType")
                                    .document(userID)
                                    .set(userTypeObject)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(UserRegisterStep3.this, "Account successfully created", Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                            finish();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserRegisterStep3.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

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
                                UserRegisterStep3.this.finish();
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}