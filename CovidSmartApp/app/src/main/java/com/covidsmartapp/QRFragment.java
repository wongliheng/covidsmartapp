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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CodeScanner qrCodeScanner;

    public QRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);
        CodeScannerView scannerView = (CodeScannerView) view.findViewById(R.id.scannerView);
        qrCodeScanner = new CodeScanner(getActivity(), scannerView);
        qrCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CheckInFragment checkInFrag = new CheckInFragment();
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