package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    public static final String apiKey = "33602bcefe2240809217c9510fa313e3";
    public static final String apiKey2 = "4ac3d974cd814bc5b69c66d10463823e";
    public boolean exceeded1 = false;

    public NewsApiClient newsApiClient;
    public ArrayList<NewsDataModel> newsArray;
    JSONObject newsJson;
    String jsonString;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news, container, false);

        TextView test1 = (TextView) view.findViewById(R.id.textView5);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://free-news.p.rapidapi.com/v1/search?q=Singapore%20Travel%20Restrictions&lang=en&page=1&page_size=5")
                .get()
                .addHeader("x-rapidapi-host", "free-news.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "c54b5bf9efmsh1ad73eb981e138ep1c2029jsnec5616f6b499")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonString = response.body().string();
                try {
                    newsJson = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                test1.setText(jsonString);
            }
        });




//        newsArray = new ArrayList<NewsDataModel>();
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.newsRecycler);

//        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), newsArray);
//        recyclerView.setAdapter(newsAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }
}