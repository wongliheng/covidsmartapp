package com.covidsmartapp;

import android.media.Image;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Text;

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

        ImageView flagImage = (ImageView) view.findViewById(R.id.flagImage);
        TextView countryName = (TextView) view.findViewById(R.id.countryName);
        TextView activeCases = (TextView) view.findViewById(R.id.activeCases);
        TextView totalCases = (TextView) view.findViewById(R.id.totalCases);
        TextView critical = (TextView) view.findViewById(R.id.critical);
        TextView deaths = (TextView) view.findViewById(R.id.deaths);
        TextView blocker = (TextView) view.findViewById(R.id.blocker);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.caseProgressBar);

        SpinnerCreation createSpinner = new SpinnerCreation(getActivity());
        SearchableSpinner countrySpinner = (SearchableSpinner) view.findViewById(R.id.countrySpinner1);
        countrySpinner = createSpinner.createSpinner(countrySpinner);
        countrySpinner.setSelection(181);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String countryString = adapterView.getItemAtPosition(i).toString();
                String noSpace = countryString.replace(" ", "%20");
                String noOpenBrackets = noSpace.replace("(", "%28");
                country = noOpenBrackets.replace(")", "%29");

                loadCases(country, flagImage, countryName, activeCases, totalCases, critical, deaths, blocker, progressBar);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadCases("sg", flagImage, countryName, activeCases, totalCases, critical, deaths, blocker, progressBar);

        return view;
    }

    public void loadCases(String country, ImageView flagImage, TextView countryName, TextView activeCases,
                          TextView totalCases, TextView critical, TextView deaths, TextView blocker,
                          ProgressBar progressBar) {
        final apiServiceCases service = new apiServiceCases(getActivity());
        progressBar.setVisibility(View.VISIBLE);
        service.getCovidData(country, new apiServiceCases.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("TAG", message);
            }

            @Override
            public void onResponse(CovidDataModel covidDataModel) {
                Glide.with(getActivity()).load(covidDataModel.getFlag()).into(flagImage);
                countryName.setText(covidDataModel.getCountry());
                activeCases.setText(covidDataModel.getActive());
                totalCases.setText(covidDataModel.getCases());
                critical.setText(covidDataModel.getCritical());
                deaths.setText(covidDataModel.getDeaths());

                blocker.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}