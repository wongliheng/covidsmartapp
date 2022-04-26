package com.covidsmartapp;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserHomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;
    private CheckOutAdapter adapter;

    public UserHomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        //M
        ConstraintLayout messageLayout = (ConstraintLayout) view.findViewById(R.id.messageLayout);
        ImageView unreadImage = (ImageView) view.findViewById(R.id.unreadImage);
        ImageView readImage = (ImageView) view.findViewById(R.id.readImage);
        TextView userGreeting = (TextView) view.findViewById(R.id.userGreeting);
        TextView noRecentLocations = (TextView) view.findViewById(R.id.noRecentLocations);
        ConstraintLayout vaccinationLayout = (ConstraintLayout) view.findViewById(R.id.vaccinationLayout);
        ImageView vaccinationImage = (ImageView) view.findViewById(R.id.vaccinationImage);
        TextView vaccinationStatus = (TextView) view.findViewById(R.id.vaccinationStatus);
        ConstraintLayout scanQR = (ConstraintLayout) view.findViewById(R.id.constraintScan);
        RecyclerView checkOutRecycler = (RecyclerView) view.findViewById(R.id.checkOutRecycler);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        // Start displaying elements on page
        progressBar.setVisibility(View.VISIBLE);

        // Check for unread messages
        getMessages(readImage, unreadImage);

        // Display user greeting
        db.collection("users").document(userID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fName = documentSnapshot.getString("fName");
                userGreeting.setText("Hello " + fName);
            }
        });

        // Get the vaccination status of the user
        getVaccinationStatus(vaccinationStatus, vaccinationImage, progressBar, getActivity());

        // Create the recycler view for checked in locations
        createCheckOutRecycler(checkOutRecycler, noRecentLocations);

        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserMessageCentreFragment userMessageCentreFragment = new UserMessageCentreFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), userMessageCentreFragment, "userMessageCentreFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

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
                        UserQRFragment userQRFragment = new UserQRFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(((ViewGroup)getView().getParent()).getId(), userQRFragment, "userQRFragment")
                                .commit();
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {

                    }
                });
            }
        });

        // Vaccination status button
        vaccinationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserVaccinationStatusFragment userVaccinationStatusFragment = new UserVaccinationStatusFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), userVaccinationStatusFragment, "userVaccinationStatusFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void getMessages(ImageView read, ImageView unread) {
        ArrayList<String> messageStatus = new ArrayList<>();
        db.collection("info")
                .document(userID)
                .collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String status = document.getString("status");
                        if (status.equals("unread")) {
                            messageStatus.add("unread");
                        } else if (status.equals("read")){
                            messageStatus.add("read");
                        }
                    }
                    // Show unread message image if there are unread messages
                    if (messageStatus.contains("unread")) {
                        unread.setVisibility(View.VISIBLE);
                    }
                    else {
                        read.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
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
                        String status = document.getString("vaccinationStatus");
                        vaccinationStatus.setText(status);
                        setVaccinationImage(status, vaccinationImage, ct);
                    } else {
                        vaccinationStatus.setText("Unvaccinated");
                        setVaccinationImage("Unvaccinated", vaccinationImage, ct);
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
        if (status.equals("Unvaccinated")) {
            vaccinationImage.setImageDrawable(ct.getResources().getDrawable(R.drawable.ic_baseline_not_interested_24));
        } else if (status.equals("Partially Vaccinated")) {
            vaccinationImage.setImageDrawable(ct.getResources().getDrawable(R.drawable.ic_baseline_not_interested_24));
        } else if (status.equals("Vaccinated without Booster")) {
            vaccinationImage.setImageDrawable(ct.getResources().getDrawable(R.drawable.ic_baseline_check_24));
        } else if (status.equals("Fully Vaccinated")) {
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
        checkOutRecycler.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
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
