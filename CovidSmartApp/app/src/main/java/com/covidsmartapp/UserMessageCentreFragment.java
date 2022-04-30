package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class UserMessageCentreFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
    private UserMessageAdapter adapter;

    public UserMessageCentreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_message_centre, container, false);

        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        TextView noMessages = (TextView) view.findViewById(R.id.noMessages);

        createMessageRecycler(recyclerView, noMessages);
        markMessagesAsRead();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void markMessagesAsRead() {
        db.collection("info")
                .document(userID)
                .collection("messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String status = document.getString("status");
                                if (status.equals("unread")) {
                                    document.getReference().update("status", "read");
                                }
                            }
                        }
                    }
                });
    }

    private void createMessageRecycler(RecyclerView recyclerView, TextView noMessages) {
        CollectionReference ref = db.collection("info")
                .document(userID)
                .collection("messages");

        Query query = ref.orderBy("dateTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<UserMessageClass> options = new FirestoreRecyclerOptions.Builder<UserMessageClass>()
                .setQuery(query, UserMessageClass.class)
                .build();

        adapter = new UserMessageAdapter(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    noMessages.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noMessages.setVisibility(View.GONE);
                }
            }
        };
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}