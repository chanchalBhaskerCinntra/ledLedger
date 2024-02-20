package com.cinntra.ledure.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.AddOrderAct;
import com.cinntra.ledure.activities.OrderActivity;
import com.cinntra.ledure.adapters.Open_Order_Adapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.QuotationItem;
import com.cinntra.ledure.viewModel.QuotationList_ViewModel;
import com.pixplicity.easyprefs.library.Prefs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Ledger_Open_Order_Customervice extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    int currentpage = 0;
    boolean recallApi = true;
    ArrayList<QuotationItem> AllItemList;
    LinearLayoutManager layoutManager;
    String cardCode;

    public Ledger_Open_Order_Customervice(String cardCode) {
        // Required empty public constructor
        this.cardCode = cardCode;
        Log.e("CardCodeT=>",cardCode);
    }



    Open_Order_Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
    ViewGroup container,
    Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View v=inflater.inflate(R.layout.fragment_open_order, container, false);
    ButterKnife.bind(this,v);
    AllItemList = new ArrayList<>();
    layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);

    if(Globals.checkInternet(getActivity())) {
        {
            loader.setVisibility(View.VISIBLE);
            callApi(loader);
        }

    }

        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(Globals.checkInternet(getActivity())){

                    callApi(loader);
                }
                else
                    swipeRefreshLayout.setRefreshing(false);

            }
        });

    return v;
    }



    private void callApi(ProgressBar loader)
          {
     loader.setVisibility(View.VISIBLE);
     QuotationList_ViewModel model = ViewModelProviders.of(this).get(QuotationList_ViewModel.class);
     model.getOrders(loader).observe(getActivity(), new Observer<List<QuotationItem>>() {
     @Override
     public void onChanged(@Nullable List<QuotationItem> itemsList) {



        AllItemList.clear();
         assert itemsList != null;
         AllItemList.addAll(itemsList);
        adapter = new Open_Order_Adapter(getContext(),AllItemList);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if(adapter!=null&&cardCode!="")
            adapter.filterCustomer(cardCode);


         if(itemsList.size()<=0)
             no_datafound.setVisibility(View.VISIBLE);
         else
             no_datafound.setVisibility(View.GONE);
         swipeRefreshLayout.setRefreshing(false);
    }


        });
    }



}