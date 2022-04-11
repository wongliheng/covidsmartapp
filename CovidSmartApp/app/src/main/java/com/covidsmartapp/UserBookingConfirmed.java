package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class UserBookingConfirmed extends Fragment {

    private String appointmentType;
    private String date;
    private String time;
    private String location;

    public UserBookingConfirmed() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appointmentType = getArguments().getString("appointmentType");
            date = getArguments().getString("date");
            time = getArguments().getString("time");
            location = getArguments().getString("location");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_booking_confirmed, container, false);

        TextView appointmentTypeText = (TextView) view.findViewById(R.id.appointmentTypeText);
        TextView dateTimeText = (TextView) view.findViewById(R.id.dateTimeText);
        TextView locationText = (TextView) view.findViewById(R.id.locationText);
        Button backBtn = (Button) view.findViewById(R.id.backBtn);

        appointmentTypeText.setText(appointmentType);
        String [] dateArray = date.split(" ");
        String dateWithoutYear = dateArray[0] + " " + dateArray[1];
        dateTimeText.setText(dateWithoutYear + ", " + time);
        locationText.setText("@ " + location);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserBookingFragment bookFrag = new UserBookingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, fragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), bookFrag, "bookFrag")
                        .commit();
            }
        });

        return view;

    }
}