package com.covidsmartapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class apiServiceNews {

    public static final String newsQuery = "https://api.thenewsapi.com/v1/news/top?api_token=PAaZYbeWoh6np5cCwe7iA32K1W4INGohhpVLUdmD&limit=5&language=en";
    Context context;

    public apiServiceNews(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(ArrayList<NewsDataModel> newsArray);
    }

    public void getLocal (final VolleyResponseListener vrl) {
        String url = newsQuery + "&search=singapore+travel+restrictions";
//        NewsDataModel newsDataModel = new NewsDataModel();
        ArrayList<NewsDataModel> newsArray = new ArrayList<NewsDataModel>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject meta = response.getJSONObject("meta");
                        String returned = meta.getString("returned");
                        int total = Integer.parseInt(returned);

                        JSONArray articleArray = response.getJSONArray("data");
                        for (int i = 0; i < total; i++) {
                            NewsDataModel newsDataModel = new NewsDataModel();
                            JSONObject article = articleArray.getJSONObject(i);
                            newsDataModel.setTitle(article.getString("title"));
                            newsArray.add(newsDataModel);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vrl.onResponse(newsArray);
                }, error -> vrl.onError("error"));

        SingletonClass.getInstance(context).addToRequestQueue(request);
    }
}

