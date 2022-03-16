package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class LocationHistoryFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;
    private LocationHistoryAdapter adapter;

    public LocationHistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_location_history, container, false);

        RecyclerView historyRecycler = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noHistory = (TextView) view.findViewById(R.id.noHistory);

        createHistoryRecycler(historyRecycler, noHistory);

        return view;
    }

    private void createHistoryRecycler(RecyclerView historyRecycler, TextView noHistory) {

        CollectionReference ref = db.collection("info")
                .document(userID)
                .collection("locations");

        Query query = ref.whereEqualTo("checkedOut", true)
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .limit(20);

        FirestoreRecyclerOptions<LocationAfterCheckOutClass> options = new FirestoreRecyclerOptions.Builder<LocationAfterCheckOutClass>()
                .setQuery(query, LocationAfterCheckOutClass.class)
                .build();

        adapter = new LocationHistoryAdapter(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    historyRecycler.setVisibility(View.INVISIBLE);
                    noHistory.setVisibility(View.VISIBLE);
                }
                else {
                    historyRecycler.setVisibility(View.VISIBLE);
                    noHistory.setVisibility(View.GONE);
                }
            }
        };
        historyRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyRecycler.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}