package com.covidsmartapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private ArrayList<NewsDataModel> news;
    private NewsOnClick listener;

    public NewsAdapter (Context ct, ArrayList<NewsDataModel> newsArray, NewsOnClick l) {
        context = ct;
        news = newsArray;
        listener = l;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsDataModel article = news.get(position);
        holder.headline.setText(article.getTitle());
        holder.description.setText(article.getDescription());
        holder.source.setText(article.getSource());
//        holder.date.setText(article.getPublishDate());
//        Glide.with(this.context).load(article.getUrlToImage()).into(holder.image);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNewsClicked(news.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        public TextView headline, description, source;
//        public ImageView image;
        public CardView card;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.newsHeadline);
            description = itemView.findViewById(R.id.newsDescription);
            source = itemView.findViewById(R.id.newsSource);
//            image = itemView.findViewById(R.id.newsImage);
            card = itemView.findViewById(R.id.newsCard);
        }
    }
}
