package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class UserTestResultFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;
    private TestResultAdapter adapter;

    public UserTestResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_test_result, container, false);

        RecyclerView testResultRecycler = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noResults = (TextView) view.findViewById(R.id.noResults);

        createTestResultRecycler(testResultRecycler, noResults);

        return view;
    }

    private void createTestResultRecycler(RecyclerView testResultRecycler, TextView noResults) {
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

//        LocalDateTime dateTimeNow = LocalDateTime.now();
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
//        String dateTimeString = dateTimeNow.format(format);
//        Long dateTimeLong = Long.parseLong(dateTimeString);

        CollectionReference ref = db.collection("info")
                .document(userID)
                .collection("appointments");

        Query query = ref.whereEqualTo("appointmentType", "COVID-19 Test")
                .whereLessThan("dateTime", dateTimeLong)
                .orderBy("dateTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<AppointmentClass> options = new FirestoreRecyclerOptions.Builder<AppointmentClass>()
                .setQuery(query, AppointmentClass.class)
                .build();

        adapter = new TestResultAdapter(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    testResultRecycler.setVisibility(View.INVISIBLE);
                    noResults.setVisibility(View.VISIBLE);
                }
                else {
                    testResultRecycler.setVisibility(View.VISIBLE);
                    noResults.setVisibility(View.GONE);
                }
            }
        };
        testResultRecycler.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        testResultRecycler.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}