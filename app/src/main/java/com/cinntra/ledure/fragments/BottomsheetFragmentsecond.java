package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cinntra.ledure.R;

import butterknife.ButterKnife;


public class BottomsheetFragmentsecond extends Fragment {



    public BottomsheetFragmentsecond() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BottomsheetFragmentsecond newInstance(String param1, String param2) {
        BottomsheetFragmentsecond fragment = new BottomsheetFragmentsecond();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
           ViewGroup container,
           Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this,v);
        return v;
    }
}