package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covidsmartapp.data.LoginDataSource;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

//

public class NewsFragment extends Fragment {

    private ArrayList<NewsDataModel> articleArray;

    private String newsType;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsType = getArguments().getString("newsType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.newsRecycler);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.newsProgressBar);
        WebView webView = (WebView) view.findViewById(R.id.newsWebView);
        TextView urlText = (TextView) view.findViewById(R.id.urlTextView);
        ConstraintLayout webConstraint = (ConstraintLayout) view.findViewById(R.id.webConstraint);
        ImageButton close = (ImageButton) view.findViewById(R.id.closeButton);

        TextView pageHeader = (TextView) view.findViewById(R.id.pageHeader);
        if (newsType.equals("local"))
            pageHeader.setText("Travel Restriction News In Singapore");
        else if (newsType.equals("global"))
            pageHeader.setText("Travel Restriction News Around The World");
        else if (newsType.equals("vaccine"))
            pageHeader.setText("Covid Vaccine News");

        loadNews(recyclerView, progressBar, webView, urlText, webConstraint, close, newsType);

        return view;
    }

    public void loadNews(RecyclerView recyclerView, ProgressBar progressBar, WebView webView,
                         TextView urlText, ConstraintLayout webConstraint, ImageButton close,
                         String newsType) {
        final apiServiceNews service = new apiServiceNews(getActivity());

        if (newsType.equals("local")){
            service.getLocal(new apiServiceNews.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Log.d("TAG", message);
                }

                @Override
                public void onResponse(ArrayList<NewsDataModel> newsArray) {
                    articleArray = newsArray;

                    NewsAdapter adapter = new NewsAdapter(getActivity(), articleArray, new NewsOnClick() {
                        @Override
                        public void onNewsClicked(NewsDataModel newsDataModel) {
                            urlText.setText(newsDataModel.getUrl());
                            WebSettings settings = webView.getSettings();
                            settings.setJavaScriptEnabled(true);
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(newsDataModel.getUrl());
                            webConstraint.setVisibility(View.VISIBLE);

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    webConstraint.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        else if (newsType.equals("global")) {
            service.getGlobal(new apiServiceNews.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Log.d("TAG", message);
                }

                @Override
                public void onResponse(ArrayList<NewsDataModel> newsArray) {
                    articleArray = newsArray;

                    NewsAdapter adapter = new NewsAdapter(getActivity(), articleArray, new NewsOnClick() {
                        @Override
                        public void onNewsClicked(NewsDataModel newsDataModel) {
                            urlText.setText(newsDataModel.getUrl());
                            WebSettings settings = webView.getSettings();
                            settings.setJavaScriptEnabled(true);
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(newsDataModel.getUrl());
                            webConstraint.setVisibility(View.VISIBLE);

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    webConstraint.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        else if (newsType.equals("vaccine")){
            service.getVaccine(new apiServiceNews.VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Log.d("TAG", message);
                }

                @Override
                public void onResponse(ArrayList<NewsDataModel> newsArray) {
                    articleArray = newsArray;

                    NewsAdapter adapter = new NewsAdapter(getActivity(), articleArray, new NewsOnClick() {
                        @Override
                        public void onNewsClicked(NewsDataModel newsDataModel) {
                            urlText.setText(newsDataModel.getUrl());
                            WebSettings settings = webView.getSettings();
                            settings.setJavaScriptEnabled(true);
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(newsDataModel.getUrl());
                            webConstraint.setVisibility(View.VISIBLE);

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    webConstraint.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }


}