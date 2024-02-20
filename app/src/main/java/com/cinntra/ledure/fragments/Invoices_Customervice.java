package com.cinntra.ledure.fragments;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.Invoices_Adapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.InvoiceNewData;
import com.cinntra.ledure.model.InvoiceResponse;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.viewModel.QuotationList_ViewModel;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Invoices_Customervice extends Fragment implements View.OnClickListener {

   private QuotationList_ViewModel viewModel;
  @BindView(R.id.recyclerview)
  RecyclerView recyclerview;
  @BindView(R.id.loader)
  ProgressBar loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
  private SearchView searchView;
  private Invoices_Adapter adapter;
    int currentpage = 0;
    boolean recallApi = true;
    LinearLayoutManager layoutManager;
    ArrayList<InvoiceNewData> AllItemList;


 String cardCode ="";
    public Invoices_Customervice(String cardCode) {
    //Required empty public constructor
        this.cardCode =cardCode;
       }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
     //Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_quotes_list, container, false);
    ButterKnife.bind(this,v);
        AllItemList = new ArrayList<>();


        if(Globals.checkInternet(getActivity())) {
            no_datafound.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
            callApi(loader);
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


    private void callApi(ProgressBar loader) {
        loader.setVisibility(View.VISIBLE);
        Call<InvoiceResponse> call = NewApiClient.getInstance().getApiService().getallinvoice();
        call.enqueue(new Callback<InvoiceResponse>() {
            @Override
            public void onResponse(Call<InvoiceResponse> call, Response<InvoiceResponse> response) {

                if(response.code()==200)
                {

                       AllItemList.clear();
                    AllItemList.addAll(response.body().getValue());
                    layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
                    adapter = new Invoices_Adapter(getContext(),AllItemList);
                    recyclerview.setLayoutManager(layoutManager);
                    recyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Log.e("CardCodeT=>",cardCode);
                    if(adapter!=null&&cardCode!="")
                        adapter.filterCustomer(cardCode);

                    if(adapter.getItemCount()==0||adapter==null)
                    {
                        no_datafound.setVisibility(View.VISIBLE);

                    }
                    else {
                        no_datafound.setVisibility(View.GONE);

                    }
                    loader.setVisibility(View.GONE);
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,LeadResponse.class);
                        Toast.makeText(getContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<InvoiceResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
           {
    switch(item.getItemId())
        {
    case R.id.all:
      if(adapter!=null)
         adapter.AllData();
         break;
    case R.id.my:
         if(adapter!=null)
            adapter.Customername();
            break;
    case R.id.my_team:
           break;
    case  R.id.valid:
          if(adapter!= null)
             adapter.ValidDate();
           break;
     case R.id.newest:
          LocalDate dateObj1 = LocalDate.parse(Globals.curntDate);
          LocalDate afterdate1 = dateObj1.minusDays(8);
          adapter.PostingDate(afterdate1, dateObj1);
          break;
     case R.id.oldest:
          Toast.makeText(getContext(),"Existing",Toast.LENGTH_LONG).show();
           break;

        }
        return true;
    }


}