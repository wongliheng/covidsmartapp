package com.covidsmartapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        ConstraintLayout constraintVaccination = (ConstraintLayout) view.findViewById(R.id.constraintVaccination);
        ImageView vaccinationImage = (ImageView) view.findViewById(R.id.vaccinationImage);
        TextView vaccinationStatus = (TextView) view.findViewById(R.id.vaccinationStatus);
        ConstraintLayout scanQR = (ConstraintLayout) view.findViewById(R.id.constraintScan);
        RecyclerView checkOutRecycler = (RecyclerView) view.findViewById(R.id.checkOutRecycler);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        // Display user greeting
        db.collection("users").document(userID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fName = documentSnapshot.getString("fName");
                userGreeting.setText("Hello " + fName);
            }
        });

        getVaccinationStatus(vaccinationStatus, vaccinationImage, progressBar, getActivity());

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

    private void getVaccinationStatus(TextView vaccinationStatus, ImageView vaccinationImage, ProgressBar progressBar, Context ct) {
        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        Timestamp timestamp = new Timestamp(currentDate);

        DocumentReference vacRef = db.collection("info")
                .document(userID)
                .collection("vaccination")
                .document("vaccinationStatus");

        vacRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String status = document.getString("status");
                        if (status.equals("none")) {
                            vaccinationStatus.setText("Not Vaccinated");
                        } else if (status.equals("first")) {
                            vaccinationStatus.setText("Partially Vaccinated");
                        } else if (status.equals("second")) {
                            vaccinationStatus.setText("Vaccinated without Booster");
                        } else if (status.equals("booster")) {
                            vaccinationStatus.setText("Fully Vaccinated");
                        }
                        setVaccinationImage(status, vaccinationImage, ct);
                    } else {
                        Log.d("DEBUG", "No such document");
                    }
                } else {
                    Log.d("DEBUG", "get failed with ", task.getException());
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setVaccinationImage (String status, ImageView vaccinationImage, Context ct) {
        if (status.equals("none")) {
            vaccinationImage.setImageDrawable(ct.getResources().getDrawable(R.drawable.ic_baseline_not_interested_24));
        } else if (status.equals("first")) {
            vaccinationImage.setImageDrawable(ct.getResources().getDrawable(R.drawable.ic_baseline_not_interested_24));
        } else if (status.equals("second")) {
            vaccinationImage.setImageDrawable(ct.getResources().getDrawable(R.drawable.ic_baseline_check_24));
        } else if (status.equals("booster")) {
            vaccinationImage.setImageDrawable(ct.getResources().getDrawable(R.drawable.ic_baseline_check_circle_outline_24));
        }
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
