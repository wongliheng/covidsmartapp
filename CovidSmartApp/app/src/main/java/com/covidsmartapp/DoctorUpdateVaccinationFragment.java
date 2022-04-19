package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DoctorUpdateVaccinationFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private VaccinationAdapter adapter;
    private String doctorEmail;

    public DoctorUpdateVaccinationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_doctor_update_vaccination, container, false);

        SearchableSpinner patientSpinner = (SearchableSpinner) view.findViewById(R.id.patientSpinner);
        TextView currentStatus = (TextView) view.findViewById(R.id.currentStatus);
        SearchableSpinner statusSpinner = (SearchableSpinner) view.findViewById(R.id.statusSpinner);
        Button updateBtn = (Button) view.findViewById(R.id.updateBtn);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noVaccination = (TextView) view.findViewById(R.id.noVaccination);

        createPatientSpinner(patientSpinner);

        SpinnerCreation spinnerCreation = new SpinnerCreation(getActivity());
        spinnerCreation.createStatusSpinner(statusSpinner);

        patientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String patientEmail = adapterView.getItemAtPosition(i).toString();
                getPatientInfo(patientEmail, currentStatus, recyclerView, noVaccination);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateBtn.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateBtn.setEnabled(false);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patientEmail = patientSpinner.getSelectedItem().toString();
                String status = statusSpinner.getSelectedItem().toString();
                updateVaccination(status, patientEmail);
                currentStatus.setText("Current Status: " + status);
                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
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

    private void getPatientInfo(String email, TextView currentStatus, RecyclerView recyclerView,
                                TextView noVaccination) {
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
                    db.collection("info")
                            .document(userIDList.get(0))
                            .collection("vaccination")
                            .document("vaccinationStatus")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String status = document.getString("vaccinationStatus");
                                            currentStatus.setText("Current Status: " + status);
                                        } else {
                                            currentStatus.setText("Current Status: Unvaccinated");
                                            Log.d("DEBUG", "No such document");
                                        }
                                    } else {
                                        Log.d("DEBUG", "get failed with ", task.getException());
                                    }
                                }
                            });
                    createAppointmentRecycler(userIDList.get(0), recyclerView, noVaccination);
                }
            }
        });
    }

    private void createAppointmentRecycler(String userID, RecyclerView recyclerView, TextView noVaccination) {
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

        Query query = ref.whereEqualTo("appointmentType", "COVID-19 Vaccination")
                .whereLessThan("dateTime", dateTimeLong)
                .orderBy("dateTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<AppointmentClass> options = new FirestoreRecyclerOptions.Builder<AppointmentClass>()
                .setQuery(query, AppointmentClass.class)
                .build();

        adapter = new VaccinationAdapter(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noVaccination.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noVaccination.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void updateVaccination(String status, String email) {
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
                    DocumentReference vacRef = db.collection("info")
                            .document(userIDList.get(0))
                            .collection("vaccination")
                            .document("vaccinationStatus");
                   vacRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    vacRef.update("vaccinationStatus", status);
                                    vacRef.update("updatedBy", doctorEmail);
                                } else {
                                    Map<String, Object> vacStatus = new HashMap<>();
                                    vacStatus.put("vaccinationStatus", status);
                                    vacStatus.put("updatedBy", doctorEmail);
                                    vacRef.set(vacStatus);
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

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}