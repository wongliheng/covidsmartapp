package com.covidsmartapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CheckOutFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;

    private String documentID;

    public CheckOutFragment() {
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
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        TextView checkOutText = (TextView) view.findViewById(R.id.checkOutText);
        TextView locationName = (TextView) view.findViewById(R.id.locationName);
        TextView durationText = (TextView) view.findViewById(R.id.durationText);
        Button backToHomeBtn = (Button) view.findViewById(R.id.backToHomeBtn);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

//         Get time
        Date timeNow = Calendar.getInstance().getTime();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("ddMMyyyy-HH:mm:ss");
        dateTimeFormat.setTimeZone(TimeZone.getDefault());

        String dateTime = dateTimeFormat.format(timeNow);
        String [] dateTimeArray = dateTime.split("-");

        String date = dateTimeArray[0];
        String time = dateTimeArray[1];

        DocumentReference ref = db.collection("info")
                .document(userID)
                .collection("locations")
                .document(documentID);

        ref.update("checkedOut", true);
        ref.update("checkOutDate", date);
        ref.update("checkOutTime", time);

        db.collection("info")
                .document(userID)
                .collection("locations")
                .document(documentID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String loc = documentSnapshot.getString("locationName");
                String checkInTime = documentSnapshot.getString("checkInTime");
                String checkOutTime = documentSnapshot.getString("checkOutTime");

                String checkInTimeWithoutSeconds =  checkInTime.substring(0,5);
                String checkOutTimeWithoutSeconds =  checkOutTime.substring(0,5);

                checkOutText.setText("Checked Out");
                locationName.setText(loc);
                durationText.setText(checkInTimeWithoutSeconds + " - " + checkOutTimeWithoutSeconds);
                backToHomeBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFrag = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(((ViewGroup)getView().getParent()).getId(), homeFrag, "homeFrag")
                        .commit();
            }
        });

        return view;
    }
}