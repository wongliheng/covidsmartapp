package com.covidsmartapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;

public class LocationHistoryAdapterForCT extends FirestoreRecyclerAdapter<LocationAfterCheckOutClass, LocationHistoryAdapterForCT.LocationHistoryHolder> {

    private String dateString, timeString;

    public LocationHistoryAdapterForCT(@NonNull FirestoreRecyclerOptions<LocationAfterCheckOutClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LocationHistoryHolder holder, int position, @NonNull LocationAfterCheckOutClass model) {
        holder.locationText.setText(model.getLocationName());

        String checkInDate = model.getCheckInDate();
        String checkOutDate = model.getCheckOutDate();

        if (checkInDate.equals(checkOutDate)){
            String day = checkInDate.substring(0, 2);
            String month = checkInDate.substring(2, 4);
            dateString = day + " " + getMonthString(Integer.parseInt(month));

        } else {
            String checkInDay = checkInDate.substring(0, 2);
            String checkInMonth = checkInDate.substring(2, 4);
            String checkOutDay = checkOutDate.substring(0, 2);
            String checkOutMonth = checkOutDate.substring(2, 4);
            dateString = checkInDay + " " + getMonthString(Integer.parseInt(checkInMonth)) + " - " +
                    checkOutDay + " " + getMonthString(Integer.parseInt(checkOutMonth));
        }

        holder.dateText.setText(dateString);

        String checkInTime = model.getCheckInTime();
        String checkOutTime = model.getCheckOutTime();

        String [] checkIn = checkInTime.split(":");
        String [] checkOut = checkOutTime.split(":");

        int checkInHour = Integer.parseInt(checkIn[0]);
        int checkOutHour = Integer.parseInt(checkOut[0]);

        timeString = checkInHour + ":" + checkIn[1] + " - " + checkOutHour + ":" + checkOut[1];

        holder.timeText.setText(timeString);
    }

    @NonNull
    @Override
    public LocationHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_history_layout_ct, parent, false);
        return new LocationHistoryAdapterForCT.LocationHistoryHolder(view);
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

    public class LocationHistoryHolder extends RecyclerView.ViewHolder {

        TextView locationText, dateText, timeText;

        public LocationHistoryHolder(@NonNull View itemView) {
            super(itemView);
            locationText = itemView.findViewById(R.id.locationText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
