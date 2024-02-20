package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CustomersReportAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.Customers_Report;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Ledger_Group_Fragment extends Fragment  {


    ArrayList<Customers_Report> customerList = new ArrayList<>();
    @BindView(R.id.customers_recyclerview)
    RecyclerView customerRecyclerView;

    String orderBYName = "";
    String orderBYAmt = "";



    public Ledger_Group_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Ledger_Group_Fragment newInstance(String param1, String param2) {
        Ledger_Group_Fragment fragment = new Ledger_Group_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            customerList = (ArrayList<Customers_Report>) b.getSerializable(Globals.LedgerCompanyData);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.ledger_item_layout, container, false);
        ButterKnife.bind(this,v);
        Toast.makeText(requireContext(), "Ledger Group", Toast.LENGTH_SHORT).show();
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CustomersReportAdapter adapter =new CustomersReportAdapter(getContext(),customerList);
        customerRecyclerView.setAdapter(adapter);

    }


}