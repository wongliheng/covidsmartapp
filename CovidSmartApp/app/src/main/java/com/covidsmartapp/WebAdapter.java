package com.covidsmartapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WebAdapter extends RecyclerView.Adapter<WebAdapter.WebViewHolder> {

    private Context context;
    private String url;

    public WebAdapter (Context ct, String url) {
        context = ct;
        this.url = url;
    }

    @NonNull
    @Override
    public WebViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.web_layout, parent, false);
        return new WebViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WebViewHolder holder, int position) {
        holder.urlBar.setText(url);
        WebSettings settings = holder.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        holder.webView.setWebViewClient(new WebViewClient());
        holder.webView.loadUrl(url);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class WebViewHolder extends RecyclerView.ViewHolder{

        public TextView urlBar;
        public ProgressBar progressBar;
        public WebView webView;

        public WebViewHolder(@NonNull View itemView) {
            super(itemView);
            urlBar = itemView.findViewById(R.id.urlTextView);
            progressBar = itemView.findViewById(R.id.urlLoadingBar);
            webView = itemView.findViewById(R.id.webView);
        }
    }
}
