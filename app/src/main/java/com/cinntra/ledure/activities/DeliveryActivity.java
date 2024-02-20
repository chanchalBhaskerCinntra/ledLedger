package com.cinntra.ledure.activities;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.DeliveryAdapter;
import com.cinntra.ledure.databinding.DeliveryActBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.AddBusinessPartnerData;
import com.cinntra.ledure.model.QuotationItem;
import com.cinntra.ledure.model.QuotationItemDelivery;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.ResponseDeliveryOrder;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryActivity extends MainBaseActivity implements View.OnClickListener {
    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.mainHeaderLay)
    RelativeLayout mainHeaderLay;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.filter)
    ImageView filter;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;

    DeliveryAdapter adapter;
    DeliveryActBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState)
      {
    super.onCreate(savedInstanceState);
    binding=DeliveryActBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    ButterKnife.bind(this);
    setDefaults();
    eventSearchManager();

       }
   // String DelievryType;
    private ArrayList<QuotationItemDelivery> deliveryList = new ArrayList<>();
    private void setDefaults()
      {
   // DelievryType = getIntent().getExtras().getString("DeliveryType").trim();
    head_title.setText("Deliveries");
    new_quatos.setVisibility(View.GONE);
    search.setOnClickListener(this);
    back_press.setOnClickListener(this);
    filter.setOnClickListener(this);
    binding.header.relativeInfoView.setVisibility(View.GONE);
    binding.header.relativeCalView.setVisibility(View.GONE);
    binding.header.filterView.setVisibility(View.GONE);
 //   binding.header.searchLay.setVisibility(View.GONE);


         /* AddBusinessPartnerData salesEmployeeItem = new AddBusinessPartnerData();
          salesEmployeeItem.setSalesPersonCode(Prefs.getString(Globals.SalesEmployeeCode,""));
          salesEmployeeItem.setT*/

          HashMap<String, String > hd = new HashMap<>();
          hd.put("SalesEmployeeCode",Prefs.getString(Globals.SalesEmployeeCode,""));
          hd.put("Type","");
         /* if(DelievryType.equalsIgnoreCase("Overdue"))
            salesEmployeeItem.setType("over");
          else if(DelievryType.equalsIgnoreCase("Open"))
              salesEmployeeItem.setType("open");
          else if(DelievryType.equalsIgnoreCase("Closed"))
              salesEmployeeItem.setType("close");*/


    if(Globals.checkInternet(this)){
        loader.setVisibility(View.VISIBLE);
        callApi(hd);
    }




    /*if(Globals.checkInternet(getApplicationContext()))
        callApi(loader,DelievryType);*/


      }

    private void callApi(HashMap<String,String> salesEmployeeItem ) {

        Call<ResponseDeliveryOrder> call = NewApiClient.getInstance().getApiService().orderlist(salesEmployeeItem);
        call.enqueue(new Callback<ResponseDeliveryOrder>() {
            @Override
            public void onResponse(Call<ResponseDeliveryOrder> call, Response<ResponseDeliveryOrder> response) {
                if (response != null)
                {
                if(response.code()==200){

                        deliveryList.clear();
                        deliveryList.addAll(response.body().getData());
                        adapter = new DeliveryAdapter(DeliveryActivity.this,deliveryList,3,"");
                        recyclerview.setLayoutManager(new LinearLayoutManager(DeliveryActivity.this,LinearLayoutManager.VERTICAL,false));
                        recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    if (deliveryList.size()<=0){
                        no_datafound.setVisibility(View.VISIBLE);
                    }
                }
                    loader.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ResponseDeliveryOrder> call, Throwable t) {
                Log.e("delivery>>>>>>   ", "onFailure: "+t.getMessage());
                Toast.makeText(DeliveryActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
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
                if(adapter!=null){
                    adapter.filter(query);
                }else{
                    Toast.makeText(DeliveryActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(adapter!=null){
                    adapter.filter(newText);
                }else{
                    Toast.makeText(DeliveryActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }



    @Override
    public void onClick(View v) {
       switch (v.getId())
         {
      case R.id.back_press:
       finish();
          break;
       case R.id.filter:
        showfilteroption();
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
    private void showfilteroption() {
        PopupMenu popup = new PopupMenu(DeliveryActivity.this, filter);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.delivery_filter, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.all:
                        if (adapter != null)
                            adapter.AllData();

                        break;

                    case R.id.my:

                        break;
                    case R.id.posting:

                        break;
                    case R.id.valid:
                        LocalDate dateObj1 = LocalDate.parse(Globals.curntDate);
                        LocalDate afterdate1 = dateObj1.plusDays(8);
                        adapter.ValidDate(afterdate1, dateObj1);
                        break;
                    case R.id.newest:

                        break;
                    case R.id.oldest:

                        break;

                    case R.id.filter:
                        showfilteroption();
                        break;
                }
                return true;

            }
        });
        popup.show();
    }

}