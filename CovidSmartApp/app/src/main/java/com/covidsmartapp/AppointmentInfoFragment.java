package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AppointmentInfoFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;
    private AppointmentAdapter adapter;

    public AppointmentInfoFragment() {
        // Required empty public constructor
    }

    public static AppointmentInfoFragment newInstance() {
        AppointmentInfoFragment fragment = new AppointmentInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_appointment_info, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noBookings = (TextView) view.findViewById(R.id.noBookings);
//        Button bookBtn = (Button) view.findViewById(R.id.bookBtn);

//        bookBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppointmentFragment appointmentFrag = new AppointmentFragment();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(((ViewGroup)getView().getParent()).getId(), appointmentFrag, "appointmentFrag")
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

        createAppointmentRecycler(recyclerView, noBookings);

        return view;
    }

    private void createAppointmentRecycler(RecyclerView recyclerView, TextView noBookings) {
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

//        LocalDateTime dateTimeNow = LocalDateTime.now();
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
//        String dateTimeString = dateTimeNow.format(format);
//        Long dateTimeLong = Long.parseLong(dateTimeString);

        Query query = ref.whereGreaterThan("dateTime", dateTimeLong);

        FirestoreRecyclerOptions<AppointmentClass> options = new FirestoreRecyclerOptions.Builder<AppointmentClass>()
                .setQuery(query, AppointmentClass.class)
                .build();

        adapter = new AppointmentAdapter(options, userID, getActivity()) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noBookings.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noBookings.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
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