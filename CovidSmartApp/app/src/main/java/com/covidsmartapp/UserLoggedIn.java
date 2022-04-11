package com.covidsmartapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationBarView;

public class UserLoggedIn extends AppCompatActivity {

    // This page serves as the main activity home page that the user arrives to after logging in

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged_in);

        fragmentManager = getSupportFragmentManager();

        replaceFragment(new UserHomeFragment());

        NavigationBarView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navHome:
                    replaceFragment(new UserHomeFragment());
                    break;
                case R.id.navInfo:
                    replaceFragment(new UserInfoFragment());
                    break;
                case R.id.navBooking:
                    replaceFragment(new UserBookingFragment());
                    break;
                case R.id.navHistory:
                    replaceFragment(new UserLocationHistoryFragment());
                    break;
                case R.id.navMore:
                    replaceFragment(new UserMoreFragment());
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
                            UserLoggedIn.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else
            super.onBackPressed();
    }
}