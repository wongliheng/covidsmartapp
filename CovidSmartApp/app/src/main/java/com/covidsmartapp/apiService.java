package com.covidsmartapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class apiService {

    public static final String covidQuery = "https://disease.sh/v3/covid-19/countries/";
    public static final String strictFalse = "?strict=false";
    Context context;

    public apiService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError (String message);

        void onResponse (CovidDataModel covidDataModel);

//        void onImage (ImageLoader imageLoader, String url);
    }

    public void getCovidData (String country, final VolleyResponseListener vrl) {
        String url = covidQuery + country + strictFalse;
        CovidDataModel covidDataModel = new CovidDataModel();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        covidDataModel.setCases(response.getInt("cases"));
                        covidDataModel.setDeaths(response.getInt("deaths"));
                        covidDataModel.setActive(response.getInt("active"));
                        covidDataModel.setCritical(response.getInt("critical"));
                        covidDataModel.setCountry(response.getString("country"));
                        JSONObject countryInfo = response.getJSONObject("countryInfo");
                        covidDataModel.setFlag(countryInfo.getString("flag"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vrl.onResponse(covidDataModel);
//                    ImageLoader imageLoader = SingletonClass.getInstance(context).getImageLoader();
//                    vrl.onImage(imageLoader, covidDataModel.getFlag());
                }, error -> vrl.onError("error"));

        SingletonClass.getInstance(context).addToRequestQueue(request);
    }
}

