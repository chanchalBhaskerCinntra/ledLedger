package com.cinntra.ledure.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.BottomSheetAdapter;
import com.cinntra.ledure.adapters.BottomsheetRecyclerviewAdapter;
import com.cinntra.ledure.globals.Globals;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kingfisher.easyviewindicator.AnyViewIndicator;
import com.kingfisher.easyviewindicator.GridLayoutSnapHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewpagerBottomSheet extends BottomSheetDialogFragment implements BottomSheetAdapter.ItemListener {
    Context context;
    /*@BindView(R.id.database_listing)
    RecyclerView database_listing;*/
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.anyViewIndicator2)
    AnyViewIndicator gridindicator;
    /*@BindView(R.id.dot1)
    DotsIndicator dot1;*/

        BottomsheetRecyclerviewAdapter adapter;
    ArrayList<Integer> iconlist = new ArrayList<>();
    ArrayList<String> namelist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recyclerview_bottomsheet, container, false);
        Globals.CURRENT_CLASS = getClass().getName();
        ButterKnife.bind(this,v);
       // fragmentRefresher =(FragmentRefresher)getActivity();
        context = getContext();
        iconlist.add(R.drawable.ic_person);
        iconlist.add(R.drawable.ic_quotation);
        iconlist.add(R.drawable.customer);
        iconlist.add(R.drawable.ic_order);
        iconlist.add(R.drawable.ic_opportunity);
        iconlist.add(R.drawable.personman);
        iconlist.add(R.drawable.ic_invoice);
        iconlist.add(R.drawable.campaign);
        iconlist.add(R.drawable.ic_delivery_open);
        iconlist.add(R.drawable.ic_inventory);
        iconlist.add(R.drawable.customer);
        iconlist.add(R.drawable.ic_order);
        iconlist.add(R.drawable.ic_location_icon);
        iconlist.add(R.drawable.ic_rupee_symbol_white);


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
        gridindicator.setItemCount(2);
        gridindicator.setCurrentPosition(0);
        adapter = new BottomsheetRecyclerviewAdapter(context,iconlist,namelist);
       recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false));
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(adapter);
        GridLayoutSnapHelper gridLayoutSnapHelper = new GridLayoutSnapHelper(6);
        gridLayoutSnapHelper.attachToRecyclerView(recyclerview);

        adapter.notifyDataSetChanged();

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        gridindicator.setCurrentPosition((int) (Math.ceil(Double.valueOf(position)/ 6) - 1));
                        break;
                }
            }
        });


        return v;
    }


    @Override
    public void onClickAt(int position) {
      /*  Prefs.putString(Globals.SalesEmployeeCode,Dashboard.teamList_Hearchi.get(position).getSalesEmployeeCode());
        Prefs.putString(Globals.SalesEmployeeName,Dashboard.teamList_Hearchi.get(position).getSalesEmployeeName());

        Globals.TeamEmployeeID = Dashboard.teamList_Hearchi.get(position).getId().toString();
        Prefs.putString(Globals.Role,Dashboard.teamList_Hearchi.get(position).getRole());
        changeTeam.changeTeam(Dashboard.teamList_Hearchi.get(position).getSalesEmployeeName());
        fragmentRefresher.onRefresh();*/
        dismiss();
    }




}