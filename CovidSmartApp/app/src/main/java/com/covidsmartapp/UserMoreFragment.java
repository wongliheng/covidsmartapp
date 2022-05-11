package com.covidsmartapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

public class UserMoreFragment extends Fragment {

    private FirebaseAuth mAuth;

    public UserMoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_more, container, false);

        Button stayingSafeBtn = (Button) view.findViewById(R.id.stayingSafeBtn);
        Button safeTravellingBtn = (Button) view.findViewById(R.id.safeTravellingBtn);
        Button editProfileBtn = (Button) view.findViewById(R.id.editProfileBtn);
        Button deleteAccountBtn = (Button) view.findViewById(R.id.deleteAccountBtn);
        Button signOutBtn = (Button) view.findViewById(R.id.signOutBtn);

        stayingSafeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserCovidTipsFragment userCovidTipsFragment = new UserCovidTipsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), userCovidTipsFragment, "userCovidTipsFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        safeTravellingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSafeTravellingFragment userSafeTravellingFragment = new UserSafeTravellingFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), userSafeTravellingFragment, "userSafeTravellingFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserEditProfileFragment userEditProfileFragment = new UserEditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), userEditProfileFragment, "userEditProfileFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDeleteAccountFragment userDeleteAccountFragment = new UserDeleteAccountFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), userDeleteAccountFragment, "userDeleteAccountFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Sign out button
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}