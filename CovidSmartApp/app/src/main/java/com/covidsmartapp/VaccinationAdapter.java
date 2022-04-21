package com.covidsmartapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class VaccinationAdapter extends FirestoreRecyclerAdapter<AppointmentClass, VaccinationAdapter.VaccinationHolder> {

    public VaccinationAdapter(@NonNull FirestoreRecyclerOptions<AppointmentClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VaccinationHolder holder, int position, @NonNull AppointmentClass model) {
        holder.locationText.setText(model.getLocation());

        String date = model.getDate();
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String dayString = date.substring(6, 8);

        int day = Integer.parseInt(dayString);
        String monthString = getMonthString(Integer.parseInt(month));

        String newDate = day + " " + monthString + " " + year;
        holder.dateText.setText(newDate);

        String time = model.getTime();
        String meridiem = "AM";
        String hours = time.substring(0,2);
        String minutes = time.substring(2,4);
        int hour = Integer.parseInt(hours);
        if (hour >= 12) {
            if (hour > 12){
                hour = hour - 12;
            }
            meridiem = "PM";
        }
        String newTime = hour + ":" + minutes + " " + meridiem;
        holder.timeText.setText(newTime);
    }

    @NonNull
    @Override
    public VaccinationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_layout, parent, false);
        return new VaccinationAdapter.VaccinationHolder(view);
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

    public class VaccinationHolder extends RecyclerView.ViewHolder {

        TextView dateText, timeText, locationText;

        public VaccinationHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            locationText = itemView.findViewById(R.id.locationText);
        }
    }
}
