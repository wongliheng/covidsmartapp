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

        createDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminCreateDoctorFragment createDoctorFragment = new AdminCreateDoctorFragment();
                Bundle args = new Bundle();
                args.putString("email", adminEmail);
                args.putString("pw", adminPw);
                createDoctorFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), createDoctorFragment, "appointmentFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}