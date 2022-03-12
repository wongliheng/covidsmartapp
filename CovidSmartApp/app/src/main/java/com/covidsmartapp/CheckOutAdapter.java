package com.covidsmartapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CheckOutAdapter extends FirestoreRecyclerAdapter<LocationClass, CheckOutAdapter.CheckOutHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CheckOutAdapter(@NonNull FirestoreRecyclerOptions<LocationClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CheckOutHolder holder, int position, @NonNull LocationClass model) {
        holder.checkedInLocation.setText(model.getLocationName());
        holder.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOut(position);
            }
        });
    }

    @NonNull
    @Override
    public CheckOutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_layout, parent, false);
        return new CheckOutHolder(view);
    }

    public void checkOut(int position) {
        DocumentReference ref = getSnapshots().getSnapshot(position).getReference();
        
        // Get time
        Date timeNow = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        dateFormat.setTimeZone(TimeZone.getDefault());
        // For database
        String date = dateFormat.format(timeNow);
        String[] dateTimeArray = date.split("-");

        Integer checkOutday = Integer.parseInt(dateTimeArray[0]);
        Integer checkOutMonth = Integer.parseInt(dateTimeArray[1]);
        Integer checkOutYear = Integer.parseInt(dateTimeArray[2]);
        Integer checkOutHour = Integer.parseInt(dateTimeArray[3]);
        Integer checkOutMinute = Integer.parseInt(dateTimeArray[4]);

        ref.update("checkedOut", true);
        ref.update("checkOutDay", checkOutday);
        ref.update("checkOutMonth", checkOutMonth);
        ref.update("checkOutYear", checkOutYear);
        ref.update("checkOutHour", checkOutHour);
        ref.update("checkOutMinute", checkOutMinute);
    }

    class CheckOutHolder extends RecyclerView.ViewHolder {

        TextView checkedInLocation;
        Button checkOutBtn;

        public CheckOutHolder(@NonNull View itemView) {
            super(itemView);
            checkedInLocation = itemView.findViewById(R.id.checkedInLocation);
            checkOutBtn = itemView.findViewById(R.id.checkOutBtn);
        }
    }
}
