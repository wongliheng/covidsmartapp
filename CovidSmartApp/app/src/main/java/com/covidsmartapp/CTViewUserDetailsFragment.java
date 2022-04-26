package com.covidsmartapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CTViewUserDetailsFragment extends Fragment {

    private String location, date, time, userID;
    private FirebaseFirestore db;
    private AlertDialog messageDialog;

    public CTViewUserDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = getArguments().getString("location");
            date = getArguments().getString("date");
            time = getArguments().getString("time");
            userID = getArguments().getString("userID");
        }

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_t_view_user_details, container, false);

        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        TextView emailText = (TextView) view.findViewById(R.id.emailText);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView phoneText = (TextView) view.findViewById(R.id.phoneText);
        TextView locationText = (TextView) view.findViewById(R.id.locationText);
        TextView dateText = (TextView) view.findViewById(R.id.dateText);
        TextView timeText = (TextView) view.findViewById(R.id.timeText);
        Button messageBtn = (Button) view.findViewById(R.id.messageBtn);

        getUserDetails(userID, emailText, nameText, phoneText);

        locationText.setText(location);
        dateText.setText(date);
        timeText.setText(time);

        String messageDraft = "You have been in possible close contact with someone who has COVID-19 during your visit at "
                + location + " on " + date + " during " + time + ".";

        LinearLayout messageLayout = new LinearLayout(getActivity());
        messageLayout.setOrientation(LinearLayout.VERTICAL);
        messageLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        EditText messageInput = new EditText(getActivity());
        messageInput.setText(messageDraft);
        messageLayout.setPadding(30, 0, 30, 0);
        messageLayout.addView(messageInput);

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageDialog != null && !messageDialog.isShowing()) {
                    messageDialog.show();
                }
                else {
                    messageDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Message Preview")
                            .setMessage("Notify the user about the possible close contact.")
                            .setView(messageLayout)
                            .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String message = messageInput.getText().toString();

                                    Date now = Calendar.getInstance().getTime();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
                                    dateFormat.setTimeZone(TimeZone.getDefault());
                                    String today = dateFormat.format(now);

                                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                    dateTimeFormat.setTimeZone(TimeZone.getDefault());
                                    String dateTimeString = dateTimeFormat.format(now);
                                    Long dateTime = Long.parseLong(dateTimeString);

                                    Map<String, Object> messageObject = new HashMap<>();
                                    messageObject.put("date", today);
                                    messageObject.put("dateTime", dateTime);
                                    messageObject.put("status", "unread");
                                    messageObject.put("message", message);

                                    db.collection("info")
                                            .document(userID)
                                            .collection("messages")
                                            .document()
                                            .set(messageObject);

                                    Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void getUserDetails(String userID, TextView emailText, TextView nameText, TextView phoneText) {
        db.collection("users")
                .document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String email = document.getString("email");

                                String fName = document.getString("fName");
                                String lName = document.getString("lName");
                                String name = fName + " " + lName;

                                String phoneNum = document.getString("phoneNum");

                                emailText.setText(email);
                                nameText.setText(name);
                                phoneText.setText(phoneNum);
                            }
                        }
                    }
                });
    }
}