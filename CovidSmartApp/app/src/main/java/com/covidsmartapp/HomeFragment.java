package com.covidsmartapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;
    private CheckOutAdapter adapter;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView userGreeting = (TextView) view.findViewById(R.id.userGreeting);
        TextView noRecentLocations = (TextView) view.findViewById(R.id.noRecentLocations);
        Button scanQR = (Button) view.findViewById(R.id.scanQR);
        RecyclerView checkOutRecycler = (RecyclerView) view.findViewById(R.id.checkOutRecycler);

        // Display user greeting
        db.collection("users").document(userID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fName = documentSnapshot.getString("fName");
                userGreeting.setText("Hello " + fName);
            }
        });

        // Check Out Recycler View
        createCheckOutRecycler(checkOutRecycler, noRecentLocations);

        // QR code button
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions = {Manifest.permission.CAMERA};
                String rationale = "Please provide camera permission to scan QR Codes";
                Permissions.Options options = new Permissions.Options()
                        .setRationaleDialogTitle("Info")
                        .setSettingsDialogTitle("Warning");

                Permissions.check(getActivity(), permissions, rationale, options, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        QRFragment qrFrag = new QRFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(((ViewGroup)getView().getParent()).getId(), qrFrag, "qrFrag")
                                .commit();
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {

                    }
                });
            }
        });

        return view;
    }

    private void createCheckOutRecycler(RecyclerView checkOutRecycler, TextView noRecentLocations) {
        CollectionReference ref = db.collection("info")
                .document(userID)
                .collection("locations");
        Query query = ref.whereEqualTo("checkedOut", false);

        FirestoreRecyclerOptions<LocationBeforeCheckOutClass> options = new FirestoreRecyclerOptions.Builder<LocationBeforeCheckOutClass>()
                .setQuery(query, LocationBeforeCheckOutClass.class)
                .build();

        adapter = new CheckOutAdapter(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    checkOutRecycler.setVisibility(View.INVISIBLE);
                    noRecentLocations.setVisibility(View.VISIBLE);
                }
                else {
                    checkOutRecycler.setVisibility(View.VISIBLE);
                    noRecentLocations.setVisibility(View.GONE);
                }
            }
        };
        checkOutRecycler.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        checkOutRecycler.setAdapter(adapter);
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
