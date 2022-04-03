package com.covidsmartapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class AppointmentAdapter extends FirestoreRecyclerAdapter<AppointmentClass, AppointmentAdapter.AppointmentHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context context;
    private String userID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AppointmentAdapter(@NonNull FirestoreRecyclerOptions<AppointmentClass> options, String userID, Context context) {
        super(options);
        this.userID = userID;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentHolder holder, @SuppressLint("RecyclerView") int position,
                                    @NonNull AppointmentClass model) {
        holder.appointmentTypeText.setText(model.getAppointmentType());
        holder.locationText.setText(model.getLocation());

        String date = model.getDate();
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String dayString = date.substring(6, 8);

//        String [] dateArray = date.split("_");
//        String dayString = dateArray[2];
        int day = Integer.parseInt(dayString);
        String monthString = getMonthString(Integer.parseInt(month));

        String newDate = day + " " + monthString + " " + year;
        holder.dateText.setText(newDate);

        String time = model.getTime();
        String meridiem = "AM";
//        String [] timeArray = time.split(":");
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

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String documentID = String.valueOf(model.getDateTime());
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference appointment = db.collection("info")
                        .document(userID)
                        .collection("appointments")
                        .document(documentID);

                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to cancel this booking?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                appointment.delete();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_layout, parent, false);
        return new AppointmentHolder(view);
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

    public class AppointmentHolder extends RecyclerView.ViewHolder {

        TextView appointmentTypeText, dateText, timeText, locationText;
        Button cancelBtn;

        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);
            appointmentTypeText = itemView.findViewById(R.id.appointmentTypeText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            locationText = itemView.findViewById(R.id.locationText);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
        }
    }
}
