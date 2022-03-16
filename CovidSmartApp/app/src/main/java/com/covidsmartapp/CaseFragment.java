package com.covidsmartapp;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CaseFragment extends Fragment {

    private CovidDataModel data;
    private String country;
    public CaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
        WebView webView = (WebView) view.findViewById(R.id.webView);
        TextView urlText = (TextView) view.findViewById(R.id.urlTextView);
        ConstraintLayout webConstraint = (ConstraintLayout) view.findViewById(R.id.webConstraint);
        ImageButton close = (ImageButton) view.findViewById(R.id.closeButton);
        Button covidSitrepBtn = (Button) view.findViewById(R.id.covidSitrepBtn);

        SpinnerCreation createSpinner = new SpinnerCreation(getActivity());
        SearchableSpinner countrySpinner = (SearchableSpinner) view.findViewById(R.id.countrySpinner);
        countrySpinner = createSpinner.createCountrySpinner(countrySpinner);
//        countrySpinner.setSelection(181);

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

        covidSitrepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "covidsitrep.moh.gov.sg/";
                Intent defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
                defaultBrowser.setData(Uri.parse(data));
                startActivity(defaultBrowser);
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