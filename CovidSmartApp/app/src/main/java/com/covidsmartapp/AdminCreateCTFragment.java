package com.covidsmartapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminCreateCTFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String adminEmail, adminPw;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public AdminCreateCTFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adminEmail = getArguments().getString("email");
            adminPw = getArguments().getString("pw");
        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_create_c_t, container, false);

        TextInputEditText nameEditText = (TextInputEditText) view.findViewById(R.id.nameEditText);
        TextInputEditText emailEditText = (TextInputEditText) view.findViewById(R.id.emailEditText);
        TextInputEditText passwordEditText = (TextInputEditText) view.findViewById(R.id.passwordEditText);
        Button registerBtn = (Button) view.findViewById(R.id.register);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(nameEditText, emailEditText, passwordEditText, progressBar);
            }
        });

        return view;
    }

    private void register(TextInputEditText name, TextInputEditText email, TextInputEditText pw, ProgressBar progressBar) {
        String nameString = name.getText().toString();
        String emailString = email.getText().toString().trim();
        String pwString = pw.getText().toString().trim();

        boolean verified = false;

        if (nameString.isEmpty()) {
            name.setError("Please enter a name");
            name.requestFocus();
        }
        if (emailString.isEmpty()) {
            email.setError("Please enter an email");
            email.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
        }
        else if (pwString.isEmpty()) {
            pw.setError("Please enter a password");
            pw.requestFocus();
        }
        else if (pwString.length() < 8) {
            pw.setError("Password needs to be at least 8 characters");
            pw.requestFocus();
        }
        else if (pwString.length() > 20) {
            pw.setError("Maximum of 20 characters for password");
            pw.requestFocus();
        }
        else if (!checkPassword(pwString)) {
            pw.setError("Password requires at least 1 digit, lowercase and uppercase character");
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
                                user.sendEmailVerification();
                                String userID = user.getUid();

                                Map<String, Object> userObject = new HashMap<>();
                                userObject.put("userID", userID);
                                userObject.put("name", nameString);
                                userObject.put("email", emailString);
                                userObject.put("type", "tracer");

                                db.collection("users")
                                        .document(userID)
                                        .set(userObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Map<String, Object> userTypeObject = new HashMap<>();
                                        userTypeObject.put("userType", "tracer");
                                        db.collection("userType")
                                                .document(userID)
                                                .set(userTypeObject)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        new AlertDialog.Builder(getActivity())
                                                                .setTitle("Registration Complete")
                                                                .setMessage("Have your new contact tracer verify their email and the process is complete.")
                                                                .setCancelable(true)
                                                                .show();
                                                        mAuth.signOut();
                                                        mAuth.signInWithEmailAndPassword(adminEmail, adminPw);
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                });
                                    }
                                });
                            } else {
                                new AlertDialog.Builder(getActivity())
                                        .setMessage(task.getException().getMessage())
                                        .setCancelable(true)
                                        .show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private boolean checkPassword(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}