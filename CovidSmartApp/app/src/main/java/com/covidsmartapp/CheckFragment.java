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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String userID;
    private String loc;

    public CheckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckFragment newInstance(String param1, String param2) {
        CheckFragment fragment = new CheckFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        loc = getArguments().getString("location");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check, container, false);

        TextView locationName = (TextView) view.findViewById(R.id.locationName);
        TextView currentTime = (TextView) view.findViewById(R.id.currentTime);
        Button checkInBtn = (Button) view.findViewById(R.id.checkInBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);

        locationName.setText(loc);

        Date timeNow = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        dateFormat.setTimeZone(TimeZone.getDefault());

        // For database
        String date = dateFormat.format(timeNow);
        String[] dateTimeArray = date.split("-");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(timeNow);
        currentTime.setText(time);

        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> location = new HashMap<>();
                location.put("locationName", loc);
                location.put("checkInDay", Integer.parseInt(dateTimeArray[0]));
                location.put("checkInMonth", Integer.parseInt(dateTimeArray[1]));
                location.put("checkInYear", Integer.parseInt(dateTimeArray[2]));
                location.put("checkInHour", Integer.parseInt(dateTimeArray[3]));
                location.put("checkInMinute", Integer.parseInt(dateTimeArray[4]));
                location.put("checkedOut", false);

                db.collection("history")
                        .document(userID)
                        .collection("locations")
                        .document(date)
                        .set(location);

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}