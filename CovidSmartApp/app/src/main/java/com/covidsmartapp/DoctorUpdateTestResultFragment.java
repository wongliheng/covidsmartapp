package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorUpdateTestResultFragment extends Fragment {

    private FirebaseFirestore db;
    private String location, date, time, userID, documentID;

    public DoctorUpdateTestResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = getArguments().getString("location");
            date = getArguments().getString("date");
            time = getArguments().getString("time");
            userID = getArguments().getString("userID");
            documentID = getArguments().getString("documentID");
        }

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_update_test_result, container, false);

        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView locationText = (TextView) view.findViewById(R.id.locationText);
        TextView dateText = (TextView) view.findViewById(R.id.dateText);
        TextView timeText = (TextView) view.findViewById(R.id.timeText);
        Button positiveBtn = (Button) view.findViewById(R.id.positiveBtn);
        Button negativeBtn = (Button) view.findViewById(R.id.negativeBtn);
        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);

        db.collection("users")
                .document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String fName = document.getString("fName");
                                String lName = document.getString("lName");
                                String name = fName + " " + lName;
                                nameText.setText(name);
                            }
                        }
                    }
                });

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("info")
                        .document(userID)
                        .collection("appointments")
                        .document(documentID)
                        .update("status", "Positive")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Test Result Updated")
                                        .setMessage("Result has been updated as Positive.")
                                        .setCancelable(true)
                                        .show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("info")
                        .document(userID)
                        .collection("appointments")
                        .document(documentID)
                        .update("status", "Negative")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Test Result Updated")
                                        .setMessage("Result has been updated as Negative.")
                                        .setCancelable(true)
                                        .show();
                                getActivity().getSupportFragmentManager().popBackStack();
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

        locationText.setText(location);
        dateText.setText(date);
        timeText.setText(time);



        return view;
    }
}