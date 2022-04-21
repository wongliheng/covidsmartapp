package com.covidsmartapp;

import android.annotation.SuppressLint;
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

public class UpdateTestAdapter extends FirestoreRecyclerAdapter<AppointmentClass, UpdateTestAdapter.TestHolder> {

    public UpdateTestAdapter(@NonNull FirestoreRecyclerOptions<AppointmentClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TestHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AppointmentClass model) {
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
        holder.resultText.setText(model.getStatus());

        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                String documentID = getDocID(position);

//                UserCheckOutFragment checkOutFrag = new UserCheckOutFragment();
//                Bundle args = new Bundle();
//                args.putString("documentID", documentID);
//                checkOutFrag.setArguments(args);
//                activity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentContainerView, checkOutFrag, "checkOutFrag")
//                        .commit();
            }
        });
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

    public String getDocID(int position) {
        DocumentReference ref = getSnapshots().getSnapshot(position).getReference();
        String documentID = ref.getId();
        return documentID;
    }

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_test_layout, parent, false);
        return new UpdateTestAdapter.TestHolder(view);
    }

    public class TestHolder extends RecyclerView.ViewHolder {

        TextView locationText, dateText, timeText, resultText;
        Button updateBtn;

        public TestHolder(@NonNull View itemView) {
            super(itemView);
            locationText = itemView.findViewById(R.id.locationText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            resultText = itemView.findViewById(R.id.resultText);
            updateBtn = itemView.findViewById(R.id.updateBtn);
        }
    }
}
