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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFragment extends Fragment {

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

    public CheckInFragment() {
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
    public static CheckInFragment newInstance(String param1, String param2) {
        CheckInFragment fragment = new CheckInFragment();
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
        View view = inflater.inflate(R.layout.fragment_check_in, container, false);

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

        currentTime.setText(timeWithoutSeconds);

        HomeFragment homeFrag = new HomeFragment();

        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> location = new HashMap<>();
                location.put("locationName", loc);
                location.put("checkInDate", date);
                location.put("checkInTime", time);
                location.put("checkedOut", false);

                db.collection("info")
                        .document(userID)
                        .collection("locations")
                        .document(dateTime)
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
