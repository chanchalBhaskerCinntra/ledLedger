package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.PaymentCollectionAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentCollection extends Fragment implements  View.OnClickListener {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.add)
    ImageView add;
    private PaymentCollectionAdapter paymentCollectionAdapter;

    @Override
    public void onResume() {
        super.onResume();
      //  if((AppCompatActivity) getActivity()!=null)
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {


        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.delivery_act, container, false);
        ButterKnife.bind(this,v);

        add.setVisibility(View.GONE);
        head_title.setText(getResources().getString(R.string.collect_payment));
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerview.setAdapter(new PaymentCollectionAdapter(getContext()));
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_press:
                getActivity().onBackPressed();
                break;
        }
    }
}
