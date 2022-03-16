package com.covidsmartapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoggedIn extends AppCompatActivity {

    // This page serves as the main activity home page that the user arrives to after logging in

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        fragmentManager = getSupportFragmentManager();

        replaceFragment(new HomeFragment());

        NavigationBarView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navHome:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navInfo:
                    replaceFragment(new InfoFragment());
                    break;
                case R.id.navBooking:
                    replaceFragment(new BookingFragment());
                    break;
                case R.id.navHistory:
                    replaceFragment(new LocationHistoryFragment());
                    break;
                case R.id.navMore:
                    replaceFragment(new MoreFragment());
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
                            LoggedIn.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else
            super.onBackPressed();
    }
}