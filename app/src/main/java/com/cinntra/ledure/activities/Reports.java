package com.cinntra.ledure.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.cinntra.ledure.R;
import com.cinntra.ledure.customUI.CustomMarkerViewReceivables;
import com.cinntra.ledure.customUI.RoundedBarChart;
import com.cinntra.ledure.fragments.CreditNotes_Fragment;
import com.cinntra.ledure.fragments.DueFragment;
import com.cinntra.ledure.fragments.DuePaymentZoneFragment;
import com.cinntra.ledure.fragments.PaymentCollection_Fragment;
import com.cinntra.ledure.fragments.Ledger_Comp_Fragment;
import com.cinntra.ledure.fragments.PaymentCollection_ZoneFragment;
import com.cinntra.ledure.fragments.PaymentReceipt_Fragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.Customers_Report;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Reports extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.groupby_dropdown)
    Spinner groupby_dropdown;
    @BindView(R.id.salesvalue)
    TextView salesvalue;
    @BindView(R.id.linearDropDownLedger)
    LinearLayout linearDropDownLedger;

    @BindView(R.id.type_dropdown)
    Spinner type_dropdown;


    @BindView(R.id.any_chart_view_dash)
    BarChart customer_barChart;
    @BindView(R.id.from_to_date)
    TextView from_to_date;
    String groupCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reports);
        ButterKnife.bind(this);
        //   Toast.makeText(this, "Reports", Toast.LENGTH_SHORT).show();
        toolbar.getMenu().findItem(R.id.filterAtoZ).setChecked(true);

        ActionBar actionBar = getSupportActionBar();


