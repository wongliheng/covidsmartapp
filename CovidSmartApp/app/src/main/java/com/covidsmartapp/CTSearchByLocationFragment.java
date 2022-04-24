package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class CTSearchByLocationFragment extends Fragment {

    private FirebaseFirestore db;
    private LocationAdapterForCT adapter;

    public CTSearchByLocationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_c_t_search_by_location, container, false);

        TextInputEditText locationText = (TextInputEditText) view.findViewById(R.id.locationText);
        TextInputEditText dateText = (TextInputEditText) view.findViewById(R.id.dateText);
        TextInputEditText timeText = (TextInputEditText) view.findViewById(R.id.timeText);
        Button searchBtn = (Button) view.findViewById(R.id.searchBtn);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noLocations = (TextView) view.findViewById(R.id.noLocations);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dateText.setText(day + " " + getMonthString(month) + " " + year);
        timeText.setText("00:00");

        DatePickerForCTFragment.DatePickerFragmentListener dateListener = new DatePickerForCTFragment.DatePickerFragmentListener() {
            @Override
            public void onDateSet(int year, int month, int day) {
                dateText.setText(day + " " + getMonthString(month) + " " + year);
            }
        };

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerForCTFragment datePickerFragment = new DatePickerForCTFragment();
                datePickerFragment.setDatePickerListener(dateListener);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        TimePickerFragment.TimePickerFragmentListener timeListener = new TimePickerFragment.TimePickerFragmentListener() {
            @Override
            public void onTimeSet(int hour, int minute) {
                String hourString = String.valueOf(hour);
                String minuteString = String.valueOf(minute);

                if (hour < 10)
                    hourString = "0" + hour;
                if (minute < 10)
                    minuteString = "0" + minute;

                timeText.setText(hourString + ":" + minuteString);
            }
        };

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setTimePickerListener(timeListener);
                timePickerFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields(locationText)) {
                    String location = locationText.getText().toString().trim();
                    String date = dateText.getText().toString().trim();
                    String [] dateArray = date.split(" ");
                    String monthDigit  = getMonth(dateArray[1]);
                    String dayString = dateArray[0];
                    int day = Integer.parseInt(dateArray[0]);
                    if (day < 10)
                        dayString = "0" + day;

                    String dateString = dateArray[2] + monthDigit + dayString;

                    String time = timeText.getText().toString().trim();
                    String [] timeArray = time.split(":");
                    String timeString =  timeArray[0].concat(timeArray[1]);

                    String dateTimeString = dateString.concat(timeString);
                    long dateTime = Long.parseLong(dateTimeString);

                    createRecycler(location, dateTime, recyclerView, noLocations);
                };
            }
        });


        return view;
    }

    private void createRecycler (String location, long dateTime, RecyclerView recyclerView, TextView noLocations) {
        String dateTimeString = String.valueOf(dateTime);
        String date = dateTimeString.substring(0, 8);
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        String newDate = day + month + year;

        Query query = db.collectionGroup("locations")
                .whereEqualTo("locationName", location)
                .whereEqualTo("checkedOut", true)
                .whereGreaterThanOrEqualTo("dateTime", dateTime)
                .whereEqualTo("checkInDate", newDate);

        FirestoreRecyclerOptions<LocationAfterCheckOutClass> options = new FirestoreRecyclerOptions.Builder<LocationAfterCheckOutClass>()
                .setQuery(query, LocationAfterCheckOutClass.class)
                .build();

        adapter = new LocationAdapterForCT(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noLocations.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noLocations.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private boolean checkFields(TextInputEditText locationText) {
        String location = locationText.getText().toString().trim();

        boolean check = false;

        if (location.isEmpty()) {
            locationText.setError("Please enter a location");
            locationText.requestFocus();
        } else {
            check = true;
        }

        return true;
    }

    private String getMonthString (int month) {
        String monthString = "December";
        if (month == 0)
            monthString = "January";
        else if (month == 1)
            monthString = "February";
        else if (month == 2)
            monthString = "March";
        else if (month == 3)
            monthString = "April";
        else if (month == 4)
            monthString = "May";
        else if (month == 5)
            monthString = "June";
        else if (month == 6)
            monthString = "July";
        else if (month == 7)
            monthString = "August";
        else if (month == 8)
            monthString = "September";
        else if (month == 9)
            monthString = "October";
        else if (month == 10)
            monthString = "November";

        return monthString;
    }

    private String getMonth (String monthString) {
        String month = "12";
        if (monthString.equals("January"))
            month = "01";
        else if (monthString.equals("February"))
            month = "02";
        else if (monthString.equals("March"))
            month = "03";
        else if (monthString.equals("April"))
            month = "04";
        else if (monthString.equals("May"))
            month = "05";
        else if (monthString.equals("June"))
            month = "06";
        else if (monthString.equals("July"))
            month = "07";
        else if (monthString.equals("August"))
            month = "08";
        else if (monthString.equals("September"))
            month = "09";
        else if (monthString.equals("October"))
            month = "10";
        else if (monthString.equals("November"))
            month = "11";

        return month;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}