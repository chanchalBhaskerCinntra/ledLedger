package com.cinntra.ledure.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cinntra.ledure.R;

import com.cinntra.ledure.fragments.Ledger_CompGroup_Fragment;
import com.cinntra.ledure.fragments.Ledger_Comp_Fragment;

import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.Customers_Report;
import com.pixplicity.easyprefs.library.Prefs;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Sale_Inovice_Reports extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.groupby_dropdown)
    Spinner groupby_dropdown;
    @BindView(R.id.salesvalue)
    TextView salesvalue;
    @BindView(R.id.type_dropdown)
    Spinner type_dropdown;
    @BindView(R.id.from_to_date)
    TextView from_to_date;
    String fromwhere;
    String groupCode;
    String groupFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_reports_new);
        ButterKnife.bind(this);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);


        //  Toast.makeText(this, "SaleINVOICE REPORT", Toast.LENGTH_SHORT).show();

//        loadLedgerCompFragment();
        if (Prefs.getString("ForReports", "MainActivity_B2C_Ledger").equalsIgnoreCase("Group")) {
            fromwhere = "Group";
            groupCode = getIntent().getStringExtra("cardCode");
            groupFilter = "Group";


        } else {
            fromwhere = "";
            groupCode = "";
            groupFilter = "";
        }


        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            toolbar.setTitle("Total Purchase");
        } else {
            toolbar.setTitle("Total Sales");
        }

        loadLedgerCompFragment(new Ledger_Comp_Fragment(salesvalue, from_to_date, type_dropdown, groupby_dropdown, fromwhere, groupCode, groupFilter));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // loadLedgerCompFragment(new Ledger_Comp_Fragment(salesvalue,type_dropdown));//
        groupby_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                loadLedgerCompFragment(new Ledger_CompGroup_Fragment(salesvalue, from_to_date, type_dropdown,
                        groupby_dropdown, fromwhere, groupCode, groupFilter));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private static final String TAG = "Sale_Inovice_Reports";

    @Override
    protected void onResume() {

        super.onResume();
        toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
        toolbar.getMenu().findItem(R.id.filterAmountAsc).setVisible(true);
//        toolbar.getMenu().findItem(R.id.filterAtoZ).setChecked(Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ATOZ));
//        toolbar.getMenu().findItem(R.id.filterZtoA).setChecked(Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ZTOA));
//        toolbar.getMenu().findItem(R.id.filterAmount).setChecked(Prefs.getString(Globals.PREFS_Amount, "").equalsIgnoreCase(Globals.DESC));
//
//
//        toolbar.getMenu().findItem(R.id.filterAmount).setChecked(false);
//        Log.e(TAG, "onResumeATOZ: "+Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ATOZ));
//        Log.e(TAG, "onResumeZTOA: "+Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ZTOA));
//        Log.e(TAG, "onResumeAMOUNT: "+Prefs.getString(Globals.PREFS_Amount, "").equalsIgnoreCase(Globals.DESC));
    }

    private void loadLedgerCompFragment(Fragment fragment) {
        Bundle b = new Bundle();
        b.putSerializable(Globals.LedgerCompanyData, customerList);
//        Ledger_Comp_Fragment fragment = new Ledger_Comp_Fragment();
        fragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (fm.getBackStackEntryCount() > 0)
            transaction.replace(R.id.container, fragment);
        else
            transaction.add(R.id.container, fragment);
        transaction.commit();


    }

    ArrayList<Customers_Report> customerList = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.transaction_menu_filter, menu);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.share_sales).setVisible(true);
        menu.findItem(R.id.ledger_menu).setVisible(false);
        menu.findItem(R.id.calendar).setVisible(false);
        menu.findItem(R.id.filterDate).setVisible(false);
        menu.findItem(R.id.filterAmountAsc).setVisible(false);
        menu.findItem(R.id.filterZtoA).setChecked(true);
        //   Toast.makeText(this, "" + Prefs.getString(Globals.PREFS_ATOZ, ""), Toast.LENGTH_SHORT).show();
//        menu.findItem(R.id.filterAtoZ).setChecked(Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ATOZ));
//        menu.findItem(R.id.filterZtoA).setChecked(Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ZTOA));
//        menu.findItem(R.id.filterAmount).setChecked(Prefs.getString(Globals.PREFS_Amount, "").equalsIgnoreCase(Globals.DESC));


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.calendar:
                // Globals.selectDat(this);
                break;
            case R.id.ledger_menu:
                Toast.makeText(this, "ledger", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.filter);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // setAdapter();

        }
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}