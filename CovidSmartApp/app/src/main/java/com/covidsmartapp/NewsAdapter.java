package com.covidsmartapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;
    ArrayList<NewsDataModel> news;

    public NewsAdapter (Context ct, ArrayList<NewsDataModel> newsArray) {
        context = ct;
        news = newsArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewsDataModel article = news.get(position);
        holder.headline.setText(article.getTitle());
//        holder.description.setText(article.getDescription());
//        Glide.with(this.context).load(article.getUrlToImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView headline, description;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.newsHeadline);
            description = itemView.findViewById(R.id.newsDescription);
            image = itemView.findViewById(R.id.newsImage);
        }
    }
}