//        getActionBar().hide();

        if (Prefs.getString("ForReports", "").equalsIgnoreCase("Receivable")) {

            toolbar.setTitle("Receivable");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmountAsc).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            managebyShubhReceivable(MainActivity_B2C.Receivableentries);

            Prefs.putString(Globals.FROM_DATE_receivable, "All");
            customer_barChart.setVisibility(View.VISIBLE);
            from_to_date.setVisibility(View.GONE);


            //groupby_dropdown.setSelection(5);

            loadLedgerCompFragment(new PaymentCollection_Fragment(salesvalue, from_to_date, type_dropdown, groupby_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("DueZoneRe")) {

            toolbar.setTitle("Zones");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmountAsc).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            //   Prefs.putString(Globals.FROM_DATE_receivable, "All");

            //groupby_dropdown.setSelection(5);

            loadLedgerCompFragment(new DuePaymentZoneFragment(salesvalue, from_to_date, type_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("ZoneRe")) {

            toolbar.setTitle("Zones");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmountAsc).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            customer_barChart.setVisibility(View.VISIBLE);

            //   Prefs.putString(Globals.FROM_DATE_receivable, "All");

            //groupby_dropdown.setSelection(5);

            loadLedgerCompFragment(new PaymentCollection_ZoneFragment(salesvalue, from_to_date, type_dropdown, customer_barChart));

        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("payment")) {

            toolbar.setTitle("Dues");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmountAsc).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            Prefs.putString(Globals.OVER_DUE_DATE_PREF, "7");
            from_to_date.setVisibility(View.GONE);
            linearDropDownLedger.setVisibility(View.VISIBLE);


            //groupby_dropdown.setSelection(5);
            loadLedgerCompFragment(new DueFragment(salesvalue, from_to_date, type_dropdown, groupby_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("overDue")) {

            toolbar.setTitle("OverDues");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.calendar).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAmountAsc).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);

            Prefs.putString(Globals.FROM_DATE_receivable, "All");
            groupby_dropdown.setVisibility(View.VISIBLE);
            linearDropDownLedger.setVisibility(View.VISIBLE);

            //groupby_dropdown.setSelection(5);
            loadLedgerCompFragment(new DueFragment(salesvalue, from_to_date, type_dropdown, groupby_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("MainActivity_B2C_CreditNotes")) {
            toolbar.setTitle("Total Credits");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);


            loadLedgerCompFragment(new CreditNotes_Fragment(salesvalue, from_to_date, type_dropdown));
        } else if (Prefs.getString("ForReports", "").equalsIgnoreCase("ReceiptLedger")) {

            if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                toolbar.setTitle("Payment");
            } else {
                toolbar.setTitle("Total Received");
            }

            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.share_received).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmountAsc).setVisible(true);
            // toolbar.getMenu().findItem(R.id.filterDate).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.clearAllFilter).setVisible(true);


            loadLedgerCompFragment(new PaymentReceipt_Fragment(salesvalue, from_to_date, type_dropdown));
        } else {
            toolbar.setTitle("Total Sales");
            toolbar.getMenu().findItem(R.id.info_trans).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger).setVisible(false);
            toolbar.getMenu().findItem(R.id.ledger_menu).setVisible(false);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAtoZ).setChecked(true);
            toolbar.getMenu().findItem(R.id.filterZtoA).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterDate).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmount).setVisible(true);
            toolbar.getMenu().findItem(R.id.filterAmountAsc).setVisible(true);

            //  Toast.makeText(this, "" + Prefs.getString(Globals.PREFS_ATOZ, ""), Toast.LENGTH_SHORT).show();


          /*  toolbar.getMenu().findItem(R.id.filterAtoZ).setChecked(Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ATOZ));
            toolbar.getMenu().findItem(R.id.filterZtoA).setChecked(Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ZTOA));
            toolbar.getMenu().findItem(R.id.filterAmount).setChecked(Prefs.getString(Globals.PREFS_Amount, "").equalsIgnoreCase(Globals.DESC));*/


            loadLedgerCompFragment(new Ledger_Comp_Fragment(salesvalue, from_to_date, type_dropdown, groupby_dropdown, "", "", ""));
        }
        // loadLedgerCompFragment(new Ledger_Comp_Fragment(salesvalue,type_dropdown));//

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    private void managebyShubhReceivable(List<BarEntry> entries) {


        List<String> xvalues = Arrays.asList(">90", "61-90", "0-30", "31-60");
        //   List<String> xvalues= Arrays.asList("Jan","Feb","March","April","May","June");



       /* for (int i = 1; i <= entries.size()-1; i++) {
            temp.add(String.valueOf(entries.get(i).getY()));
        }*/


     /*   CustomMarkerView markerView = new CustomMarkerView(context, R.layout.barchart_marker, temp);


        customer_barChart.setMarker(markerView);

        customer_barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // Display your MarkerView when a value is selected
                customer_barChart.highlightValue(h);
                // markerView.content("Value: " + e.getY()); // Customize content as needed
                markerView.refreshContent(e, h);
                markerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected() {
                // Hide the MarkerView when nothing is selected
                markerView.setVisibility(View.GONE);
            }
        });*/


        RoundedBarChart roundedBarChartRenderer = new RoundedBarChart(customer_barChart, customer_barChart.getAnimator(), customer_barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(10f);
        customer_barChart.setRenderer(roundedBarChartRenderer);

        customer_barChart.setDrawBarShadow(false);
        customer_barChart.setDrawValueAboveBar(false);
        customer_barChart.getDescription().setEnabled(false);
        customer_barChart.setDrawGridBackground(false);


        customer_barChart.getAxisRight().setEnabled(false);
        Legend legend = customer_barChart.getLegend();
        legend.setEnabled(false);


        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        dataSet.setColor(getResources().getColor(R.color.white));
        dataSet.setDrawValues(false);
        dataSet.setHighLightColor(Color.WHITE); // Set the color
        dataSet.setHighLightAlpha(100);
        dataSets.add(dataSet);


        BarData data = new BarData(dataSets);
        data.setBarWidth(0.75f);
        data.setValueTextColor(getResources().getColor(R.color.white));
        //  data.setValueTextColor(Color.WHITE);
        customer_barChart.setData(data);
        customer_barChart.setFitBars(false);


        // customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xvalues));
        customer_barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        customer_barChart.getXAxis().setTextColor(getResources().getColor(R.color.white));
        customer_barChart.getXAxis().setLabelCount(13, false);
        customer_barChart.getXAxis().setDrawGridLines(false);

        customer_barChart.getXAxis().setGranularity(1f);
        customer_barChart.setPinchZoom(false);
        //todo previous type of adding xaxis
        //  customer_barChart.getXAxis().setGranularityEnabled(true);
//        customer_barChart.getXAxis().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                if (value == 0) {
//                    return ">90";
//                } else {
//                    return xvalues.get((int) value);
//                }
//
//            }
//        });

        customer_barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(MainActivity_B2C.ReceivableentriesXaxis));


        YAxis yAxis = customer_barChart.getAxisLeft();
        yAxis.setTextColor(getResources().getColor(R.color.white));
        yAxis.setAxisMinimum(0f);
        yAxis.setEnabled(true);

        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                axis.setLabelCount(3, true);
                return "" + Globals.convertToLakhAndCroreFromFloat(value);
            }
        });

        customer_barChart.setTouchEnabled(true);
        customer_barChart.setDrawBarShadow(true);


        //hide grid lines
        customer_barChart.getAxisLeft().setDrawGridLines(false);


        //remove right y-axis
        customer_barChart.getAxisRight().setEnabled(false);

        //remove legend


        //remove description label
        customer_barChart.getDescription().setEnabled(false);

        //add animation
        customer_barChart.animateY(2000);
        CustomMarkerViewReceivables markerView = new CustomMarkerViewReceivables(this, R.layout.barchart_marker, MainActivity_B2C.ReceivableValueForMarker);
        customer_barChart.setMarker(markerView);


        //draw chart
        customer_barChart.invalidate();


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
        getMenuInflater().inflate(R.menu.transaction_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.search:
                Toast.makeText(this, "click new ", Toast.LENGTH_SHORT).show();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        //   Toast.makeText(this, "call", Toast.LENGTH_SHORT).show();

    }
}