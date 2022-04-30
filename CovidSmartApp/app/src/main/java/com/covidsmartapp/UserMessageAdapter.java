package com.covidsmartapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UserMessageAdapter extends FirestoreRecyclerAdapter<UserMessageClass, UserMessageAdapter.UserMessageHolder> {

    public UserMessageAdapter(@NonNull FirestoreRecyclerOptions<UserMessageClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserMessageHolder holder, int position, @NonNull UserMessageClass model) {
        holder.dateText.setText(model.getDate());
        holder.messageText.setText(model.getMessage());
    }

    @NonNull
    @Override
    public UserMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_message_layout, parent, false);
        return new UserMessageAdapter.UserMessageHolder(view);
    }

    public class UserMessageHolder extends RecyclerView.ViewHolder {

        TextView dateText, messageText;

        public UserMessageHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            messageText = itemView.findViewById(R.id.messageText);
        }
    }
}
