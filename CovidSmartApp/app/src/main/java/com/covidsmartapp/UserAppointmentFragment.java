package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserAppointmentFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;
    private String appointmentType;

    private boolean timeCheck = false;

    public UserAppointmentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserAppointmentFragment newInstance(String param1, String param2) {
        UserAppointmentFragment fragment = new UserAppointmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appointmentType = getArguments().getString("appointmentType");
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_appointment, container, false);

        EditText dateEditText = (EditText) view.findViewById(R.id.dateEditText);

//        TextView appointmentText = (TextView) view.findViewById(R.id.appointmentText);
        TextView appointmentTypeText = (TextView) view.findViewById(R.id.appointmentTypeText);
        TextView dateText = (TextView) view.findViewById(R.id.dateText);
        TextView timeText = (TextView) view.findViewById(R.id.timeText);
        TextView locationText = (TextView) view.findViewById(R.id.locationText);

        if (appointmentType.equals("COVID-19 Test")){
            appointmentTypeText.setText("COVID-19 Test Booking");
        }
        else if (appointmentType.equals("COVID-19 Vaccination")) {
            appointmentTypeText.setText("COVID-19 Vaccination Booking");
        }

//        SearchableSpinner appointmentSpinner = (SearchableSpinner) view.findViewById(R.id.appointmentSpinner);
//        Spinner appointmentSpinner = (Spinner) view.findViewById(R.id.appointmentSpinner);
        Spinner timeSpinner = (Spinner) view.findViewById(R.id.timeSpinner) ;
        Spinner locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        Button bookBtn = (Button) view.findViewById(R.id.bookBtn);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dateEditText.setText(day + " " + getMonthString(month) + " " + year);

        SpinnerCreation createSpinner = new SpinnerCreation(getActivity());
//        createSpinner.createAppointmentSpinner(appointmentSpinner);
        createSpinner.createTimeSpinner(timeSpinner, day, month);
        createSpinner.createLocationSpinner(locationSpinner);

//        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.locations, R.layout.spinner);
//        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        locationSpinner.setAdapter(locationAdapter);

        DatePickerFragment.DatePickerFragmentListener listener = new DatePickerFragment.DatePickerFragmentListener() {
            @Override
            public void onDateSet(int year, int month, int day) {
                dateEditText.setText(day + " " + getMonthString(month) + " " + year);
                createSpinner.createTimeSpinner(timeSpinner, day, month);
                timeText.setText("Time:");
                timeText.setTextColor(getResources().getColor(R.color.teal_700));
            }
        };

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setDatePickerListener(listener);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = dateEditText.getText().toString();
                String time = timeSpinner.getSelectedItem().toString();
                String location = locationSpinner.getSelectedItem().toString();

                if (!time.equals("No timeslots available"))
                    timeCheck = true;

                if (!timeCheck){
                    timeText.setText("Please select a different date");
                    timeText.setTextColor(getResources().getColor(R.color.red_400));
                } else {
                    timeText.setText("Time:");
                    timeText.setTextColor(getResources().getColor(R.color.teal_700));
                }

                if (timeCheck) {
                    String [] dateArray = date.split(" ");
                    String monthDigit  = getMonth(dateArray[1]);
                    String dayString = dateArray[0];
                    int day = Integer.parseInt(dateArray[0]);
                    if (day < 10)
                        dayString = "0" + day;

                    String newDate = dateArray[2] + monthDigit + dayString;

                    String [] timeArray = time.split(":");
                    int hour = Integer.parseInt(timeArray[0]);
                    String [] subArray = timeArray[1].split(" ");
                    String minuteString = subArray[0];

                    // Get hour in 24 hour format
                    if (hour < 9)
                        hour = hour + 12;

                    // Add leading zero
                    String hourString = String.valueOf(hour);
                    if (hour < 10)
                        hourString = "0" + hour;

                    String newTime = hourString + minuteString;
                    String documentID = newDate + newTime;
                    long dateTimeLong = Long.parseLong(documentID);

                    Map<String, Object> appointment = new HashMap<>();
                    appointment.put("appointmentType", appointmentType);
                    appointment.put("location", location);
                    appointment.put("date", newDate);
                    appointment.put("time", newTime);
                    appointment.put("dateTime", dateTimeLong);
                    appointment.put("status", "Processing");

                    DocumentReference ref = db.collection("info")
                            .document(userID)
                            .collection("appointments")
                            .document(String.valueOf(dateTimeLong));
                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("You already have an appointment at this timeslot.");
                                    final AlertDialog dialog = builder.create();
                                    dialog.show();

                                    final Timer t = new Timer();
                                    t.schedule(new TimerTask() {
                                        public void run() {
                                            dialog.dismiss();
                                            t.cancel();
                                        }
                                    }, 1250);
                                } else {
                                    ref.set(appointment);

                                    UserBookingConfirmed bookingConfirmed = new UserBookingConfirmed();
                                    Bundle args = new Bundle();
                                    args.putString("appointmentType", appointmentType);
                                    args.putString("date", date);
                                    args.putString("time", time);
                                    args.putString("location", location);
                                    bookingConfirmed.setArguments(args);
                                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                            .replace(((ViewGroup)getView().getParent()).getId(), bookingConfirmed, "bookingConfirmed")
                                            .commit();

                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        }
                    });

                }
            }
        });

        return view;
    }

    private String getMonthString (int month) {
        String monthString = "December";
        if (month == 0)
            monthString = "January";
        else if (month == 1)
            monthString = "February";
        else if (month == 2)
            monthString = "March";
        else if (month == 3)
            monthString = "April";
        else if (month == 4)
            monthString = "May";
        else if (month == 5)
            monthString = "June";
        else if (month == 6)
            monthString = "July";
        else if (month == 7)
            monthString = "August";
        else if (month == 8)
            monthString = "September";
        else if (month == 9)
            monthString = "October";
        else if (month == 10)
            monthString = "November";

        return monthString;
    }

    private String getMonth (String monthString) {
        String month = "12";
        if (monthString.equals("January"))
            month = "01";
        else if (monthString.equals("February"))
            month = "02";
        else if (monthString.equals("March"))
            month = "03";
        else if (monthString.equals("April"))
            month = "04";
        else if (monthString.equals("May"))
            month = "05";
        else if (monthString.equals("June"))
            month = "06";
        else if (monthString.equals("July"))
            month = "07";
        else if (monthString.equals("August"))
            month = "08";
        else if (monthString.equals("September"))
            month = "09";
        else if (monthString.equals("October"))
            month = "10";
        else if (monthString.equals("November"))
            month = "11";

        return month;
    }
}