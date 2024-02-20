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

public class Sale_Group_Inovice_Reports extends AppCompatActivity {

    private static final String TAG = "Sale_Group_Inovice_Repo";
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

    @BindView(R.id.all_customer)
    TextView all_customer;
    String fromwhere;
    String groupCode;
    String groupFilter;
    private String intentGroupName = "";
    private String intentStockGroupFromWHere = "";

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


//        loadLedgerCompFragment();

        groupby_dropdown.setVisibility(View.INVISIBLE);

        groupCode = getIntent().getStringExtra("code");
        groupFilter = getIntent().getStringExtra("group");
        intentStockGroupFromWHere = getIntent().getStringExtra("stockFormWhere");
        fromwhere = "";
        Log.e(TAG, "onCreate: " + groupCode);


        if (intentStockGroupFromWHere.equalsIgnoreCase("zonesub")) {
            groupby_dropdown.setVisibility(View.GONE);
            all_customer.setVisibility(View.INVISIBLE);
        } else {
            if (groupFilter.equalsIgnoreCase("Zone")) {
                groupby_dropdown.setVisibility(View.VISIBLE);
                all_customer.setVisibility(View.VISIBLE);
            } else {
                groupby_dropdown.setVisibility(View.GONE);
                all_customer.setVisibility(View.INVISIBLE);
            }
        }


        intentGroupName = getIntent().getStringExtra("groupname");

        toolbar.setTitle(intentGroupName);
        loadLedgerCompFragment(new Ledger_CompGroup_Fragment(salesvalue, from_to_date, type_dropdown,
                groupby_dropdown, fromwhere, groupCode, groupFilter));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.getMenu().findItem(R.id.filterAmount).setVisible(false);
        // toolbar.getMenu().findItem(R.id.fil).setVisible(false);
    }

    ArrayList<Customers_Report> customerList = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.transaction_menu_filter, menu);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.share_sales).setVisible(true);
        menu.findItem(R.id.ledger_menu).setVisible(false);
        menu.findItem(R.id.filterAmountAsc).setVisible(true);


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