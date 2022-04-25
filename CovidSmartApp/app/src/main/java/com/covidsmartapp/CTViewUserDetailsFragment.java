package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CTViewUserDetailsFragment extends Fragment {

    private String location, date, time, userID;
    private FirebaseFirestore db;

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

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> message = new HashMap<>();
                message.put("date", "date");
                message.put("status", "unread");
                message.put("message", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");

                db.collection("info")
                        .document(userID)
                        .collection("messages")
                        .document()
                        .set(message);
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