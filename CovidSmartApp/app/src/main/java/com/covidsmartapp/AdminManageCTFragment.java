package com.covidsmartapp;

import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

public class AdminManageCTFragment extends Fragment {

    private FirebaseFirestore db;

    public AdminManageCTFragment() {
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
        View view = inflater.inflate(R.layout.fragment_admin_manage_c_t, container, false);

        SearchableSpinner CTSpinner = (SearchableSpinner) view.findViewById(R.id.CTSpinner);
        TextView CTEmailTitle = (TextView) view.findViewById(R.id.CTEmailTitle);
        TextView CTNameTitle = (TextView) view.findViewById(R.id.CTNameTitle);
        TextView currentStatusTitle = (TextView) view.findViewById(R.id.currentStatusTitle);
        TextView CTEmailText = (TextView) view.findViewById(R.id.CTEmailText);
        TextView CTNameText = (TextView) view.findViewById(R.id.CTNameText);
        TextView currentStatusText = (TextView) view.findViewById(R.id.currentStatusText);
        Button manageBtn = (Button) view.findViewById(R.id.manageBtn);

        createCTSpinner(CTSpinner);

        CTSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String email = adapterView.getItemAtPosition(i).toString();
                getCTInfo(email, CTEmailTitle, CTNameTitle, currentStatusTitle,
                        CTEmailText, CTNameText, currentStatusText, manageBtn);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    private void createCTSpinner(SearchableSpinner CTSpinner) {
        ArrayList<String> emails = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String type = document.getString("type");
                                if (type.equals("tracer") || type.equals("tracerSuspended")) {
                                    String email = document.getString("email");
                                    emails.add(email);
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner, emails);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            CTSpinner.setAdapter(adapter);
                            CTSpinner.setTitle("Select an account");
                            CTSpinner.setPositiveButton("OK");
                        }
                    }
                });
    }

    private void getCTInfo(String email, TextView CTEmailTitle, TextView CTNameTitle, TextView currentStatusTitle,
                               TextView CTEmailText, TextView CTNameText, TextView currentStatusText, Button manageBtn) {
        ArrayList<String> userIDList = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String CTEmail = document.getString("email");
                                if (CTEmail.equals(email)) {
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
                                                    String CTEmail = document.getString("email");
                                                    String CTName = document.getString("name");
                                                    String type = document.getString("type");

                                                    CTEmailText.setText(CTEmail);
                                                    CTNameText.setText(CTName);
                                                    if (type.equals("tracer")) {
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
                                                                                        .update("type", "tracerSuspended");
                                                                                db.collection("userType")
                                                                                        .document(userIDList.get(0))
                                                                                        .update("userType", "suspended");
                                                                                getCTInfo(email, CTEmailTitle, CTNameTitle, currentStatusTitle,
                                                                                        CTEmailText, CTNameText, currentStatusText, manageBtn);
                                                                            }
                                                                        })
                                                                        .setNegativeButton("Cancel", null)
                                                                        .show();
                                                            }
                                                        });
                                                    } else if (type.equals("tracerSuspended")) {
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
                                                                                        .update("type", "tracer");
                                                                                db.collection("userType")
                                                                                        .document(userIDList.get(0))
                                                                                        .update("userType", "tracer");
                                                                                getCTInfo(email, CTEmailTitle, CTNameTitle, currentStatusTitle,
                                                                                        CTEmailText, CTNameText, currentStatusText, manageBtn);
                                                                            }
                                                                        })
                                                                        .setNegativeButton("Cancel", null)
                                                                        .show();
                                                            }
                                                        });
                                                    }

                                                    CTEmailTitle.setVisibility(View.VISIBLE);
                                                    CTNameTitle.setVisibility(View.VISIBLE);
                                                    currentStatusTitle.setVisibility(View.VISIBLE);
                                                    CTEmailText.setVisibility(View.VISIBLE);
                                                    CTNameText.setVisibility(View.VISIBLE);
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