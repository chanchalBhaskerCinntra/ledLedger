package com.cinntra.ledure.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.BottomsheetRecyclerviewAdapter;
import com.cinntra.ledure.databinding.FragmentMoreViewsFragmnetBinding;
import com.cinntra.ledure.globals.Globals;
import com.kingfisher.easyviewindicator.GridLayoutSnapHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class MoreViewsFragmnet extends Fragment {

    FragmentMoreViewsFragmnetBinding binding;
    BottomsheetRecyclerviewAdapter adapter;
    ArrayList<Integer> iconlist = new ArrayList<>();
    ArrayList<String> namelist = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_views_fragmnet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMoreViewsFragmnetBinding.bind(view);

        Globals.CURRENT_CLASS = getClass().getName();


        iconlist.add(R.drawable.ic_party_dashboard_deselected);
        iconlist.add(R.drawable.ic_quotation);
        iconlist.add(R.drawable.ic_customer_more);
        iconlist.add(R.drawable.ic_order);
        iconlist.add(R.drawable.ic_handshake_part);
        iconlist.add(R.drawable.ic_invoice_more);
        iconlist.add(R.drawable.campaign);
        iconlist.add(R.drawable.ic_delivery_more);
        iconlist.add(R.drawable.ic_inventory_more);
        iconlist.add(R.drawable.ic_ledger_icon);
        iconlist.add(R.drawable.ic_cash_discount);
        iconlist.add(R.drawable.ic_location_more);
        iconlist.add(R.drawable.ic_rupee_symbol_white);
        iconlist.add(R.drawable.ic_calender);




        namelist.add("Leads");
        namelist.add("Quotation");
        namelist.add("Customer");
        namelist.add("Order");
        namelist.add("Opportunity");
        namelist.add("Invoice");
        namelist.add("Campaign");
        namelist.add("Delivery");
        namelist.add("Inventory");
        namelist.add("Ledger");
        namelist.add("Cash Discount");
        namelist.add("Location");
        namelist.add("Expenses");
        namelist.add("Calender");


        adapter = new BottomsheetRecyclerviewAdapter(requireContext(), iconlist, namelist);
        binding.rvMoreItems.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        binding.rvMoreItems.setHasFixedSize(true);
       // binding.rvMoreItems.setNestedScrollingEnabled(false);
        binding.rvMoreItems.setAdapter(adapter);
        GridLayoutSnapHelper gridLayoutSnapHelper = new GridLayoutSnapHelper(6);
        gridLayoutSnapHelper.attachToRecyclerView(binding.rvMoreItems);

        adapter.notifyDataSetChanged();

    }
}