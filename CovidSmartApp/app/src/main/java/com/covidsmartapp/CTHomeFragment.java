package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CTHomeFragment extends Fragment {

    public CTHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_t_home, container, false);

        Button searchByLocationBtn = (Button) view.findViewById(R.id.searchByLocationBtn);
        Button searchByUserBtn = (Button) view.findViewById(R.id.searchByUserBtn);

        searchByLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTSearchByLocationFragment ctSearchByLocationFragment = new CTSearchByLocationFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), ctSearchByLocationFragment, "ctSearchByLocationFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        searchByUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTSearchByUserFragment ctSearchByUserFragment = new CTSearchByUserFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), ctSearchByUserFragment, "ctSearchByUserFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}