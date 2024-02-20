package com.cinntra.ledure.activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ItemsAdapter;
import com.cinntra.ledure.adapters.RecentOrderAdapter;
import com.cinntra.ledure.customUI.RecyclerViewPaginator;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.BodyItemList;
import com.cinntra.ledure.model.DocumentLines;
import com.cinntra.ledure.model.ItemCategoryData;
import com.cinntra.ledure.model.ItemResponse;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.viewModel.ItemViewModel;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemsList extends MainBaseActivity implements View.OnClickListener {

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

     List<DocumentLines> AllitemsList;

    ItemsAdapter adapter;
    //private int currentPage = PAGE_START;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);
        filterView.setVisibility(View.GONE);
        new_quatos.setVisibility(View.GONE);
        relativeCalView.setVisibility(View.GONE);
        relativeInfoView.setVisibility(View.GONE);
        AllitemsList = new ArrayList<>();
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
    head_title.setText(getResources().getString(R.string.items));
    back_press.setOnClickListener(this);
           search.setOnClickListener(this);
           AllitemsList.clear();
           String fromwhere = String.valueOf(getIntent().getExtras().getInt("CategoryID"));
           String warehouse = String.valueOf(getIntent().getStringExtra("warehouse"));
         //  Toast.makeText(this, ""+warehouse, Toast.LENGTH_SHORT).show();
           // Toast.makeText(this,fromwhere,Toast.LENGTH_SHORT).show();
          loader.setVisibility(View.VISIBLE);
           if (Globals.checkInternet(getApplicationContext()))
               itemOnPageBasis(pageSize,fromwhere,warehouse);
              // callApi(loader,fromwhere);


       /*    RecyclerViewPaginator recyclerViewPaginator = new RecyclerViewPaginator(itemsRecycler)
           {
               @Override
               public boolean isLastPage() {
                   return viewModel.isLastPage();
               }

               @Override
               public void loadMore(Long start, Long count) {
                   viewModel.loadData(start, count);
               }
           };*/

           /* Add this paginator to the onScrollListener of RecyclerView  */
          // itemsRecycler.addOnScrollListener(recyclerViewPaginator);
           itemsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
               @Override
               public void onScrolled(RecyclerView recyclerView,
                                      int dx, int dy) {
                   super.onScrolled(recyclerView, dx, dy);
                   int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                   int totalItemCount = recyclerView.getLayoutManager().getItemCount();

                   Log.e("Visible <==> Total ==>",""+visibleItemCount+" ==> "+totalItemCount);
                    if(visibleItemCount==totalItemCount-5){
                        if (Globals.checkInternet(getApplicationContext()))
                            itemOnPageBasis(pageSize,fromwhere,warehouse);
                    }
               }
           });
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
    private void callApi(ProgressBar loader, String fromwhere)
         {
        ItemCategoryData id = new ItemCategoryData();
        id.setCatID(fromwhere);
        id.setPriceListId("3");
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getItemsList(loader,id).observe(this, new Observer<List<DocumentLines>>() {
            @Override
            public void onChanged(@Nullable List<DocumentLines> itemsList) {

                if(itemsList == null || itemsList.size() == 0){
                    Globals.setmessage(getApplicationContext());
                    no_datafound.setVisibility(View.VISIBLE);
                }else {
                    no_datafound.setVisibility(View.GONE);
                    layoutManager = new LinearLayoutManager(ItemsList.this, RecyclerView.VERTICAL, false);
                    AllitemsList.clear();
                    AllitemsList.addAll(itemsList);
                    itemsRecycler.setLayoutManager(layoutManager);
                    adapter = new ItemsAdapter(ItemsList.this, AllitemsList);
                    itemsRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }
        });

    }

    boolean dataoverFromAPI =true;
    private void itemOnPageBasis(int pageSize,String catID,String warehouse)
          {
        HashMap<String,Integer> hde = new HashMap<>();
//        hde.put("PriceListId",1 );
//        hde.put("PageNo",pageNo );
//        hde.put("MaxSize",pageSize );
//        hde.put("CatID",Integer.valueOf(catID) );
//        hde.put("WarehouseCode", warehouse);
              BodyItemList bodyItemList=new BodyItemList();
              bodyItemList.setPageNo(pageNo);
              bodyItemList.setPriceListId(1);
              bodyItemList.setMaxSize(pageSize);
              bodyItemList.setCatID(Integer.valueOf(catID) );
              bodyItemList.setWarehouseCode(warehouse);

        Call<ItemResponse> call = NewApiClient.getInstance().getApiService().itemAllFilter(bodyItemList);
        call.enqueue(new Callback<ItemResponse>() {
            @Override
        public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response)
            {


                layoutManager = new LinearLayoutManager(ItemsList.this, RecyclerView.VERTICAL, false);

                AllitemsList.addAll(response.body().getValue());
                itemsRecycler.setLayoutManager(layoutManager);
                adapter = new ItemsAdapter(ItemsList.this, AllitemsList);
                itemsRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if(response.body().getValue().size()==0){
                    dataoverFromAPI = false;
                    pageNo++;
                }
                loader.setVisibility(View.GONE);

            }
            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });
    }



}