package com.covidsmartapp;

import android.annotation.SuppressLint;
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

public class CheckOutAdapter extends FirestoreRecyclerAdapter<LocationBeforeCheckOutClass, CheckOutAdapter.CheckOutHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CheckOutAdapter(@NonNull FirestoreRecyclerOptions<LocationBeforeCheckOutClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CheckOutHolder holder, @SuppressLint("RecyclerView") int position, @NonNull LocationBeforeCheckOutClass model) {
        holder.checkedInLocation.setText(model.getLocationName());

        String time = model.getCheckInTime();
        String timeWithoutSeconds = time.substring(0,5);
        holder.checkInTime.setText(timeWithoutSeconds);

        holder.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                String documentID = getDocID(position);

                UserCheckOutFragment checkOutFrag = new UserCheckOutFragment();
                Bundle args = new Bundle();
                args.putString("documentID", documentID);
                checkOutFrag.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, checkOutFrag, "checkOutFrag")
                        .commit();
            }
        });
    }

    @NonNull
    @Override
    public CheckOutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_layout, parent, false);
        return new CheckOutHolder(view);
    }

    public String getDocID(int position) {
        DocumentReference ref = getSnapshots().getSnapshot(position).getReference();
        String documentID = ref.getId();
        return documentID;
    }

    class CheckOutHolder extends RecyclerView.ViewHolder {

        TextView checkedInLocation, checkInTime;
        Button checkOutBtn;

        public CheckOutHolder(@NonNull View itemView) {
            super(itemView);
            checkedInLocation = itemView.findViewById(R.id.checkedInLocation);
            checkInTime = itemView.findViewById(R.id.checkInTime);
            checkOutBtn = itemView.findViewById(R.id.checkOutBtn);
        }
    }
}
