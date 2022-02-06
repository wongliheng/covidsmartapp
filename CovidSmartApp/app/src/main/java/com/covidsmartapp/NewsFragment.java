package com.covidsmartapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    public static final String apiKey = "33602bcefe2240809217c9510fa313e3";
    public static final String apiKey2 = "4ac3d974cd814bc5b69c66d10463823e";
    public boolean exceeded1 = false;

    NewsApiClient newsApiClient;
    ArrayList<NewsDataModel> newsArray;

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

        newsArray = new ArrayList<NewsDataModel>();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.newsRecycler);

        if (exceeded1){
            newsApiClient = new NewsApiClient(apiKey2);
        }
        else {
            newsApiClient = new NewsApiClient(apiKey);
        }

        TextView test1 = (TextView) view.findViewById(R.id.textView5);
        TextView test3 = (TextView) view.findViewById(R.id.textView7);
        Button testBtn = (Button) view.findViewById(R.id.reks);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("singapore travel restrictions")
                        .sortBy("publishedAt")
                        .pageSize(7)
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        test1.setText(response.getArticles().get(0).getTitle());


//                        for (int i = 0; i < response.getTotalResults(); i++){
//                            NewsDataModel news = new NewsDataModel();
////                            news.setSource(response.getArticles().get(i).getSource().getId());
//                            news.setTitle(response.getArticles().get(i).getTitle());
////                            news.setDescription(response.getArticles().get(i).getDescription());
////                            news.setUrl(response.getArticles().get(i).getUrl());
////                            news.setSource(response.getArticles().get(i).getUrlToImage());
//                            newsArray.add(news);
//                        }
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        exceeded1 = true;
                    }
                }
        );


//        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), newsArray);
//        recyclerView.setAdapter(newsAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }
}