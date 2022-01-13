package com.covidsmartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        SearchableSpinner countrySpinner = (SearchableSpinner) findViewById(R.id.countrySpinner);
        countrySpinner.setTitle("Select Country");
        countrySpinner.setPositiveButton("OK");

        //Country list
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale l : locales) {
            String country = l.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter<String> countryA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
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



        Button getCases = (Button) findViewById(R.id.getCases);
        getCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(RealtimePage.this, country, Toast.LENGTH_SHORT).show();

                final TextView textView = (TextView) findViewById(R.id.caseStat);

// Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(RealtimePage.this);
                String url ="https://disease.sh/v3/covid-19/countries/" + country;

// Request a string response from the provided URL.
                JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    textView.setText("total cases = " + response.getString("cases")
                                    + "\ntoday cases= " + response.getString("todayCases"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                queue.add(arrayRequest);
            }
        });
    }

}