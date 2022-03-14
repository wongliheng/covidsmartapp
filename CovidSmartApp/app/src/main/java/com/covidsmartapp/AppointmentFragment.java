package com.covidsmartapp;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;

    private boolean appointmentCheck = false;
    private boolean dateCheck = false;
    private boolean timeCheck = false;
    private boolean locationCheck = false;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        EditText dateEditText = (EditText) view.findViewById(R.id.dateEditText);

        TextView appointmentText = (TextView) view.findViewById(R.id.appointmentText);
        TextView dateText = (TextView) view.findViewById(R.id.dateText);
        TextView timeText = (TextView) view.findViewById(R.id.timeText);
        TextView locationText = (TextView) view.findViewById(R.id.locationText);

        Spinner appointmentSpinner = (Spinner) view.findViewById(R.id.appointmentSpinner);
        Spinner timeSpinner = (Spinner) view.findViewById(R.id.timeSpinner) ;
        Spinner locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        Button bookBtn = (Button) view.findViewById(R.id.bookBtn);

        SpinnerCreation createSpinner = new SpinnerCreation(getActivity());
        createSpinner.createAppointmentSpinner(appointmentSpinner);
        createSpinner.setUpTimeSpinner(timeSpinner);
        createSpinner.createLocationSpinner(locationSpinner);

//        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.locations, R.layout.spinner);
//        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        locationSpinner.setAdapter(locationAdapter);

        DatePickerFragment.DatePickerFragmentListener listener = new DatePickerFragment.DatePickerFragmentListener() {
            @Override
            public void onDateSet(int year, int month, int day) {
                dateEditText.setText(day + " " + getMonthString(month) + " " + year);
                timeText.setText("Select a time");
                createSpinner.createTimeSpinner(timeSpinner, day);
            }
        };

        dateEditText.setText("Select a date");
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setDatePickerListener(listener);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String appointmentType = appointmentSpinner.getSelectedItem().toString();
                String date = dateEditText.getText().toString();
                String time = timeSpinner.getSelectedItem().toString();
                String location = locationSpinner.getSelectedItem().toString();

                if (!appointmentType.equals("Select a type"))
                    appointmentCheck = true;
                if (!date.equals("Select a date"))
                    dateCheck = true;
                if (!time.equals("Select a time") && !time.equals("Select a date"))
                    timeCheck = true;
                if (!location.equals("Select a location"))
                    locationCheck = true;

                if (!appointmentCheck){
                    appointmentText.setText("Please select a type");
                    appointmentText.setTextColor(getResources().getColor(R.color.red_400));
                } else {
                    appointmentText.setText("Appointment Type:");
                    appointmentText.setTextColor(getResources().getColor(R.color.teal_700));
                }
                if (!dateCheck){
                    dateText.setText("Please select a date");
                    dateText.setTextColor(getResources().getColor(R.color.red_400));
                } else {
                    dateText.setText("Date:");
                    dateText.setTextColor(getResources().getColor(R.color.teal_700));
                }
                if (!timeCheck){
                    timeText.setText("Please select a time");
                    timeText.setTextColor(getResources().getColor(R.color.red_400));
                } else {
                    timeText.setText("Time:");
                    timeText.setTextColor(getResources().getColor(R.color.teal_700));
                }
                if (!locationCheck){
                    locationText.setText("Please select a location");
                    locationText.setTextColor(getResources().getColor(R.color.red_400));
                } else {
                    locationText.setText("Location:");
                    locationText.setTextColor(getResources().getColor(R.color.teal_700));
                }

                if (appointmentCheck && dateCheck && timeCheck && locationCheck) {
                    String [] dateArray = date.split(" ");
                    int month  = getMonth(dateArray[1]);
                    String newDate = dateArray[0] + month + dateArray[2];

                    Map<String, Object> appointment = new HashMap<>();
                    appointment.put("appointmentType", appointmentType);
                    appointment.put("date", newDate);
                    appointment.put("time", time);
                    appointment.put("location", location);

                    db.collection("info")
                            .document(userID)
                            .collection("appointments")
                            .document(newDate + " " + time)
                            .set(appointment);
                }
            }
        });

        return view;
    }

    private String getMonthString (int month) {
        String monthString = "December";
        if (month == 1)
            monthString = "January";
        else if (month == 2)
            monthString = "February";
        else if (month == 3)
            monthString = "March";
        else if (month == 4)
            monthString = "April";
        else if (month == 5)
            monthString = "May";
        else if (month == 6)
            monthString = "June";
        else if (month == 7)
            monthString = "July";
        else if (month == 8)
            monthString = "August";
        else if (month == 9)
            monthString = "September";
        else if (month == 10)
            monthString = "October";
        else if (month == 11)
            monthString = "November";

        return monthString;
    }

    private int getMonth (String monthString) {
        int month = 12;
        if (monthString == "January")
            month = 1;
        else if (monthString == "February")
            month = 2;
        else if (monthString == "March")
            month = 3;
        else if (monthString == "April")
            month = 4;
        else if (monthString == "May")
            month = 5;
        else if (monthString == "June")
            month = 6;
        else if (monthString == "July")
            month = 7;
        else if (monthString == "August")
            month = 8;
        else if (monthString == "September")
            month = 9;
        else if (monthString == "October")
            month = 10;
        else if (monthString == "November")
            month = 11;

        return month;
    }
}