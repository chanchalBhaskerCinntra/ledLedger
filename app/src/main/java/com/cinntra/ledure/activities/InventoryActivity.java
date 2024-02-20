package com.cinntra.ledure.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.cinntra.ledure.adapters.EnventoryItemsAdapter;
import com.cinntra.ledure.databinding.DeliveryActBinding;
import com.cinntra.ledure.fragments.Dashboard;
import com.cinntra.ledure.globals.MainBaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;


public class InventoryActivity extends MainBaseActivity implements View.OnClickListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
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

    private EnventoryItemsAdapter adapter;
    DeliveryActBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DeliveryActBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this);
        setDefaults();
        binding.header.filterView.setVisibility(View.GONE);
        binding.header.relativeCalView.setVisibility(View.GONE);
        binding.header.relativeInfoView.setVisibility(View.GONE);

        String itemNames = getIntent().getStringExtra("IN_Type");

        if(itemNames.equalsIgnoreCase("Fast Moving"))
        {
            if(Dashboard.fastInventoryList.size()>0)
             adapter = new EnventoryItemsAdapter(InventoryActivity.this, Dashboard.fastInventoryList);
            else
               no_datafound.setVisibility(View.VISIBLE);
        }
        else if(itemNames.equalsIgnoreCase("Slow Moving"))
        {
            if(Dashboard.mediumInventoryList.size()>0)
            adapter = new EnventoryItemsAdapter(InventoryActivity.this, Dashboard.mediumInventoryList);
else
            no_datafound.setVisibility(View.VISIBLE);
        }
        else if(itemNames.equalsIgnoreCase("Non Moving"))
        {
            if(Dashboard.nonInventoryList.size()>0)
            adapter = new EnventoryItemsAdapter(InventoryActivity.this, Dashboard.nonInventoryList);
else
            no_datafound.setVisibility(View.VISIBLE);
        }
        else{
            if(Dashboard.allInventoryList.size()>0)
            adapter = new EnventoryItemsAdapter(InventoryActivity.this, Dashboard.allInventoryList);
else
            no_datafound.setVisibility(View.VISIBLE);
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(InventoryActivity.this, RecyclerView.VERTICAL,false));
        recyclerview.setAdapter(adapter);

       // eventSearchManager();
    }

    private void setDefaults()
              {
        head_title.setText(getString(R.string.inventory));
        /*InventoryItemAdapter delivery_adapter = new InventoryItemAdapter(InventoryActivity.this,itemsList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerview.setAdapter(delivery_adapter);
         */
        back_press.setOnClickListener(this);
        search.setOnClickListener(this);
        filter.setOnClickListener(this);
        new_quatos.setVisibility(View.GONE);

              }





    @Override
    public void onClick(View v)
       {
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
                    Toast.makeText(InventoryActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(adapter!=null){
                    adapter.filter(newText);
                }else{
                    Toast.makeText(InventoryActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }

    private void showfilteroption() {
        PopupMenu popup = new PopupMenu(InventoryActivity.this, filter);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.inventory_filter, popup.getMenu());

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
                    case R.id.newest:

                        break;
                    case R.id.alpha:
                        if (adapter != null)
                            adapter.Alphabetical();
                        break;

                }
                return true;

            }
        });
        popup.show();
    }
}