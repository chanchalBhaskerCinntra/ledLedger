package com.cinntra.ledure.activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CashDisAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.CashDiscountItem;
import com.cinntra.ledure.model.CashDiscountResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CashDiscount extends MainBaseActivity implements View.OnClickListener {

     @BindView(R.id.head_title)
    TextView head_title;
     @BindView(R.id.back_press)
     RelativeLayout back_press;
     @BindView(R.id.itemsRecycler)
     RecyclerView itemsRecycler;
     @BindView(R.id.loader)
     ProgressBar loader;
     @BindView(R.id.no_datafound)
     ImageView no_datafound;
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

    @BindView(R.id.relativeCalView)
    RelativeLayout relativeCalView;

    @BindView(R.id.relativeInfoView)
    RelativeLayout relativeInfoView;

    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
     LinearLayoutManager layoutManager;
     int skipSize = 20;
     int pageSize =100;
     int pageNo =1;


    //private int currentPage = PAGE_START;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);
        filterView.setVisibility(View.GONE);
        new_quatos.setVisibility(View.GONE);
        relativeInfoView.setVisibility(View.GONE);
        relativeCalView.setVisibility(View.GONE);
        layoutManager=new LinearLayoutManager(this);

        setDefaults();

        eventSearchManager();






    }

    @Override
    public void onBackPressed()
    {

        if(mainHeaderLay.getVisibility()==View.GONE)
        {
            searchLay.setVisibility(View.GONE);
            mainHeaderLay.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    private void eventSearchManager()
    {
        searchView.setBackgroundColor(Color.parseColor("#00000000"));
        searchLay.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
           {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(adapter!=null){
                    adapter.filter(newText);
                }
                return true;
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDefaults()
       {
    head_title.setText(getResources().getString(R.string.cash_discounts));
    back_press.setOnClickListener(this);
           search.setOnClickListener(this);


          loader.setVisibility(View.VISIBLE);
           if (Globals.checkInternet(getApplicationContext()))
               cashDiscountOneApi();
               //cashDiscountApiTest();


           itemsRecycler.addOnScrollListener(scrollListener);
       }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
    case R.id.back_press:
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        break;
        case R.id.search:
            mainHeaderLay.setVisibility(View.GONE);
            searchLay.setVisibility(View.VISIBLE);

            searchView.setIconifiedByDefault(true);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
            break;

          }
       }


    boolean dataoverFromAPI =true;


    CashDisAdapter adapter;
    private ArrayList<CashDiscountItem> cashList = new ArrayList<>();


    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleitempositon = layoutManager.findFirstVisibleItemPosition(); //first item
            int visibleItemCount = layoutManager.getChildCount(); //total number of visible item
            int totalItemCount = layoutManager.getItemCount();   //total number of item

            boolean isNotLoadingAndNotLastPage = !isLoading && !islastPage;
            boolean isAtLastItem = firstVisibleitempositon + visibleItemCount >= totalItemCount;
            boolean isNotAtBeginning = firstVisibleitempositon >= 0;
            boolean isTotaolMoreThanVisible = totalItemCount >= Globals.QUERY_PAGE_SIZE;
            boolean shouldPaginate =
                    isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotaolMoreThanVisible
                            && isScrollingpage;

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                loader.setVisibility(View.VISIBLE);
                pageNo++;
              cashDiscountPageApi();
                isScrollingpage = false;
            } else {
                // Log.d(TAG, "onScrolled:not paginate");
                recyclerView.setPadding(0, 0, 0, 0);
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { //it means we are scrolling
                isScrollingpage = true;
            }
        }
    };


    private void cashDiscountOneApi()
           {
        HashMap<String,String> hde = new HashMap<>();
      //  hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode,""));
        hde.put("PageNo",String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("FromDate","");
        hde.put("ToDate","");
        Call<CashDiscountResponse> call = NewApiClient.getInstance().getApiService(this).cashDiscountList(hde);
        call.enqueue(new Callback<CashDiscountResponse>() {
            @Override
            public void onResponse(Call<CashDiscountResponse> call, Response<CashDiscountResponse> response) {
                if(response.code()==200) {
                    loader.setVisibility(View.GONE);
                    if(response.body().getValue() == null || response.body().getValue().size() == 0){
                        Globals.setmessage(getApplicationContext());
                        no_datafound.setVisibility(View.VISIBLE);
                    }else {
                        loader.setVisibility(View.GONE);
                        cashList.clear();
                        cashList.addAll(response.body().getValue());
                         adapter = new CashDisAdapter(CashDiscount.this,cashList);
                        itemsRecycler.setLayoutManager(layoutManager);
                        itemsRecycler.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                        no_datafound.setVisibility(View.GONE);

                        }




                }
            }
            @Override
            public void onFailure(Call<CashDiscountResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(CashDiscount.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void cashDiscountPageApi()
    {
        loader.setVisibility(View.VISIBLE);
        HashMap<String,String> hde = new HashMap<>();
      //  hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode,""));
        hde.put("PageNo",String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("FromDate","");
        hde.put("ToDate","");
        Call<CashDiscountResponse> call = NewApiClient.getInstance().getApiService(this).cashDiscountList(hde);
        call.enqueue(new Callback<CashDiscountResponse>() {
            @Override
            public void onResponse(Call<CashDiscountResponse> call, Response<CashDiscountResponse> response) {
                if(response.code()==200) {
                    loader.setVisibility(View.GONE);
                    if(response.body().getValue() == null || response.body().getValue().size() == 0){
                        Globals.setmessage(getApplicationContext());
                        no_datafound.setVisibility(View.VISIBLE);
                    }else {
                        loader.setVisibility(View.GONE);
                        // cashList.clear();
                        cashList.addAll(response.body().getValue());
//                        adapter = new CashDisAdapter(CashDiscount.this,cashList);
//                        itemsRecycler.setLayoutManager(layoutManager);
//                        itemsRecycler.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                        no_datafound.setVisibility(View.GONE);

                        if (response.body().getValue().size() == 0) {
                            pageNo++;
                            no_datafound.setVisibility(View.VISIBLE);
                        } else {
                            no_datafound.setVisibility(View.INVISIBLE);
                        }

                    }




                }
            }
            @Override
            public void onFailure(Call<CashDiscountResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(CashDiscount.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void cashDiscountApiTest()
    {
        HashMap<String,String> hde = new HashMap<>();
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode,""));
        Call<String> call = NewApiClient.getInstance().getApiService(this).cashDiscountListTest();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code()==200) {
                    loader.setVisibility(View.GONE);
                   /* if(response.body().getValue() == null || response.body().getValue().size() == 0){
                        Globals.setmessage(getApplicationContext());
                        no_datafound.setVisibility(View.VISIBLE);
                    }else {
                        cashList.clear();
                        cashList.addAll(response.body().getValue());
                        CashDisAdapter adapter = new CashDisAdapter(CashDiscount.this,cashList);
                        itemsRecycler.setLayoutManager(new LinearLayoutManager(CashDiscount.this, RecyclerView.VERTICAL,false));
                        itemsRecycler.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                        no_datafound.setVisibility(View.GONE);

                    }*/




                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(CashDiscount.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}