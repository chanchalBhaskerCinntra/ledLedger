package com.cinntra.ledure.activities;


import static com.cinntra.ledure.activities.AddOrderAct.addQuotationObj;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CategoryAdapter;
import com.cinntra.ledure.adapters.Selected_Item_Adapter;
import com.cinntra.ledure.databinding.ActivitySelectedItemBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.interfaces.DatabaseClick;
import com.cinntra.ledure.model.ItemCategoryResponse;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectedItems extends MainBaseActivity implements View.OnClickListener, DatabaseClick {

     @BindView(R.id.head_title)
    TextView head_title;
     @BindView(R.id.back_press)
     RelativeLayout back_press;
     @BindView(R.id.new_quatos)
     RelativeLayout new_quatos;
     @BindView(R.id.itemsRecycler)
     RecyclerView itemsRecycler;
     @BindView(R.id.done)
     Button done;
    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.mainHeaderLay)
    RelativeLayout mainHeaderLay;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.filterView)
    RelativeLayout filterView;
    ActivitySelectedItemBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySelectedItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this);
        filterView.setVisibility(View.GONE);
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
    String fromwhere;

    private void setDefaults() {
         fromwhere = getIntent().getStringExtra("FromWhere");
        if(fromwhere.equalsIgnoreCase("invoices")){
            new_quatos.setVisibility(View.GONE);
            new_quatos.setClickable(false);
        }
        binding.quoteHead.relativeCalView.setVisibility(View.INVISIBLE);
        binding.quoteHead.relativeInfoView.setVisibility(View.INVISIBLE);

        head_title.setText(getResources().getString(R.string.selected_items));
        back_press.setOnClickListener(this);
        new_quatos.setOnClickListener(this);
        done.setOnClickListener(this);
        search.setOnClickListener(this);

       setAdapter();
    }
    Selected_Item_Adapter adapter;
    private void setAdapter() {
        adapter = new Selected_Item_Adapter(SelectedItems.this, Globals.SelectedItems);
        itemsRecycler.setLayoutManager(new LinearLayoutManager(SelectedItems.this, RecyclerView.VERTICAL,false));
        itemsRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
    case R.id.back_press:
       /* Intent ii = new Intent();
        setResult(RESULT_OK, ii);*/
        onBackPressed();
//        finish();
        break;
        case R.id.new_quatos:
           /* Intent intent = new Intent(SelectedItems.this, ItemsList.class);
            startActivityForResult(intent, ITEMSCODE);*/
            openCategorydailog();
            break;
        case R.id.done:
            Intent i = new Intent();
            setResult(RESULT_OK, i);
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


    private void eventSearchManager()
         {

        searchView.setBackgroundColor(Color.parseColor("#00000000"));
        searchLay.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
             {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainHeaderLay.setVisibility(View.VISIBLE);
                searchLay.setVisibility(View.GONE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode == RESULT_OK) {
          setAdapter();

        }
    }

    Dialog TaxListdialog;
    private void openCategorydailog() {
        RelativeLayout backPress;
        TextView head_title;
        RecyclerView recyclerview;
        ProgressBar loader;

        TaxListdialog = new Dialog(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View custom_dialog =layoutInflater.inflate(R.layout.taxes_alert,null);
        recyclerview = custom_dialog.findViewById(R.id.recyclerview);
        backPress    = custom_dialog.findViewById(R.id.back_press);
        head_title   = custom_dialog.findViewById(R.id.head_title);
        loader       = custom_dialog.findViewById(R.id.loader);
        head_title.setText(this.getString(R.string.select_tax));
        TaxListdialog.setContentView(custom_dialog);
        TaxListdialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        TaxListdialog.show();

        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaxListdialog.dismiss();
            }
        });
        Call<ItemCategoryResponse> call = NewApiClient.getInstance().getApiService().getAllCategory();
        call.enqueue(new Callback<ItemCategoryResponse>() {
            @Override
            public void onResponse(Call<ItemCategoryResponse> call, Response<ItemCategoryResponse> response) {
                loader.setVisibility(View.GONE);
                if(response.code()==200)
                {

                    CategoryAdapter adapter = new CategoryAdapter(SelectedItems.this, response.body().getData(),TaxListdialog);
                    recyclerview.setLayoutManager(new LinearLayoutManager(SelectedItems.this, RecyclerView.VERTICAL,false));
                    recyclerview.setAdapter(adapter);
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(SelectedItems.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ItemCategoryResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(SelectedItems.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(int po) {
        Intent intent = new Intent(SelectedItems.this, ItemsList.class);
        intent.putExtra("CategoryID",po);
        if (fromwhere.equalsIgnoreCase("addQt")){
            intent.putExtra("warehouse",AddQuotationAct.addQuotationObj.getWarehouseCode());
        }else {
            intent.putExtra("warehouse",addQuotationObj.getWarehouseCode());
        }

        startActivityForResult(intent, CreateContact.ITEMSCODE);
    }


   }