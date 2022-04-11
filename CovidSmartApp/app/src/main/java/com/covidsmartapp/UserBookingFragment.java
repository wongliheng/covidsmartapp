package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserBookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserBookingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;

    public UserBookingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment bookingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserBookingFragment newInstance(String param1, String param2) {
        UserBookingFragment fragment = new UserBookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_booking, container, false);

        Button bookTest = (Button) view.findViewById(R.id.bookTest);
        Button bookVaccination = (Button) view.findViewById(R.id.bookVaccination);
        Button viewAppointments = (Button) view.findViewById(R.id.viewAppointments);
        Button viewTestResults = (Button) view.findViewById(R.id.viewTestResults);

        bookTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAppointmentFragment appointmentFrag = new UserAppointmentFragment();
                Bundle args = new Bundle();
                args.putString("appointmentType", "COVID-19 Test");
                appointmentFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), appointmentFrag, "appointmentFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        bookVaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAppointmentFragment appointmentFrag = new UserAppointmentFragment();
                Bundle args = new Bundle();
                args.putString("appointmentType", "COVID-19 Vaccination");
                appointmentFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), appointmentFrag, "appointmentFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        viewAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAppointmentInfoFragment appointmentInfoFrag = new UserAppointmentInfoFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), appointmentInfoFrag, "appointmentInfoFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        viewTestResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserTestResultFragment testResultFrag = new UserTestResultFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), testResultFrag, "testResultFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}