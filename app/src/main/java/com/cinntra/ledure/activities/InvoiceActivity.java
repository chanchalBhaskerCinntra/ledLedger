package com.cinntra.ledure.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.InvoicePagerAdapter;
import com.cinntra.ledure.fragments.Invoices_Override_Fragment;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InvoiceActivity extends MainBaseActivity implements View.OnClickListener {
    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.mainHeaderLay)
    RelativeLayout mainHeaderLay;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.searchView)
    SearchView searchView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_invoice);
     ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
     setDefaults();

        }
    private String []tabs = {"Overdue","All"};
    private ArrayList<Fragment> fragments =new ArrayList<Fragment>();
    private void setDefaults() {
        head_title.setText(getString(R.string.invoice));
        fragments.add(new Invoices_Override_Fragment());
     //  fragments.add(new Invoices_All_Fragment());
        new_quatos.setVisibility(View.GONE);
        back_press.setOnClickListener(this);
        search.setOnClickListener(this);

        InvoicePagerAdapter pagerAdapter = new InvoicePagerAdapter(getSupportFragmentManager(),fragments,tabs);
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