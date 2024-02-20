 package com.cinntra.ledure.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.OrderPagerAdapter;
import com.cinntra.ledure.fragments.Open_Order;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderActivity extends MainBaseActivity implements View.OnClickListener {
    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.head_title)
    TextView head_title;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.fragment_orders);
     ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
     setDefaults();

        }
    //private String []tabs = {"Open","All","Approval Status"};
    private String []tabs = {"Open","All"};
    private ArrayList<Fragment> fragments =new ArrayList<Fragment>();
    private void setDefaults() {
        head_title.setText(getString(R.string.sales_order));
        fragments.add(new Open_Order());
        //fragments.add(new All_Order());
        new_quatos.setVisibility(View.VISIBLE);
        back_press.setOnClickListener(this);
        new_quatos.setOnClickListener(this);
        search.setOnClickListener(this);
        filter.setOnClickListener(this);

        OrderPagerAdapter pagerAdapter = new OrderPagerAdapter(getSupportFragmentManager(),fragments,tabs);
        viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewpager);


    }


    @Override
    public void onClick(View v) {
     switch (v.getId())
             {
     case R.id.back_press :
          finish();
          break;
      case R.id.new_quatos:

          startActivity(new Intent(OrderActivity.this,AddOrderAct.class));
          /* New_Order fragment = new New_Order();
           FragmentManager fm       = getSupportFragmentManager();
           FragmentTransaction transaction  = fm.beginTransaction();
           transaction.replace(R.id.main_container, fragment);
           transaction.addToBackStack(null);
           transaction.commit();*/
           break;

       case R.id.search:
          mainHeaderLay.setVisibility(View.GONE);
          searchLay.setVisibility(View.VISIBLE);

          searchView.setIconifiedByDefault(true);
          searchView.setFocusable(true);
          searchView.setIconified(false);
          searchView.requestFocusFromTouch();
           break;

          case R.id.filter:

             PopupMenu popupMenu = new PopupMenu(OrderActivity.this,filter);
             popupMenu.getMenuInflater().inflate(R.menu.order_filter,popupMenu.getMenu());
             popupMenu.show();

             popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
              @Override
              public boolean onMenuItemClick(MenuItem item) {
                /* switch (item.getItemId()){
                                 case R.id.all:
                                     item.isChecked();
                                     break;
                                 case R.id.my:
                                     break;
                                 case R.id.my_team:
                                     break;
                                 case R.id.newest:
                                     break;
                                 case R.id.oldest:
                                     break;
                             }*/
               return true;
                         }
                     });
                     break;

              }
    }

    @Override
    public void onBackPressed()
    {
        if(new_quatos!=null) {
            new_quatos.setClickable(true);
            getSupportActionBar().show();
            if(mainHeaderLay.getVisibility()==View.GONE)
            {
                searchLay.setVisibility(View.GONE);
                mainHeaderLay.setVisibility(View.VISIBLE);
            }
            else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
        case android.R.id.home:
             this.finish();
             return true;
          }
      return super.onOptionsItemSelected(item);
    }
}