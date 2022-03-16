package com.covidsmartapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestResultAdapter extends FirestoreRecyclerAdapter<AppointmentClass, TestResultAdapter.TestResultHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TestResultAdapter(@NonNull FirestoreRecyclerOptions<AppointmentClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TestResultAdapter.TestResultHolder holder, int position, @NonNull AppointmentClass model) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String result = model.getTestResult();
        holder.testResult.setText(result);
        if (result.equals("Positive")) {
            holder.testResult.setTextColor(Color.RED);
        }
        else {
            holder.testResult.setTextColor(Color.GREEN);
        }

        String today = String.valueOf(year) + month + day;
        String testDay = model.getDate();
        Date currentDate = new Date();
        Date testDate = null;
        SimpleDateFormat dates = new SimpleDateFormat("yyyyMMdd");
        try {
            testDate = dates.parse(testDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = Math.abs(currentDate.getTime() - testDate.getTime());
        long differenceDates = difference / (24 * 60 * 60 * 1000);
        String dayDifference = Long.toString(differenceDates);
        if (dayDifference.equals("0"))
            holder.daysAgo.setText("Tested today");
        else if (dayDifference.equals("1"))
            holder.daysAgo.setText("Tested 1 day ago");
        else
            holder.daysAgo.setText("Tested " + dayDifference + " days ago");

        holder.location.setText(model.getLocation());
    }

    @NonNull
    @Override
    public TestResultAdapter.TestResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_layout, parent, false);
        return new TestResultAdapter.TestResultHolder(view);
    }

    public class TestResultHolder extends RecyclerView.ViewHolder {

        TextView testResult, daysAgo, location;

        public TestResultHolder(@NonNull View itemView) {
            super(itemView);

            testResult = itemView.findViewById(R.id.testResult);
            daysAgo = itemView.findViewById(R.id.daysAgo);
            location = itemView.findViewById(R.id.location);
        }
    }
}
