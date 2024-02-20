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
import com.cinntra.ledure.activities.AddQuotationAct;
import com.cinntra.ledure.activities.QuotationActivity;
import com.cinntra.ledure.adapters.Quotation_Adapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.FragmentRefresher;
import com.cinntra.ledure.model.QuotationItem;
import com.cinntra.ledure.viewModel.QuotationList_ViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class Quotation_Open_Fragment extends Fragment implements View.OnClickListener, FragmentRefresher {


  @BindView(R.id.recyclerview)
  RecyclerView recyclerview;
  @BindView(R.id.loader)
  ProgressBar loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
  private SearchView searchView;
  private  Quotation_Adapter adapter;
   LinearLayoutManager layoutManager;
    int currentPage = 0;
//    private boolean isLastPage = false;
   ArrayList<QuotationItem> AllItemList;
    private boolean isLoading = false;
    boolean recallApi = true;
    private Context mContext;

   public Quotation_Open_Fragment() {
    //Required empty public constructor

       }


  /*  // TODO: Rename and change types and number of parameters
    public static Quotation_Open_Fragment newInstance(String param1, String param2) {
      Quotation_Open_Fragment fragment = new Quotation_Open_Fragment(oppdata);
      Bundle args = new Bundle();

      fragment.setArguments(args);
      return fragment;
        }*/


    @Override
    public void onResume() {
        super.onResume();
        if(Globals.checkInternet(getActivity())) {
            loader.setVisibility(View.VISIBLE);
            callApi(loader);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
     //Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_quotes_list, container, false);
    ButterKnife.bind(this,v);
     mContext = getActivity();
     AllItemList = new ArrayList<>();



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





    @Override
    public void onClick(View v) {
        Fragment   fragment = null;
    switch(v.getId())
           {
        /*  case R.id.new_quatos:
          fragment = new New_Quotation();
        FragmentManager fm       = getFragmentManager();
        FragmentTransaction transaction  = fm.beginTransaction();
       //FragmentTransaction transaction =  ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.quatoes_main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
           break;*/



              }


    }


    /***********  API Calling ************/

    private void callApi(ProgressBar loader)
                {
        QuotationList_ViewModel model = ViewModelProviders.of(this).get(QuotationList_ViewModel.class);
       model.getQutotation(loader).observe(getActivity(), new Observer<List<QuotationItem>>() {
            @Override
            public void onChanged(@Nullable List<QuotationItem> itemsList) {


                AllItemList.clear();
                assert itemsList != null;
                AllItemList.addAll(itemsList);
                layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
                adapter = new Quotation_Adapter(Quotation_Open_Fragment.this,getContext(),AllItemList);
                recyclerview.setLayoutManager(layoutManager);
                recyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if(AllItemList.size()==0)
                    no_datafound.setVisibility(View.VISIBLE);
                else
                    no_datafound.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }



        });

    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {


        //menu.clear();
        inflater.inflate(R.menu.quotation_filter, menu);

        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.search);
        MenuItem itemPlus = menu.findItem(R.id.plus);
        itemPlus.setVisible(false);
        SearchView searchView = new SearchView(((QuotationActivity) mContext).getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search Quotation");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                if(adapter!=null)
                    adapter.filter(newText);
                return true;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.plus:

                Intent i = new Intent(getContext(), AddQuotationAct.class);
                startActivity(i);
                break;
            case R.id.all:
                if(adapter!=null)
                    adapter.AllData();
                break;
            case R.id.newest:
                LocalDate date = LocalDate.parse(Globals.curntDate);
                LocalDate dateafter = date.minusDays(8);
                adapter.PostingDate(dateafter, date);
                break;
            case R.id.oldest:
//                        Date date1 = sdf.parse(date);
//                String curntDate = Globals.getTodaysDate();
                break;
            case R.id.my:
                if(adapter!=null)
                    adapter.Favfilter("Y");
                break;
            case  R.id.valid:
                if(adapter!=null)
                    adapter.Customerfilter();
                break;
            case R.id.posting:

                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        loader.setVisibility(View.VISIBLE);
        callApi(loader);
    }
}