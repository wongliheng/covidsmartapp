package com.covidsmartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorHomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private String doctorEmail;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        fragmentManager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String userID = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                doctorEmail = documentSnapshot.getString("email");
            }
        });

        replaceFragment(new DoctorHomeFragment());

        NavigationBarView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.doctorHome:
                    replaceFragment(new DoctorHomeFragment());
                    break;
                case R.id.update:
                    DoctorUpdateFragment doctorUpdateFragment = new DoctorUpdateFragment();
                    Bundle args = new Bundle();
                    args.putString("email", doctorEmail);
                    doctorUpdateFragment.setArguments(args);
                    replaceFragment(doctorUpdateFragment);
                    break;
                case R.id.navMore:
                    replaceFragment(new DoctorMoreFragment());
                    break;
            }
            return true;
        });
    }
    private void replaceFragment (Fragment fragment){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DoctorHomeActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else
            super.onBackPressed();
    }
}