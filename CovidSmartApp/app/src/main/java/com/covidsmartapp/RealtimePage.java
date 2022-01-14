package com.covidsmartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class RealtimePage extends AppCompatActivity {

    String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_page);

        //Country spinner
        SearchableSpinner countrySpinner = findViewById(R.id.countrySpinner);
        countrySpinner.setTitle("Select Country");
        countrySpinner.setPositiveButton("OK");

        //Country list
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        for (Locale l : locales) {
            String country = l.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter<String> countryA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        countryA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryA);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final apiService service = new apiService(RealtimePage.this);

        Button getCases = findViewById(R.id.getCases);
        TextView textView = findViewById(R.id.caseStat);
        NetworkImageView testImage = findViewById(R.id.networkimagetest);

        getCases.setOnClickListener(view -> service.getCovidData(country, new apiService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                textView.setText(message);
            }

            @Override
            public void onResponse(CovidDataModel covidDataModel) {
                textView.setText(covidDataModel.toString());
            }

            @Override
            public void onImage(ImageLoader imageLoader, String url) {
                testImage.setDefaultImageResId(R.drawable.ic_launcher_background);
                testImage.setImageUrl(url, imageLoader);
            }
        }));
    }

}