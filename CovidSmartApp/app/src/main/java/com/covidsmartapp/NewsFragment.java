package com.covidsmartapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.ref.WeakReference;
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

    ArrayList<NewsDataModel> articleArray;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable parcel = Parcels.wrap(articleArray);
        outState.putParcelable("newsData", parcel);
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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.newsRecycler);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.newsProgressBar);
        WebView webView = (WebView) view.findViewById(R.id.newsWebView);
        TextView urlText = (TextView) view.findViewById(R.id.urlTextView);
        ConstraintLayout webConstraint = (ConstraintLayout) view.findViewById(R.id.webConstraint);
        ImageButton close = (ImageButton) view.findViewById(R.id.closeButton);

        loadNews(recyclerView, progressBar, webView, urlText, webConstraint, close);



        return view;
    }
    public void loadNews(RecyclerView recyclerView, ProgressBar progressBar, WebView webView,
                         TextView urlText, ConstraintLayout webConstraint, ImageButton close) {
        final apiServiceNews service = new apiServiceNews(getActivity());
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


//                        WebFragment webFrag = new WebFragment();
//                        Bundle args = new Bundle();
//                        args.putString("url", newsDataModel.getUrl());
//                        webFrag.setArguments(args);
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(((ViewGroup)getView().getParent()).getId(), webFrag, "newsFrag")
//                                .addToBackStack("newsFrag")
//                                .commit();
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}