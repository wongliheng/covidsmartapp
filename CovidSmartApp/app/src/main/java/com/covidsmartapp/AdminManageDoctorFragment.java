package com.covidsmartapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdminManageDoctorFragment extends Fragment {

    private FirebaseFirestore db;

    public AdminManageDoctorFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_manage_doctor, container, false);

        SearchableSpinner doctorSpinner = (SearchableSpinner) view.findViewById(R.id.doctorSpinner);
        TextView doctorEmailTitle = (TextView) view.findViewById(R.id.doctorEmailTitle);
        TextView doctorNameTitle = (TextView) view.findViewById(R.id.doctorNameTitle);
        TextView currentStatusTitle = (TextView) view.findViewById(R.id.currentStatusTitle);
        TextView doctorEmailText = (TextView) view.findViewById(R.id.doctorEmailText);
        TextView doctorNameText = (TextView) view.findViewById(R.id.doctorNameText);
        TextView currentStatusText = (TextView) view.findViewById(R.id.currentStatusText);
        Button manageBtn = (Button) view.findViewById(R.id.manageBtn);

        createDoctorSpinner(doctorSpinner);

        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String email = adapterView.getItemAtPosition(i).toString();
                getDoctorInfo(email, doctorEmailTitle, doctorNameTitle, currentStatusTitle,
                        doctorEmailText, doctorNameText, currentStatusText, manageBtn);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void createDoctorSpinner(SearchableSpinner doctorSpinner) {
        ArrayList<String> emails = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String type = document.getString("type");
                        if (type.equals("doctor") || type.equals("doctorSuspended")) {
                            String email = document.getString("email");
                            emails.add(email);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner, emails);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    doctorSpinner.setAdapter(adapter);
                    doctorSpinner.setTitle("Select an account");
                    doctorSpinner.setPositiveButton("OK");
                }
            }
        });
    }

    private void getDoctorInfo(String email, TextView doctorEmailTitle, TextView doctorNameTitle, TextView currentStatusTitle,
                               TextView doctorEmailText, TextView doctorNameText, TextView currentStatusText, Button manageBtn) {
        ArrayList<String> userIDList = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String doctorEmail = document.getString("email");
                        if (doctorEmail.equals(email)) {
                            String userID = document.getString("userID");
                            userIDList.add(userID);
                        }
                    }
                    db.collection("users")
                            .document(userIDList.get(0))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String doctorEmail = document.getString("email");
                                            String doctorName = document.getString("name");
                                            String type = document.getString("type");

                                            doctorEmailText.setText(doctorEmail);
                                            doctorNameText.setText(doctorName);
                                            if (type.equals("doctor")) {
                                                currentStatusText.setText("Active Account");
                                                manageBtn.setText("Suspend Account");
                                                manageBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        new AlertDialog.Builder(getActivity())
                                                                .setTitle("Suspension Confirmation")
                                                                .setMessage("Are you sure you want to suspend this account?")
                                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        db.collection("users")
                                                                                .document(userIDList.get(0))
                                                                                .update("type", "doctorSuspended");
                                                                        db.collection("userType")
                                                                                .document(userIDList.get(0))
                                                                                .update("userType", "suspended");
                                                                        getDoctorInfo(email, doctorEmailTitle, doctorNameTitle, currentStatusTitle,
                                                                                doctorEmailText, doctorNameText, currentStatusText, manageBtn);
                                                                    }
                                                                })
                                                                .setNegativeButton("Cancel", null)
                                                                .show();
                                                    }
                                                });
                                            } else if (type.equals("doctorSuspended")) {
                                                currentStatusText.setText("Suspended Account");
                                                manageBtn.setText("Activate Account");
                                                manageBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        new AlertDialog.Builder(getActivity())
                                                                .setTitle("Activation Confirmation")
                                                                .setMessage("Are you sure you want to activate this account?")
                                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        db.collection("users")
                                                                                .document(userIDList.get(0))
                                                                                .update("type", "doctor");
                                                                        db.collection("userType")
                                                                                .document(userIDList.get(0))
                                                                                .update("userType", "doctor");
                                                                        getDoctorInfo(email, doctorEmailTitle, doctorNameTitle, currentStatusTitle,
                                                                                doctorEmailText, doctorNameText, currentStatusText, manageBtn);
                                                                    }
                                                                })
                                                                .setNegativeButton("Cancel", null)
                                                                .show();
                                                    }
                                                });
                                            }

                                            doctorEmailTitle.setVisibility(View.VISIBLE);
                                            doctorNameTitle.setVisibility(View.VISIBLE);
                                            currentStatusTitle.setVisibility(View.VISIBLE);
                                            doctorEmailText.setVisibility(View.VISIBLE);
                                            doctorNameText.setVisibility(View.VISIBLE);
                                            currentStatusText.setVisibility(View.VISIBLE);
                                            manageBtn.setVisibility(View.VISIBLE);

                                        } else {
                                            Log.d("DEBUG", "No such document");
                                        }
                                    } else {
                                        Log.d("DEBUG", "get failed with ", task.getException());
                                    }
                                }
                            });
                }
            }
        });
    }
}