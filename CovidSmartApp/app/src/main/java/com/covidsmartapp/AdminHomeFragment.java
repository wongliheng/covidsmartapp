package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AdminHomeFragment extends Fragment {

    private String adminEmail, adminPw;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adminEmail = getArguments().getString("email");
            adminPw = getArguments().getString("pw");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        Button createDoctorBtn = (Button) view.findViewById(R.id.createDoctorBtn);
        Button manageDoctorBtn = (Button) view.findViewById(R.id.manageDoctorBtn);
        Button createCTBtn = (Button) view.findViewById(R.id.createCTBtn);
        Button manageCTBtn = (Button) view.findViewById(R.id.manageCTBtn);

        createDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminCreateDoctorFragment createDoctorFragment = new AdminCreateDoctorFragment();
                Bundle args = new Bundle();
                args.putString("email", adminEmail);
                args.putString("pw", adminPw);
                createDoctorFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), createDoctorFragment, "createDoctorFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        manageDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminManageDoctorFragment manageDoctorFragment = new AdminManageDoctorFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), manageDoctorFragment, "manageDoctorFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        createCTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminCreateCTFragment createCTFragment = new AdminCreateCTFragment();
                Bundle args = new Bundle();
                args.putString("email", adminEmail);
                args.putString("pw", adminPw);
                createCTFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), createCTFragment, "createCTFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        manageCTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminManageCTFragment manageCTFragment = new AdminManageCTFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), manageCTFragment, "manageCTFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}