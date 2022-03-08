package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaseFragment extends Fragment {

    private CovidDataModel data;
    private String country;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CaseFragment newInstance(String param1, String param2) {
        CaseFragment fragment = new CaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_case, container, false);

        SpinnerCreation createSpinner = new SpinnerCreation(getActivity());
        SearchableSpinner countrySpinner = (SearchableSpinner) view.findViewById(R.id.countrySpinner1);
        countrySpinner = createSpinner.createSpinner(countrySpinner);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String countryString = adapterView.getItemAtPosition(i).toString();
                String noSpace = countryString.replace(" ", "%20");
                String noOpenBrackets = noSpace.replace("(", "%28");
                country = noOpenBrackets.replace(")", "%29");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    public void loadCases(String country, ProgressBar progressBar) {
        final apiServiceCases service = new apiServiceCases(getActivity());
        service.getCovidData(country, new apiServiceCases.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("TAG", message);
            }

            @Override
            public void onResponse(CovidDataModel covidDataModel) {


            }
        });
    }
}