package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;

public class DoctorUpdateTestsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UpdateTestAdapter adapter;
    private String doctorEmail;

    public DoctorUpdateTestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorEmail = getArguments().getString("email");
        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_update_tests, container, false);

        SearchableSpinner patientSpinner = (SearchableSpinner) view.findViewById(R.id.patientSpinner);
        TextView nameTitle = (TextView) view.findViewById(R.id.nameTitle);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noTests = (TextView) view.findViewById(R.id.noTests);

        createPatientSpinner(patientSpinner);

        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String patientEmail = adapterView.getItemAtPosition(i).toString();
                getPatientInfo(patientEmail, nameText, recyclerView, noTests);
                nameTitle.setVisibility(View.VISIBLE);
                nameText.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void getPatientInfo(String email, TextView nameText, RecyclerView recyclerView, TextView noTests) {
        ArrayList<String> userIDList = new ArrayList<>();
        db.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userEmail = document.getString("email");
                        if (userEmail.equals(email)) {
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
                                            String fName = document.getString("fName");
                                            String lName = document.getString("lName");
                                            String name = fName + " " + lName;
                                            nameText.setText(name);
                                        }
                                    }
                                }
                            });
                    createAppointmentRecycler(userIDList.get(0), recyclerView, noTests);
                }
            }
        });
    }

    private void createAppointmentRecycler(String userID, RecyclerView recyclerView, TextView noTests) {
        CollectionReference ref = db.collection("info")
                .document(userID)
                .collection("appointments");

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        String monthString = "";
        String dayString = "";
        String hourString = "";
        String minuteString = "";

        // Because month is 0-11
        month++;

        if (month < 10)
            monthString = "0" + month;
        else
            monthString = String.valueOf(month);

        if (day < 10)
            dayString = "0" + day;
        else
            dayString = String.valueOf(day);

        if (hour < 10)
            hourString = "0" + hour;
        else
            hourString = String.valueOf(hour);

        if (minutes < 10)
            minuteString = "0" + minutes;
        else
            minuteString = String.valueOf(minutes);

        String dateTime = year + monthString + dayString + hourString + minuteString;
        long dateTimeLong = Long.parseLong(dateTime);

        Query query = ref.whereEqualTo("appointmentType", "COVID-19 Test")
                .whereLessThan("dateTime", dateTimeLong)
                .orderBy("dateTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<AppointmentClass> options = new FirestoreRecyclerOptions.Builder<AppointmentClass>()
                .setQuery(query, AppointmentClass.class)
                .build();

        adapter = new UpdateTestAdapter(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noTests.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noTests.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void createPatientSpinner(SearchableSpinner patientSpinner) {
        ArrayList<String> emails = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String type = document.getString("type");
                                if (type.equals("user")) {
                                    String email = document.getString("email");
                                    emails.add(email);
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner, emails);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            patientSpinner.setAdapter(adapter);
                            patientSpinner.setTitle("Select a patient");
                            patientSpinner.setPositiveButton("OK");
                        }
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}