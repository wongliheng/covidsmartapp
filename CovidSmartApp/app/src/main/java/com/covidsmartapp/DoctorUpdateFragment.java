package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DoctorUpdateFragment extends Fragment {

    private String doctorEmail;

    public DoctorUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorEmail = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_update, container, false);

        Button updateVaccinationBtn = (Button) view.findViewById(R.id.updateVaccinationBtn);
        Button updateTestBtn = (Button) view.findViewById(R.id.updateTestBtn);

        updateVaccinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorUpdateVaccinationFragment doctorUpdateVaccinationFragment = new DoctorUpdateVaccinationFragment();
                Bundle args = new Bundle();
                args.putString("email", doctorEmail);
                doctorUpdateVaccinationFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), doctorUpdateVaccinationFragment, "updateVaccinationFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        updateTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorUpdateTestsFragment doctorUpdateTestsFragment = new DoctorUpdateTestsFragment();
                Bundle args = new Bundle();
                args.putString("email", doctorEmail);
                doctorUpdateTestsFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), doctorUpdateTestsFragment, "doctorUpdateTestsFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}