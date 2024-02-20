package com.cinntra.ledure.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.AddBPCustomer;
import com.cinntra.ledure.activities.BussinessPartners;
import com.cinntra.ledure.adapters.CustomersAdapter;
import com.cinntra.ledure.adapters.CustomersAdapterDetals;
import com.cinntra.ledure.globals.Globals;

import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.viewModel.CustomerViewModel;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomersFragment extends Fragment {
    int currentPage = 0;
    @BindView(R.id.customer_lead_List)
    RecyclerView customer_lead_List;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    private SearchView searchView;
    boolean recallApi = true;
    LinearLayoutManager layoutManager;
    private Context mContext;

    public CustomersFragment() {
        // Required empty public constructor
    }

    ArrayList<BusinessPartnerData> AllitemsList;

    // TODO: Rename and change types and number of parameters
    public static CustomersFragment newInstance(String param1, String param2) {
        CustomersFragment fragment = new CustomersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_lead, container, false);
        ButterKnife.bind(this, v);
        mContext = getActivity();
        AllitemsList = new ArrayList<>();


        if (Globals.checkInternet(getActivity())) {
            loader.setVisibility(View.VISIBLE);
            if (Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("AddOrder") || Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("Quotation") ) {
                callzoneBpApi(loader);
            } else {
                callApi(loader);

            }


        }

        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(getActivity())) {

                    if (Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("AddOrder")|| Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("Quotation")) {
                        callzoneBpApi(loader);
                    } else {
                        callApi(loader);

                    }

                    //callApi(loader);




                     /* FragmentTransaction ft = getFragmentManager().beginTransaction();
                      if (Build.VERSION.SDK_INT >= 26) {
                          ft.setReorderingAllowed(false);
                      }
                      ft.detach(CustomersFragment.this).attach(CustomersFragment.this).commit();*/
                } else
                    swipeRefreshLayout.setRefreshing(false);

            }
        });
        /*  customer_lead_List.addOnScrollListener(new RecyclerView.OnScrollListener()
                 {
              @Override
              public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                  super.onScrollStateChanged(recyclerView, newState);

              }

              @Override
              public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                  super.onScrolled(recyclerView, dx, dy);

                if(layoutManager.findLastCompletelyVisibleItemPosition() == AllitemsList.size()-3 && recallApi)
                   {
           callApi(loader);
                  }
              }
          });*/

        return v;
    }


    @Override
    public void onResume() {

        if (Globals.checkInternet(getActivity())) {
            loader.setVisibility(View.VISIBLE);
            if (Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("AddOrder")|| Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("Quotation")) {
                callzoneBpApi(loader);

            } else {
                callApi(loader);

            }

            //  callApi(loader);
        }
        super.onResume();
    }

    private CustomersAdapterDetals dadapter;
    private CustomersAdapter adapter;

    private void callApi(ProgressBar loader) {
        CustomerViewModel model = ViewModelProviders.of(this).get(CustomerViewModel.class);
        model.getCustomersList(loader).observe(getActivity(), new Observer<List<BusinessPartnerData>>() {
            @Override
            public void onChanged(@Nullable List<BusinessPartnerData> itemsList) {

                if (itemsList.size() > 0) {
                    layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    customer_lead_List.setLayoutManager(layoutManager);
                    AllitemsList.clear();
                    AllitemsList.addAll(itemsList);
                    if (Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("DashBoard")) {


                        dadapter = new CustomersAdapterDetals(getActivity(), AllitemsList);

                        customer_lead_List.setAdapter(dadapter);
                        dadapter.notifyDataSetChanged();
                    } else {
                        adapter = new CustomersAdapter(getActivity(), AllitemsList);
                        customer_lead_List.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {

                    no_datafound.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void callzoneBpApi(ProgressBar loader) {
        CustomerViewModel model = ViewModelProviders.of(this).get(CustomerViewModel.class);
        model.getCustomersListZone(loader).observe(getActivity(), new Observer<List<BusinessPartnerData>>() {
            @Override
            public void onChanged(@Nullable List<BusinessPartnerData> itemsList) {

                if (itemsList.size() > 0) {
                    layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    customer_lead_List.setLayoutManager(layoutManager);
                    AllitemsList.clear();
                    AllitemsList.addAll(itemsList);
                    Log.e("ZoeCustomerList", "onChanged: " + AllitemsList.size());

                    adapter = new CustomersAdapter(getActivity(), AllitemsList);
                    customer_lead_List.setAdapter(adapter);
                    adapter.notifyDataSetChanged();




//                    if (Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("DashBoard")) {
//
//
//                        dadapter = new CustomersAdapterDetals(getActivity(), AllitemsList);
//
//                        customer_lead_List.setAdapter(dadapter);
//                        dadapter.notifyDataSetChanged();
//                    } else {
//                        adapter = new CustomersAdapter(getActivity(), AllitemsList);
//                        customer_lead_List.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                    }
                } else {

                    no_datafound.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
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


        //menu.clear();
        inflater.inflate(R.menu.bussiness_filter, menu);
        menu.findItem(R.id.plus).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(((BussinessPartners) mContext).getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search Customers");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (Prefs.getString(Globals.BussinessPageType, "").equalsIgnoreCase("DashBoard")) {
                    if (dadapter != null)
                        dadapter.filter(newText);

                } else {
                    if (adapter != null)
                        adapter.filter(newText);
                }
                return true;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.plus:
                Prefs.putString(Globals.AddBp, "");
                startActivity(new Intent(getContext(), AddBPCustomer.class));
                break;
            case R.id.all:
                item.setChecked(!item.isChecked());
                if (dadapter != null)
                    dadapter.AllData();
                if (adapter != null)
                    adapter.AllData();
                break;
            case R.id.my:
                item.setChecked(!item.isChecked());
                if (dadapter != null)
                    dadapter.Customerfilter();

                if (adapter != null)
                    adapter.Customerfilter();
                break;
            case R.id.my_team:
                break;
            /*case R.id.newest:
                item.setChecked(!item.isChecked());
                if(dadapter!=null)
                    dadapter.Typefilter("New Business");
                if(adapter!=null)
                    adapter.Typefilter("New Business");
                break;
            case R.id.oldest:
                item.setChecked(!item.isChecked());
                if(dadapter!=null)
                    dadapter.Typefilter("Existing Business");
                if(adapter!=null)
                    adapter.Typefilter("Existing Business");
                break;
*/
        }
        return true;
    }


}