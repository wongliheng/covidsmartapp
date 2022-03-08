package com.covidsmartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;



public class RealtimePage extends AppCompatActivity {

    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_page);

        //Country spinner
        SpinnerCreation createSpinner = new SpinnerCreation(RealtimePage.this);
        SearchableSpinner countrySpinner = findViewById(R.id.countrySpinner);
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



        Button getCases = findViewById(R.id.getCases);
        TextView textView = findViewById(R.id.caseStat);

        ImageView glideTest = findViewById(R.id.glidetest);

        final apiServiceCases service = new apiServiceCases(RealtimePage.this);
        getCases.setOnClickListener(view -> service.getCovidData(country, new apiServiceCases.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                textView.setText(message);
            }

            @Override
            public void onResponse(CovidDataModel covidDataModel) {
                textView.setText(covidDataModel.toString());
                Glide.with(RealtimePage.this).load(covidDataModel.getFlag()).into(glideTest);
            }
        }));
    }

}