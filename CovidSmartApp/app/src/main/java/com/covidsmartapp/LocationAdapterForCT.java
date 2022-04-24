package com.covidsmartapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LocationAdapterForCT extends FirestoreRecyclerAdapter<LocationAfterCheckOutClass, LocationAdapterForCT.LocationHolder> {

    public LocationAdapterForCT(@NonNull FirestoreRecyclerOptions<LocationAfterCheckOutClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LocationHolder holder, int position, @NonNull LocationAfterCheckOutClass model) {
        holder.userIDText.setText(model.getUserID());
        holder.locationText.setText(model.getLocationName());

        String checkInDate = model.getCheckInDate();
        String checkOutDate = model.getCheckOutDate();

        if (checkInDate.equals(checkOutDate)){
            String day = checkInDate.substring(0, 2);
            String month = checkInDate.substring(2, 4);
            holder.dateText.setText(day + " " + getMonthString(Integer.parseInt(month)));
        } else {
            String checkInDay = checkInDate.substring(0, 2);
            String checkInMonth = checkInDate.substring(2, 4);
            String checkOutDay = checkOutDate.substring(0, 2);
            String checkOutMonth = checkOutDate.substring(2, 4);

            holder.dateText.setText(checkInDay + " " + getMonthString(Integer.parseInt(checkInMonth)) + " - " +
                    checkOutDay + " " + getMonthString(Integer.parseInt(checkOutMonth)));
        }

        String checkInTime = model.getCheckInTime();
        String checkOutTime = model.getCheckOutTime();

        String [] checkIn = checkInTime.split(":");
        String [] checkOut = checkOutTime.split(":");

        int checkInHour = Integer.parseInt(checkIn[0]);
        int checkOutHour = Integer.parseInt(checkOut[0]);

        holder.timeText.setText(checkInHour + ":" + checkIn[1] + " - " + checkOutHour + ":" + checkOut[1]);
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_layout_ct, parent, false);
        return new LocationAdapterForCT.LocationHolder(view);
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

    public class LocationHolder extends RecyclerView.ViewHolder {

        TextView userIDText, locationText, dateText, timeText;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            userIDText = itemView.findViewById(R.id.userIDText);
            locationText = itemView.findViewById(R.id.locationText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
