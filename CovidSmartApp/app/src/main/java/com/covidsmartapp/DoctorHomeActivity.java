package com.covidsmartapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationBarView;

public class DoctorHomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        fragmentManager = getSupportFragmentManager();

//        replaceFragment(adminHomeFragment);

        NavigationBarView bottomNav = findViewById(R.id.bottom_navigation);

//        bottomNav.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.adminPanel:
//                    replaceFragment(adminHomeFragment);
//                    break;
//                case R.id.navMore:
//                    replaceFragment(new AdminMoreFragment());
//                    break;
//            }
//            return true;
//        });
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