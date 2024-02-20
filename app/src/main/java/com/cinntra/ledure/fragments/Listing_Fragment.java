package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Listing_Fragment extends Fragment {

   @BindView(R.id.listing)
    RecyclerView listing;

    public Listing_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Listing_Fragment newInstance(String param1, String param2) {
        Listing_Fragment fragment = new Listing_Fragment();
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
    public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_listing, container, false);
        ButterKnife.bind(this,v);

        ListAdapter adapter = new ListAdapter(getActivity());
        listing.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        listing.setAdapter(adapter);

        return v;
    }
}