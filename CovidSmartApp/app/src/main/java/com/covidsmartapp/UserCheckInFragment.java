package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UserCheckInFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String userID;
    private String loc;

    public UserCheckInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loc = getArguments().getString("location");
        }

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_check_in, container, false);

        TextView locationName = (TextView) view.findViewById(R.id.locationName);
        TextView currentTime = (TextView) view.findViewById(R.id.currentTime);
        Button checkInBtn = (Button) view.findViewById(R.id.checkInBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);

        locationName.setText(loc);

        Date timeNow = Calendar.getInstance().getTime();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("ddMMyyyy-HH:mm:ss");
        dateTimeFormat.setTimeZone(TimeZone.getDefault());

        String dateTime = dateTimeFormat.format(timeNow);
        String [] dateTimeArray = dateTime.split("-");

        String date = dateTimeArray[0];
        String time = dateTimeArray[1];
        String timeWithoutSeconds = time.substring(0,5);

        // Create document ID
        SimpleDateFormat dateTimeFormatForDocument = new SimpleDateFormat("yyyyMMddHHmmss");
        dateTimeFormatForDocument.setTimeZone(TimeZone.getDefault());
        String dateTimeForDocument = dateTimeFormatForDocument.format(timeNow);
        long dateTimeLong = Long.parseLong(dateTimeForDocument);

        currentTime.setText(timeWithoutSeconds);

        UserHomeFragment homeFrag = new UserHomeFragment();

        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> location = new HashMap<>();
                location.put("locationName", loc);
                location.put("checkInDate", date);
                location.put("checkInTime", time);
                location.put("checkedOut", false);
                location.put("dateTime", dateTimeLong);
                location.put("userID", userID);

                db.collection("info")
                        .document(userID)
                        .collection("locations")
                        .document(dateTimeForDocument)
                        .set(location);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(((ViewGroup)getView().getParent()).getId(), homeFrag, "homeFrag")
                        .commit();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(((ViewGroup)getView().getParent()).getId(), homeFrag, "homeFrag")
                        .commit();
            }
        });

        return view;
    }
}
