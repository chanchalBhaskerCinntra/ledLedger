package com.cinntra.ledure.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CampaignListAdapter;
import com.cinntra.ledure.adapters.MemberListAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.CampaignListModel;
import com.cinntra.ledure.model.CampaignListResponse;
import com.cinntra.ledure.model.CampaignModel;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberListFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.listing)
    RecyclerView listing;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.mainHeaderLay)
    RelativeLayout mainHeaderLay;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.filterView)
    RelativeLayout filterView;
    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;



    CampaignModel campaignData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            campaignData =(CampaignModel) b.getSerializable(Globals.CampaignData);

        }
    }

    MemberListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v=inflater.inflate(R.layout.member_listing, container, false);
        ButterKnife.bind(this,v);
        eventSearchManager();
        adapter=   new MemberListAdapter(getContext(),campaignData.getMemberList());
        listing.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        listing.setAdapter(adapter);

     if(Globals.checkInternet(getContext()))
          {
   //callApi(campaignData.getId());
          }


        return v;
    }


    private void eventSearchManager()
    {
        head_title.setText("Campaign");
        back_press.setOnClickListener(this);
        search.setOnClickListener(this);
        filterView.setVisibility(View.GONE);
        new_quatos.setVisibility(View.GONE);
        searchView.setBackgroundColor(Color.parseColor("#00000000"));
        searchLay.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
           {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
            if(adapter!=null)
                {
               adapter.filter(query);
                }
           return true;
               }
            @Override
            public boolean onQueryTextChange(String newText)
            {
          return true;
            }
        });

    }


    private void callApi(Integer id)
       {

        CampaignListResponse cm = new CampaignListResponse();
        cm.setCampaignSetId(id);

        Call<CampaignListModel> call = NewApiClient.getInstance().getApiService().getmemberlist(cm);
        call.enqueue(new Callback<CampaignListModel>() {
            @Override
            public void onResponse(Call<CampaignListModel> call, Response<CampaignListModel> response) {

                if(response.code()==200)
                {
                    listing.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    listing.setAdapter(new CampaignListAdapter(getContext(),response.body().getData()));
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, LeadResponse.class);
                        Toast.makeText(getContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }

            }
            @Override
            public void onFailure(Call<CampaignListModel> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.search:
                mainHeaderLay.setVisibility(View.GONE);
                searchLay.setVisibility(View.VISIBLE);

                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                break;
            case R.id.back_press:
                requireActivity().onBackPressed();
                break;


        }

    }


}
