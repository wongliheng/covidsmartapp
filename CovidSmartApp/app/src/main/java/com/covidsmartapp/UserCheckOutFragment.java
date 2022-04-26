package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UserCheckOutFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;

    private String documentID;

    public UserCheckOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            documentID = getArguments().getString("documentID");
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_check_out, container, false);

        TextView checkOutText = (TextView) view.findViewById(R.id.checkOutText);
        TextView locationName = (TextView) view.findViewById(R.id.locationName);
        TextView durationText = (TextView) view.findViewById(R.id.durationText);
        Button backToHomeBtn = (Button) view.findViewById(R.id.backToHomeBtn);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        Date timeNow = Calendar.getInstance().getTime();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("ddMMyyyy-HH:mm:ss");
        dateTimeFormat.setTimeZone(TimeZone.getDefault());

        // Check out
        String dateTime = dateTimeFormat.format(timeNow);
        String [] dateTimeArray = dateTime.split("-");

        String date = dateTimeArray[0];
        String time = dateTimeArray[1];

        Map<String, Object> checkOutUpdates = new HashMap<>();
        checkOutUpdates.put("checkedOut", true);
        checkOutUpdates.put("checkOutDate", date);
        checkOutUpdates.put("checkOutTime", time);

        db.collection("info")
                .document(userID)
                .collection("locations")
                .document(documentID)
                .update(checkOutUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        displayInfo(checkOutText, locationName, durationText, backToHomeBtn, progressBar);
                    }
                });

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserHomeFragment homeFrag = new UserHomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(((ViewGroup)getView().getParent()).getId(), homeFrag, "homeFrag")
                        .commit();
            }
        });

        return view;
    }

    private void displayInfo (TextView checkOutText, TextView locationName, TextView durationText,
                              Button backToHomeBtn, ProgressBar progressBar) {

        db.collection("info")
                .document(userID)
                .collection("locations")
                .document(documentID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String location = documentSnapshot.getString("locationName");
                String checkInTime = documentSnapshot.getString("checkInTime");
                String checkOutTime = documentSnapshot.getString("checkOutTime");

                String checkInTimeWithoutSeconds =  checkInTime.substring(0,5);
                String checkOutTimeWithoutSeconds =  checkOutTime.substring(0,5);

                checkOutText.setText("Checked Out");
                locationName.setText(location);
                durationText.setText(checkInTimeWithoutSeconds + " - " + checkOutTimeWithoutSeconds);
                backToHomeBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}