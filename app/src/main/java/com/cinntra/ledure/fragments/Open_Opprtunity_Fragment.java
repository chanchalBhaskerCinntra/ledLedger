package com.cinntra.ledure.fragments;


import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
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
import com.cinntra.ledure.activities.AddOpportunityActivity;
import com.cinntra.ledure.activities.Opportunities_Pipeline_Activity;
import com.cinntra.ledure.adapters.OpenOpportunities_Adapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.FragmentRefresher;
import com.cinntra.ledure.newapimodel.NewOpportunityRespose;
import com.cinntra.ledure.viewModel.Opportunities_ViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Open_Opprtunity_Fragment extends Fragment implements FragmentRefresher {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;

    Context mContext;

    LinearLayoutManager layoutManager;
    ArrayList<NewOpportunityRespose> AllitemLIst;

    public Open_Opprtunity_Fragment() {
    }


    //TODO: Rename and change types and number of parameters
    public static Open_Opprtunity_Fragment newInstance(String param1, String param2) {
        Open_Opprtunity_Fragment fragment = new Open_Opprtunity_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_open_opp, container, false);
        ButterKnife.bind(this, v);
        mContext = getActivity();
//          getActivity().getActionBar().setTitle((getString(R.string.opportunities)));
        AllitemLIst = new ArrayList<>();

        //eventSearchManager();
        if (Globals.checkInternet(getActivity())) {
            loader.setVisibility(View.VISIBLE);
            callApi(loader);
        }


        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Globals.checkInternet(getActivity()))
                    callApi(loader);
                else
                    swipeRefreshLayout.setRefreshing(false);
            }
        });


        return v;
    }


    OpenOpportunities_Adapter adapter;

    private void callApi(ProgressBar loader) {
        Opportunities_ViewModel model = ViewModelProviders.of(this).get(Opportunities_ViewModel.class);
        model.newOpprtunities(loader).observe(getActivity(), new Observer<List<NewOpportunityRespose>>() {
            @Override
            public void onChanged(@Nullable List<NewOpportunityRespose> itemsList) {
                AllitemLIst.clear();
                AllitemLIst.addAll(itemsList);
                swipeRefreshLayout.setRefreshing(false);
                layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                adapter = new OpenOpportunities_Adapter(Open_Opprtunity_Fragment.this, getContext(), AllitemLIst);
                recyclerview.setLayoutManager(layoutManager);
                recyclerview.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                if (itemsList.size() <= 0)
                    no_datafound.setVisibility(View.VISIBLE);
                else
                    no_datafound.setVisibility(View.GONE);
            }

        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //  menu.clear();
        inflater.inflate(R.menu.filteroption_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.search);
        MenuItem itemPlus = menu.findItem(R.id.plus);
        itemPlus.setVisible(false);
        SearchView searchView = new SearchView(((Opportunities_Pipeline_Activity) mContext).getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search Opportunity");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null && newText.trim().length() > 0)
                    adapter.filter(newText.trim());
                return true;
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.plus:
                startActivity(new Intent(getContext(), AddOpportunityActivity.class));
                break;
            case R.id.all:
                if (adapter != null)
                    adapter.AllData();

                break;
            case R.id.my:

                break;
            case R.id.my_team:
                if (adapter != null)
                    adapter.Favfilter("Y");
                break;


        }
        return true;
    }

    @Override
    public void onRefresh() {
        if (Globals.checkInternet(getActivity())) {
            loader.setVisibility(View.VISIBLE);
            callApi(loader);
        }
    }
}