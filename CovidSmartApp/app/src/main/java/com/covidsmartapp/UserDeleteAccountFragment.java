package com.covidsmartapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDeleteAccountFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;

    public UserDeleteAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_delete_account, container, false);

        TextView pwText = (TextView) view.findViewById(R.id.passwordEditText);
        Button deleteBtn = (Button) view.findViewById(R.id.deleteBtn);
        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users")
                        .document(userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String email = document.getString("email");
                                        deleteUser(email, pwText);
                                    }
                                }
                            }
                        });
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void deleteUser(String email, TextView pwText) {
        String pwString = pwText.getText().toString().trim();

        boolean check = false;
        if (pwString.isEmpty()) {
            pwText.setError("Please enter your password");
            pwText.requestFocus();
        } else {
            check = true;
        }

        if (check) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Deletion Confirmation")
                    .setMessage("Are you sure you want to delete your account?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AuthCredential credential = EmailAuthProvider.getCredential(email, pwString);
                            user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Delete data one collection at a time
                                    db.collection("info")
                                            .document(userID)
                                            .collection("appointments")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            document.getReference().delete();
                                                        }
                                                    }
                                                    db.collection("info")
                                                            .document(userID)
                                                            .collection("locations")
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            document.getReference().delete();
                                                                        }
                                                                    }
                                                                    db.collection("info")
                                                                            .document(userID)
                                                                            .collection("vaccination")
                                                                            .get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                            document.getReference().delete();
                                                                                        }
                                                                                    }
                                                                                    db.collection("info")
                                                                                            .document(userID)
                                                                                            .collection("messages")
                                                                                            .get()
                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                            document.getReference().delete();
                                                                                                        }
                                                                                                    }
                                                                                                    db.collection("info")
                                                                                                            .document(userID)
                                                                                                            .delete()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    db.collection("userType")
                                                                                                                            .document(userID)
                                                                                                                            .delete()
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                    db.collection("users")
                                                                                                                                            .document(userID)
                                                                                                                                            .delete()
                                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                @Override
                                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                    user.delete();
                                                                                                                                                    getActivity().finish();
                                                                                                                                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            });

                                                                                                }
                                                                                            });
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        pwText.setError("Incorrect password");
                                        pwText.requestFocus();
                                    } else
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}