package com.covidsmartapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserEditProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;

    private AlertDialog fNameDialog;
    private AlertDialog lNameDialog;
    private AlertDialog phoneNumDialog;

    public UserEditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_edit_profile, container, false);

        TextView fNameText = (TextView) view.findViewById(R.id.fNameText);
        TextView lNameText = (TextView) view.findViewById(R.id.lNameText);
        TextView phoneNumText = (TextView) view.findViewById(R.id.phoneNumText);

        ImageView fNameEditImage = (ImageView) view.findViewById(R.id.fNameEditImage);
        ImageView lNameEditImage = (ImageView) view.findViewById(R.id.lNameEditImage);
        ImageView phoneNumEditImage = (ImageView) view.findViewById(R.id.phoneNumEditImage);

        DocumentReference ref = db.collection("users")
                .document(userID);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("DEBUG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    fNameText.setText(snapshot.getString("fName"));
                    lNameText.setText(snapshot.getString("lName"));
                    phoneNumText.setText(snapshot.getString("phoneNum"));
                } else {
                    Log.d("DEBUG", "Current data: null");
                }
            }
        });

        LinearLayout fNameLayout = new LinearLayout(getActivity());
        fNameLayout.setOrientation(LinearLayout.VERTICAL);
        fNameLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        EditText fNameInput = new EditText(getActivity());
        fNameInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        fNameInput.setMaxLines(1);
        fNameLayout.setPadding(200, 0, 200, 0);
        fNameLayout.addView(fNameInput);

        fNameEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fNameDialog != null && !fNameDialog.isShowing()) {
                    fNameDialog.show();
                }
                else {
                    fNameDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Update your particulars")
                            .setMessage("Enter your first name")
                            .setView(fNameLayout)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String fName = fNameInput.getText().toString();
                                    db.collection("users")
                                            .document(userID)
                                            .update("fName", fName);
                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });

        LinearLayout lNameLayout = new LinearLayout(getActivity());
        lNameLayout.setOrientation(LinearLayout.VERTICAL);
        lNameLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        EditText lNameInput = new EditText(getActivity());
        lNameInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        lNameInput.setMaxLines(1);
        lNameLayout.setPadding(200, 0, 200, 0);
        lNameLayout.addView(lNameInput);

        lNameEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lNameDialog != null && !lNameDialog.isShowing()) {
                    lNameDialog.show();
                }
                else {
                    lNameDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Update your particulars")
                            .setMessage("Enter your last name")
                            .setView(lNameLayout)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String lName = lNameInput.getText().toString();
                                    db.collection("users")
                                            .document(userID)
                                            .update("lName", lName);
                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });

        LinearLayout numberLayout = new LinearLayout(getActivity());
        numberLayout.setOrientation(LinearLayout.VERTICAL);
        numberLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        EditText numberInput = new EditText(getActivity());
        numberInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        numberInput.setMaxLines(1);
        numberLayout.setPadding(200, 0, 200, 0);
        numberLayout.addView(numberInput);

        phoneNumEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNumDialog != null && !phoneNumDialog.isShowing()) {
                    phoneNumDialog.show();
                }
                else {
                    phoneNumDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Update your particulars")
                            .setMessage("Enter your phone number")
                            .setView(numberLayout)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String phoneNum = numberInput.getText().toString();
                                    db.collection("users")
                                            .document(userID)
                                            .update("phoneNum", phoneNum);
                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            }
        });

        return view;
    }
}