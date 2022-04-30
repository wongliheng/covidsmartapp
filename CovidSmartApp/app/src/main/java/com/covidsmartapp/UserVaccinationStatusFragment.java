package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserVaccinationStatusFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;

    public UserVaccinationStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_vaccination_status, container, false);

        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        TextView vaccinationStatus = (TextView) view.findViewById(R.id.vaccinationStatus);
        TextView vaccineEligibility = (TextView) view.findViewById(R.id.vaccineEligibility);
        TextView vaccineEligibilityDate = (TextView) view.findViewById(R.id.vaccineEligibilityDate);

        db.collection("info")
                .document(userID)
                .collection("vaccination")
                .document("vaccinationStatus")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String status = document.getString("vaccinationStatus");
                        vaccinationStatus.setText(status);
                        if (status.equals("Unvaccinated")) {
                            vaccineEligibility.setText("Vaccine Eligibility");
                            vaccineEligibility.setVisibility(View.VISIBLE);
                            vaccineEligibilityDate.setText("Eligible");
                            vaccineEligibilityDate.setVisibility(View.VISIBLE);
                        } else if (status.equals("Partially Vaccinated")) {
                            getTimeUntilNextShot(false, vaccineEligibility, vaccineEligibilityDate);
                        } else if (status.equals("Vaccinated without Booster")) {
                            vaccineEligibility.setText("Booster Eligibility Date:");
                            getTimeUntilNextShot(true, vaccineEligibility, vaccineEligibilityDate);
                        }
                    } else {
                        vaccinationStatus.setText("Unvaccinated");
                        vaccineEligibility.setVisibility(View.VISIBLE);
                        vaccineEligibility.setText("Vaccine Eligibility:");
                        vaccineEligibilityDate.setVisibility(View.VISIBLE);
                        vaccineEligibilityDate.setText("Eligible");
                    }
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserHomeFragment homeFrag = new UserHomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .replace(((ViewGroup) getView().getParent()).getId(), homeFrag, "homeFrag")
                        .commit();
            }
        });

        return view;
    }

    private void getTimeUntilNextShot(boolean booster, TextView vaccineEligibility, TextView vaccineEligibilityDate) {
        vaccineEligibility.setVisibility(View.VISIBLE);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        String monthString = "";
        String dayString = "";
        String hourString = "";
        String minuteString = "";

        // Because month is 0-11
        month++;

        if (month < 10)
            monthString = "0" + month;
        else
            monthString = String.valueOf(month);

        if (day < 10)
            dayString = "0" + day;
        else
            dayString = String.valueOf(day);

        if (hour < 10)
            hourString = "0" + hour;
        else
            hourString = String.valueOf(hour);

        if (minutes < 10)
            minuteString = "0" + minutes;
        else
            minuteString = String.valueOf(minutes);

        String dateTime = year + monthString + dayString + hourString + minuteString;
        long dateTimeLong = Long.parseLong(dateTime);

        db.collection("info")
                .document(userID)
                .collection("appointments")
                .whereEqualTo("appointmentType", "COVID-19 Vaccination")
                .whereLessThan("dateTime", dateTimeLong)
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String date = document.getString("date");
                                String vaccinationYearString = date.substring(0, 4);
                                String vaccinationMonthString = date.substring(4, 6);
                                String vaccinationDayString = date.substring(6, 8);
                                int vaccinationDay = Integer.parseInt(vaccinationDayString);
                                // Calendar uses 0-11 month
                                int vaccinationMonth = Integer.parseInt(vaccinationMonthString) - 1;
                                int vaccinationYear = Integer.parseInt(vaccinationYearString);

                                Calendar eligibleDate = Calendar.getInstance();
                                eligibleDate.set(Calendar.DAY_OF_MONTH, vaccinationDay);
                                eligibleDate.set(Calendar.MONTH, vaccinationMonth);
                                eligibleDate.set(Calendar.YEAR, vaccinationYear);

                                if (booster) {
                                    eligibleDate.add(Calendar.DATE, 150);
                                } else {
                                    eligibleDate.add(Calendar.DATE, 21);
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                vaccineEligibilityDate.setText(simpleDateFormat.format(eligibleDate.getTime()));
                                vaccineEligibilityDate.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

    }
}