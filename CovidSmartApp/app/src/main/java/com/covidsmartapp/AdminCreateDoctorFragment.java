package com.covidsmartapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminCreateDoctorFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String adminEmail, adminPw;

    public AdminCreateDoctorFragment() {
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
        View view = inflater.inflate(R.layout.fragment_admin_create_doctor, container, false);

        Button btn = (Button) view.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword("lhw436@uowmail.edu.au", "123456")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    String userID = user.getUid();

                                    Map<String, Object> userObject = new HashMap<>();
                                    userObject.put("userID", userID);
                                    userObject.put("userType", "doctor");
                                    
                                    mAuth.signOut();
                                    mAuth.signInWithEmailAndPassword(adminEmail, adminPw);

                                    db.collection("users")
                                            .document(userID)
                                            .set(userObject)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
//                                                    mAuth.signOut();
//                                                    mAuth.signInWithEmailAndPassword("covidsmartappfyp@gmail.com", "adminAcc1@");

                                                }
                                            });
                                }
                                else {
                                    new AlertDialog.Builder(getActivity())
                                            .setMessage(task.getException().getMessage())
                                            .setCancelable(true)
                                            .show();
                                }
                            }
                        });
            }
        });

        return view;
    }
}