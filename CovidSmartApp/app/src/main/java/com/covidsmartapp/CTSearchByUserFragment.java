package com.covidsmartapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CTSearchByUserFragment extends Fragment {

    private FirebaseFirestore db;
    private LocationHistoryAdapterForCT adapter;
    private String userEmail;

    public CTSearchByUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_t_search_by_user, container, false);

        SearchableSpinner userSpinner = (SearchableSpinner) view.findViewById(R.id.userSpinner);
        TextView nameTitle = (TextView) view.findViewById(R.id.nameTitle);
        TextView nameText = (TextView) view.findViewById(R.id.nameText);
        TextView emailTitle = (TextView) view.findViewById(R.id.emailTitle);
        TextView emailText = (TextView) view.findViewById(R.id.emailText);
        TextView phoneTitle = (TextView) view.findViewById(R.id.phoneTitle);
        TextView phoneText = (TextView) view.findViewById(R.id.phoneText);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noHistory = (TextView) view.findViewById(R.id.noHistory);

        createUserSpinner(userSpinner);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userEmail = adapterView.getItemAtPosition(i).toString();
                getUserInfo(userEmail, nameText, emailText, phoneText, recyclerView, noHistory);
                nameTitle.setVisibility(View.VISIBLE);
                nameText.setVisibility(View.VISIBLE);
                emailTitle.setVisibility(View.VISIBLE);
                emailText.setVisibility(View.VISIBLE);
                phoneTitle.setVisibility(View.VISIBLE);
                phoneText.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void getUserInfo(String email, TextView nameText, TextView emailText, TextView phoneText, RecyclerView recyclerView, TextView noHistory) {
        ArrayList<String> userIDList = new ArrayList<>();
        db.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userEmail = document.getString("email");
                        if (userEmail.equals(email)) {
                            String userID = document.getString("userID");
                            userIDList.add(userID);
                        }
                    }

                    db.collection("users")
                            .document(userIDList.get(0))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String fName = document.getString("fName");
                                            String lName = document.getString("lName");
                                            String email = document.getString("email");
                                            String phoneNum = document.getString("phoneNum");
                                            String name = fName + " " + lName;
                                            nameText.setText(name);
                                            emailText.setText(email);
                                            phoneText.setText(phoneNum);
                                        }
                                    }
                                }
                            });
                    createLocationRecycler(userIDList.get(0), recyclerView, noHistory);
                }
            }
        });
    }

    private void createLocationRecycler(String userID, RecyclerView recyclerView, TextView noHistory) {
        CollectionReference ref = db.collection("info")
                .document(userID)
                .collection("locations");

        Query query = ref.orderBy("dateTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<LocationAfterCheckOutClass> options = new FirestoreRecyclerOptions.Builder<LocationAfterCheckOutClass>()
                .setQuery(query, LocationAfterCheckOutClass.class)
                .build();

        adapter = new LocationHistoryAdapterForCT(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noHistory.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noHistory.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void createUserSpinner(SearchableSpinner userSpinner) {
        ArrayList<String> emails = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String type = document.getString("type");
                                if (type.equals("user")) {
                                    String email = document.getString("email");
                                    emails.add(email);
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner, emails);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            userSpinner.setAdapter(adapter);
                            userSpinner.setTitle("Select a user");
                            userSpinner.setPositiveButton("OK");
                        }
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}