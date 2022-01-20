package com.covidsmartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;



public class RealtimePage extends AppCompatActivity {

    String country;

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

        final apiService service = new apiService(RealtimePage.this);

        Button getCases = findViewById(R.id.getCases);
        TextView textView = findViewById(R.id.caseStat);

//        NetworkImageView testImage = findViewById(R.id.networkimagetest);
        ImageView glideTest = findViewById(R.id.glidetest);

        getCases.setOnClickListener(view -> service.getCovidData(country, new apiService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                textView.setText(message);
            }

            @Override
            public void onResponse(CovidDataModel covidDataModel) {
                textView.setText(covidDataModel.toString());
                Glide.with(RealtimePage.this).load(covidDataModel.getFlag()).into(glideTest);
            }

//            @Override
//            public void onImage(ImageLoader imageLoader, String url) {
////                testImage.setDefaultImageResId(R.drawable.ic_launcher_background);
////                testImage.setImageUrl(url, imageLoader);
//            }


        }));
    }

}