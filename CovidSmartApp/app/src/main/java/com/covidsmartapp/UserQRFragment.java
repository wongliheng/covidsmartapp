package com.covidsmartapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class UserQRFragment extends Fragment {

    private CodeScanner qrCodeScanner;

    public UserQRFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_qr, container, false);
        CodeScannerView scannerView = (CodeScannerView) view.findViewById(R.id.scannerView);
        qrCodeScanner = new CodeScanner(getActivity(), scannerView);
        qrCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserCheckInFragment checkInFrag = new UserCheckInFragment();
                        Bundle args = new Bundle();
                        args.putString("location", result.getText());
                        checkInFrag.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(((ViewGroup)getView().getParent()).getId(), checkInFrag, "checkInFrag")
                                .commit();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCodeScanner.startPreview();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        qrCodeScanner.startPreview();
    }

    @Override
    public void onStop() {
        super.onStop();
        qrCodeScanner.releaseResources();
    }

    @Override
    public void onResume() {
        super.onResume();
        qrCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        qrCodeScanner.releaseResources();
        super.onPause();
    }
}