//package com.covidsmartapp;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//
//import org.w3c.dom.Text;
//
//import java.util.ArrayList;
//
//public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.CaseViewHolder> {
//
//    private Context context;
//    private CovidDataModel data;
//
//    public CaseAdapter (Context ct, CovidDataModel data) {
//        context = ct;
//        this.data = data;
//    }
//
//    @NonNull
//    @Override
//    public CaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.case_layout, parent, false);
//        return new CaseViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CaseViewHolder holder, int position) {
////        holder.headline.setText(data.getActive());
////        holder.description.setText(data.getCases());
////        Glide.with(this.context).load(article.getUrlToImage()).into(holder.image);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 1;
//    }
//
//    public class CaseViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView headline, description;
//        public ImageView image;
//        public ConstraintLayout container;
//
//        public CaseViewHolder(@NonNull View itemView) {
//            super(itemView);
//            headline = itemView.findViewById(R.id.newsHeadline);
//            description = itemView.findViewById(R.id.newsDescription);
//            image = itemView.findViewById(R.id.newsImage);
//            container = itemView.findViewById(R.id.newsContainer);
//        }
//    }
//}
