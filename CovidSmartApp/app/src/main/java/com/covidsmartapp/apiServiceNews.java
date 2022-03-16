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

    public static final String newsQuery = "https://api.thenewsapi.com/v1/news/top?&limit=5&language=en";
    private static final String apiKey1 = "&api_token=PAaZYbeWoh6np5cCwe7iA32K1W4INGohhpVLUdmD";
    private static final String apiKey2 = "&api_token=gtF4oO3A6tYLGdMZTMZYcKJ5VC6ohv0TdjY9F8pS";
    private static final String apiKey3 = "&api_token=dODPGfgj8oL1YzDRyhpJ20scJ0H0fWMzfZFvJCMP";
    Context context;

    public apiServiceNews(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(ArrayList<NewsDataModel> newsArray);
    }

    public void getLocal (final VolleyResponseListener vrl) {
        String url = newsQuery + "&search=singapore+vtl" + apiKey1;
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
                            newsDataModel.setDescription(article.getString("description"));
                            newsDataModel.setUrl(article.getString("url"));
                            newsDataModel.setUrlToImage(article.getString("image_url"));
                            newsDataModel.setSource(article.getString("source"));
                            newsDataModel.setPublishDate(article.getString("published_at"));
                            newsArray.add(newsDataModel);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vrl.onResponse(newsArray);
                }, error -> vrl.onError("error"));

        SingletonClass.getInstance(context).addToRequestQueue(request);
    }

    public void getGlobal (final VolleyResponseListener vrl) {
        String url = newsQuery + "&search=covid+travel" + apiKey2;
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
                            newsDataModel.setDescription(article.getString("description"));
                            newsDataModel.setUrl(article.getString("url"));
                            newsDataModel.setUrlToImage(article.getString("image_url"));
                            newsDataModel.setSource(article.getString("source"));
                            newsDataModel.setPublishDate(article.getString("published_at"));
                            newsArray.add(newsDataModel);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vrl.onResponse(newsArray);
                }, error -> vrl.onError("error"));

        SingletonClass.getInstance(context).addToRequestQueue(request);
    }

    public void getVaccine (final VolleyResponseListener vrl) {
        String url = newsQuery + "&singapore=vaccine" + apiKey3;
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
                            newsDataModel.setDescription(article.getString("description"));
                            newsDataModel.setUrl(article.getString("url"));
                            newsDataModel.setUrlToImage(article.getString("image_url"));
                            newsDataModel.setSource(article.getString("source"));
                            newsDataModel.setPublishDate(article.getString("published_at"));
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

