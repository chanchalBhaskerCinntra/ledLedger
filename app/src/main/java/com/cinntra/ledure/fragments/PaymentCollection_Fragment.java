package com.cinntra.ledure.fragments;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;

import static com.cinntra.ledure.globals.Globals.numberToK;

import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cinntra.ledure.R;

import com.cinntra.ledure.activities.Reports;
import com.cinntra.ledure.activities.Sale_Group_Inovice_Reports;
import com.cinntra.ledure.adapters.BillAdapter;

import com.cinntra.ledure.adapters.LedgerZoneWiseAdapter;
import com.cinntra.ledure.adapters.ReceivableCustomerAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogOverDueSelectDateBinding;

import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SearchViewUtils;

import com.cinntra.ledure.model.Customers_Report;
import com.cinntra.ledure.model.DashboardCounterResponse;

import com.cinntra.ledure.model.DataZoneGroup;
import com.cinntra.ledure.model.ReceivableCustomerData;
import com.cinntra.ledure.model.ReceivableResponse;
import com.cinntra.ledure.model.ResponseZoneGroup;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.cinntra.roomdb.ReceivableDatabase;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentCollection_Fragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    LinearLayoutManager layoutManager;

    private ReceivableDatabase db;

    ArrayList<Customers_Report> customerList = new ArrayList<>();

    @BindView(R.id.customers_recyclerview)
    RecyclerView customerRecyclerView;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.sales_amount)
    TextView sales_amount;
    @BindView(R.id.slaes_amount)
    TextView slaes_amount;
    @BindView(R.id.pending_amount)
    TextView pending_amount;
    @BindView(R.id.pending_amount_value)
    TextView pending_amount_value;
    @BindView(R.id.all_customer)
    TextView all_customer;
    @BindView(R.id.receive_pending_layout)
    LinearLayout receive_pending_layout;


    @BindView(R.id.radioReceivableGroup)
    RadioGroup radioReceivableGroup;

    @BindView(R.id.radioAll)
    RadioButton radioAll;

    @BindView(R.id.radio_zero_thirty)
    RadioButton radio_zero_thirty;

    @BindView(R.id.radioThritySixty)
    RadioButton radioThritySixty;

    @BindView(R.id.radioSixtyNinty)
    RadioButton radioSixtyNinty;

    @BindView(R.id.radioNonDue)
    RadioButton radioNonDue;


    @BindView(R.id.radioGreaterThenNinty)
    RadioButton radioGreaterThenNinty;


    LinearLayout linearDropDownLedger;

    @BindView(R.id.no_datafound)
    ImageView no_datafound;

    @BindView(R.id.swipeRefreshItem)
    SwipeRefreshLayout swipeRefreshItem;
    TextView salesvalue;
    TextView from_to_date;
    Spinner type_dropdown;
    Spinner groupby_dropdown;
    ;
    String reportType = "Gross";
    String startDate = "";
    String endDate = "";
    /***shubh****/
    WebView dialogWeb;
    String url;
    Toolbar toolbar;

    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;

    String searchTextValue = "";
    private LedgerZoneWiseAdapter ledgerZoneWiseAdapter;

    public PaymentCollection_Fragment(TextView salesvalue, TextView from_to_date, Spinner type_dropdown, Spinner groupby_dropdown) {
        // Required empty public constructor
        this.salesvalue = salesvalue;
        this.from_to_date = from_to_date;
        this.type_dropdown = type_dropdown;
        this.groupby_dropdown = groupby_dropdown;

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            customerList = (ArrayList<Customers_Report>) b.getSerializable(Globals.LedgerCompanyData);
        }


    }

    private static final String TAG = "PaymentCollection_Fragm";
    String groupType = "Customer";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.ledger_item_layout, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.transaction_menu);
        toolbar.setOnMenuItemClickListener(this);
        //   layoutManager = new LinearLayoutManager(requireContext());
        db = ReceivableDatabase.getDatabase(requireContext());
        loader.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(requireContext());

        slaes_amount.setText("Received Amount");
        pending_amount.setText("Pending Amount");
        receive_pending_layout.setVisibility(View.GONE);
        contentFrame.setBackground(getResources().getDrawable(R.drawable.background_white_noborder));
        //  from_to_date.setText("All");
        from_to_date.setOnClickListener(view -> {
            showDateBottomSheetDialog(requireContext());
        });


        radioReceivableGroup.setVisibility(View.VISIBLE);
        radioReceivableGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioAll:
                        startDate = "";
                        endDate = "";
                        pageNo = 1;
                        overDueFilter = "";
                        from_to_date.setText("All");
                        callDashboardCounter();
                        Prefs.putString(Globals.FROM_DATE_receivable, "All");
                        if (groupType.equals("Zone")) {
                            all_customer.setText("Zone");
                            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                        } else {
                            all_customer.setText("Customer");
                            callledgerOneapi(reportType, startDate, endDate);
                        }
                        break;
                    case R.id.radio_zero_thirty:
                        startDate = Globals.getTodaysDate();
                        endDate = Globals.getDateForReceivable(0);
                        pageNo = 1;
                        overDueFilter = "0";
                        Prefs.putString(Globals.FROM_DATE_receivable, "0");
                        callDashboardCounter();
                        if (groupType.equals("Zone")) {
                            all_customer.setText("Zone");
                            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                        } else {
                            all_customer.setText("Customer");
                            customerRecyclerView.setAdapter(adapter);
                            callledgerOneapi(reportType, startDate, endDate);
                        }
                        break;
                    case R.id.radioThritySixty:
                        startDate = Globals.getTodaysDate();
                        endDate = Globals.getDateForReceivable(-30);
                        pageNo = 1;

                        overDueFilter = "30";
                        Prefs.putString(Globals.FROM_DATE_receivable, "30");
                        callDashboardCounter();
                        if (groupType.equals("Zone")) {
                            all_customer.setText("Zone");
                            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                        } else {
                            all_customer.setText("Customer");
                            callledgerOneapi(reportType, startDate, endDate);
                        }
                        break;

                    case R.id.radioSixty:
                        startDate = Globals.getTodaysDate();
                        endDate = Globals.getDateForReceivable(-45);
                        pageNo = 1;

                        overDueFilter = "45";
                        Prefs.putString(Globals.FROM_DATE_receivable, "45");
                        callDashboardCounter();
                        if (groupType.equals("Zone")) {
                            all_customer.setText("Zone");
                            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                        } else {
                            all_customer.setText("Customer");
                            callledgerOneapi(reportType, startDate, endDate);
                        }
                        break;
                    case R.id.radioSixtyNinty:
                        startDate = Globals.getTodaysDate();
                        endDate = Globals.getDateForReceivable(-60);
                        pageNo = 1;
                        overDueFilter = "60";
                        Prefs.putString(Globals.FROM_DATE_receivable, "60");
                        callDashboardCounter();
                        if (groupType.equals("Zone")) {
                            all_customer.setText("Zone");
                            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                        } else {
                            all_customer.setText("Customer");
                            callledgerOneapi(reportType, startDate, endDate);
                        }
                        break;


                    case R.id.radioGreaterThenNinty:
                        startDate = Globals.getTodaysDate();
                        endDate = Globals.getDateForReceivable(-90);
                        pageNo = 1;
                        overDueFilter = "90";
                        Prefs.putString(Globals.FROM_DATE_receivable, "90");
                        callDashboardCounter();
                        if (groupType.equals("Zone")) {
                            all_customer.setText("Zone");
                            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                        } else {
                            all_customer.setText("Customer");
                            callledgerOneapi(reportType, startDate, endDate);
                        }
                        break;
                    case R.id.radioNonDue:
                        startDate = Globals.getTodaysDate();
                        endDate = Globals.getTodaysDate();
                        pageNo = 1;
                        overDueFilter = "-1";
                        Prefs.putString(Globals.FROM_DATE_receivable, "nondue");
                        callDashboardCounter();
                        if (groupType.equals("Zone")) {
                            all_customer.setText("Zone");
                            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                        } else {
                            all_customer.setText("Customer");
                            customerRecyclerView.setAdapter(adapter);
                            callledgerOneapi(reportType, startDate, endDate);

                        }

                        break;

                }

            }
        });


        if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("nondue")) {
            from_to_date.setText("Not due");
            overDueFilter = "-1";

        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("0")) {
            from_to_date.setText("0-30");
            overDueFilter = "0";
        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("30")) {
            from_to_date.setText(" 31-60");
            overDueFilter = "30";
        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("60")) {
            from_to_date.setText("61-90");
            overDueFilter = "60";

        }
        else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("90")) {
            from_to_date.setText(">90");
            overDueFilter = "90";

        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("All")) {
            from_to_date.setText("All");
            overDueFilter = "";
        } else {
            from_to_date.setText("All");
            overDueFilter = "";
        }

        // setSaleAdapter();
        setRecyclerViewAdapter();


        swipeRefreshItem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                if (Globals.checkInternet(getActivity())) {
                    pageNo = 1;
                    db.myDataDao().deleteAll();
                    ALlItemList.clear();
                    orderBYName = "";
                    orderBYAmt = "";
                    searchTextValue = "";
                    overDueFilter = "";
                    Prefs.putString(Globals.FROM_DATE_receivable, "All");
                    from_to_date.setText("All");
                    if (groupType.equalsIgnoreCase("Customer")) {
                        callledgerOneapi(reportType, startDate, endDate);
                    } else {
                        callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                    }


                    swipeRefreshItem.setRefreshing(false);
                }

                swipeRefreshItem.setRefreshing(false);


            }
        });

        url = Globals.overAllReceivable + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Filter=&Code=&DueDaysGroup=" + overDueFilter;
        linearDropDownLedger = (LinearLayout) getActivity().findViewById(R.id.linearDropDownLedger);
        linearDropDownLedger.setVisibility(View.VISIBLE);

        type_dropdown.setVisibility(View.GONE);
        groupby_dropdown.setVisibility(View.VISIBLE);

        if (Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("admin") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Director") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Accounts")) {
            ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.ledger_receivable_dropdown, // Replace with your item array resource
                    R.layout.spinner_textview_dashboard);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            groupby_dropdown.setAdapter(spinnerArrayAdapter);
        } else {

            ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.ledger_receivable_dropdown, // Replace with your item array resource
                    R.layout.spinner_textview_dashboard);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            groupby_dropdown.setAdapter(spinnerArrayAdapter);
        }

        groupby_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groupType = groupby_dropdown.getSelectedItem().toString();

                reportType = type_dropdown.getSelectedItem().toString();


                pageNo = 1;


                searchTextValue = "";
                searchView.setQuery("", false);
                if (groupType.equals("Zone")) {
                    all_customer.setText("Zone");

                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    all_customer.setText("Customer");

                    callledgerOneapi(reportType, startDate, endDate);
                }


          /*      if (autoCallGroup) {

                  //  groupTypeManager();
                }
              //  autoCallGroup = true;*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        type_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // reportType = type_dropdown.getSelectedItem().toString();
                startDatelng = (long) 0.0;
                endDatelng = (long) 0.0;
                // callledgerOneapi(type_dropdown.getSelectedItem().toString(), "", "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        customerRecyclerView.addOnScrollListener(scrollListener);

        searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchEnable();
            }
        });

        searchView.setQueryHint("search customer");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();

                searchTextValue = query;
                if (!searchTextValue.isEmpty()) {
                    pageNo = 1;
                    callledgerOneapi(reportType, startDate, endDate);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("QUERY>>>>>>>", "onQueryTextChange: " + newText);
                searchView.clearFocus();

                searchTextValue = newText;

                pageNo = 1;
                callledgerOneapi(reportType, startDate, endDate);


                return true;
            }
        });


        SearchViewUtils.setupSearchView(searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                searchTextValue = query;
                if (!searchTextValue.isEmpty()) {
                    pageNo = 1;
                    if (groupType.equals("Zone")) {
                        callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                    } else {
                        callledgerOneapi(reportType, startDate, endDate);
                    }
                }
                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                searchView.clearFocus();

                searchTextValue = newText;

                pageNo = 1;
                if (groupType.equals("Zone")) {
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    callledgerOneapi(reportType, startDate, endDate);
                }


            }
        });


        // now create the instance of the material date
        // picker


        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.calendar:
                // Toast.makeText(getActivity(), "Date Selector", Toast.LENGTH_SHORT).show();
                // Globals.selectDat(this);
                //showDateBottomSheetDialog(requireContext());
                // showDateBottomSheetDialog_60_30(requireContext());
                return true;

            case R.id.share_received:
                Toast.makeText(requireContext(), "UnAble To Share\nThis Section is Under Maintenance", Toast.LENGTH_SHORT).show();
                //   shareLedgerData();
                return true;


            case R.id.filterAtoZ:
                // Globals.selectDat(this);
                /***shubh****/
                orderBYName = Globals.ATOZ;
                orderBYAmt = "";

                if (groupType.equals("Zone")) {
                    all_customer.setText("Zone");
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    all_customer.setText("Customer");
                    callledgerOneapi(reportType, startDate, endDate);
                }

                //     adapter.sortingA2Z("AtoZ");


                break;

            case R.id.filterZtoA:
                orderBYName = Globals.ZTOA;
                orderBYAmt = "";
                if (groupType.equals("Zone")) {
                    all_customer.setText("Zone");
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    all_customer.setText("Customer");
                    callledgerOneapi(reportType, startDate, endDate);
                }
                // adapter.sortingA2Z("ZtoA");
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void shareLedgerData() {
        String title = getString(R.string.share_customer_list);

        if (groupType.equalsIgnoreCase("Customer")) {
            title = getString(R.string.share_customer_list);
            url = Globals.overAllReceivable + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Filter=&Code=&DueDaysGroup=" + overDueFilter;

        } else {
            title = getString(R.string.share_group_list);
            // SalesPersonCode=-1&FromDate=&ToDate=&DueDaysGroup=
            url = Globals.overAllLedger + "SalesPersonCode=" + Prefs.getString(Globals.SalesEmployeeCode, "") + "&FromDate=" + startDate + "&ToDate=" + endDate + "&DueDaysGroup=" + overDueFilter;

        }

        WebViewBottomSheetFragment addPhotoBottomDialogFragment = WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(), "");

    }


    String rupeeSymbol = "\u20B9";


    private void setRecyclerViewAdapter() {
        salesvalue.setText("\u20B9 " + Prefs.getString(Globals.Total_Receivables, "0"));
        //    salesvalue.setText("\u20B9 " + Prefs.getString(Globals.Total_Receivables, "0"));
        callDashboardCounter();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (db.myDataDao().getAll().size() > 0) {
                        ALlItemList.addAll(db.myDataDao().getAll());
                    } else {
                        if (Globals.checkInternet(requireContext())) {

                            callledgerOneapi(reportType, startDate, endDate);
                        }

                    }
                    adapter = new ReceivableCustomerAdapter(getActivity(), ALlItemList, "Receivable");
                    // layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    // customerRecyclerView.setLayoutManager(layoutManager);
                    customerRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("TAG", "run: " + e.getMessage());
                }
            }
        }).start();


    }


    private void callDashboardCounter() {
        // Prefs.putString(Globals.FROM_DATE, startDate);
        // Prefs.putString(Globals.TO_DATE, endDate);
        HashMap obj = new HashMap<String, String>();
        obj.put("Filter", "");
        obj.put("Code", "");
        obj.put("Type", reportType);
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put(Globals.payLoadDueDaysGroup, overDueFilter);
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService().getDashBoardCounterForLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    loader.setVisibility(View.GONE
                    );
                    //    alertDialog.dismiss();
                    //setCounter(response.body().getData().get(0));
                    try {
                        try {
                            salesvalue.setText(rupeeSymbol + " " + numberToK(response.body().getData().get(0).getDifferenceAmount()));
                            Prefs.putString(Globals.Total_Receivables, numberToK(response.body().getData().get(0).getDifferenceAmount()));
                        } catch (Resources.NotFoundException e) {
                            Log.e(TAG, "SLESVALUE=======>: " + e.getMessage());
                        }
                        pending_amount_value.setText(rupeeSymbol + " " + numberToK(response.body().getData().get(0).getDifferenceAmount()));
                        sales_amount.setText(rupeeSymbol + " " + numberToK(response.body().getData().get(0).getDifferenceAmount()));
                    } catch (Resources.NotFoundException e) {
                        Log.e(TAG, "ERRORRR=====>: " + e.getMessage());
                    }

                    //  lead_spiner.setAdapter(leadAdapter);
                    //  leadAdapter.notifyDataSetChanged();
                } else {
                    //    alertDialog.dismiss();

                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, LeadResponse.class);
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DashboardCounterResponse> call, Throwable t) {
                //   alertDialog.dismiss();
                loader.setVisibility(View.GONE);
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }


    // TODO: Rename and change types and number of parameters
   /* public static Bills_Fragment newInstance(String param1, String param2) {
        Bills_Fragment fragment = new Bills_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }*/


    String groupCode = "";
    List<DataZoneGroup> allZoneSaleList = new ArrayList<>();

    private void callGroupZoneOneApi(String reportType, String startDate, String endDate, String groupType) {
        loader.setVisibility(View.VISIBLE);
        Log.e("ZONE==>", "callGroupledgerOneApi: " + reportType + startDate + endDate + groupType);

        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        pageNo = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
                HashMap<String, String> hde = new HashMap<>();

                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", groupType);

                hde.put("SearchText", searchTextValue);
                hde.put("DueDaysGroup", overDueFilter);
                hde.put("OrderByName", orderBYName);
                hde.put("OrderByAmt", orderBYAmt);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));


                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ResponseZoneGroup> call = NewApiClient.getInstance().getApiService().getGroupReceivables(hde);
                try {
                    Response<ResponseZoneGroup> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //   sales_amount.setText("Rs." + response.body().getTotalSales());
                                //     salesvalue.setText("Rs." + response.body().getTotalSales());
                                layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                customerRecyclerView.setLayoutManager(layoutManager);

                                allZoneSaleList.clear();
                                if (response.body().data.size() > 0) {
                                    allZoneSaleList.addAll(response.body().getData());
                                    //  ledgerZoneDatabase.myDataDao().insertAll(allZoneSaleList);

                                }

                                if (response.body().getData().size() == 0) {
                                    no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    no_datafound.setVisibility(View.GONE);

                                }
                                Prefs.putString(Globals.GROSS_NET, reportType);

                                //if (searchTextValue.isEmpty() && Prefs.getString(Globals.FROM_DATE_receivable, "All").equalsIgnoreCase("All") )
                                {
                                    layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                    customerRecyclerView.setLayoutManager(layoutManager);
                                    ledgerZoneWiseAdapter = new LedgerZoneWiseAdapter(getActivity(), allZoneSaleList);
                                    ledgerZoneWiseAdapter.AllData(allZoneSaleList);
                                    customerRecyclerView.setAdapter(ledgerZoneWiseAdapter);
                                }


                                loader.setVisibility(View.GONE);
                                ledgerZoneWiseAdapter.notifyDataSetChanged();
                                ledgerZoneWiseAdapter.setOnItemClickListener(new LedgerZoneWiseAdapter.OnItemGroupClickListener() {
                                    @Override
                                    public void onItemGroupClick(int position, String code, String name) {
                                        Prefs.putString("ForReports", "ZoneRe");
                                        groupCode = code;
                                        Intent intent = new Intent(requireActivity(), Reports.class);
                                        intent.putExtra("group", groupType);
                                        intent.putExtra("code", code);
                                        intent.putExtra("groupname", name);
                                        startActivity(intent);


                                    }
                                });

                            }
                        });
                        // Handle successful response

                    } else {
                        loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }

    private void callGroupZoneAllPageApi(String reportType, String startDate, String endDate, String groupType) {
        Log.e("ZONE===>", "callGroupledgerAllPageApi: ");
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        loader.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
                HashMap<String, String> hde = new HashMap<>();
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", groupType);

                hde.put("SearchText", searchTextValue);
                hde.put("DueDaysGroup", overDueFilter);
                hde.put("OrderByName", orderBYName);
                hde.put("OrderByAmt", orderBYAmt);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));


                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

                Call<ResponseZoneGroup> call = NewApiClient.getInstance().getApiService().getGroupReceivables(hde);
                try {
                    Response<ResponseZoneGroup> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (response.body().data.size() > 0) {
                                    allZoneSaleList.addAll(response.body().getData());
                                    ledgerZoneWiseAdapter.AllData(allZoneSaleList);
                                    //ledgerZoneDatabase.myDataDao().insertAll(allZoneSaleList);
                                    ledgerZoneWiseAdapter.notifyDataSetChanged();
                                }

                                Log.e("TAG", "run: " + allZoneSaleList.size());
//                                if (response.body().getData().size() == 0) {
//                                    pageNo++;
//                                  //  no_datafound.setVisibility(View.VISIBLE);
//                                } else {
//                                   // no_datafound.setVisibility(View.GONE);
//
//                                }

//                                ledgerGroupWiseAdapter = new LedgerGroupWiseAdapter(getActivity(), allGroupSaleList);
//                                customerRecyclerView.setAdapter(ledgerGroupWiseAdapter);
                                loader.setVisibility(View.GONE);

                                ledgerZoneWiseAdapter.setOnItemClickListener(new LedgerZoneWiseAdapter.OnItemGroupClickListener() {

                                    @Override
                                    public void onItemGroupClick(int position, String code, String name) {
                                        Prefs.putString("ForReports", "ZoneRe");
                                        groupCode = code;
                                        Intent intent = new Intent(requireActivity(), Reports.class);
                                        intent.putExtra("group", groupType);
                                        intent.putExtra("code", code);
                                        intent.putExtra("groupname", name);
                                        startActivity(intent);


                                    }
                                });
                                // Update UI element here
                            }
                        });
                        // Handle successful response

                    } else {
                        loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    private void SearchEnable() {
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
    }


    Menu menu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu = menu;
        //menu.clear();
        inflater.inflate(R.menu.transaction_menu_filter, menu);

        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.search);
        MenuItem cal = menu.findItem(R.id.calendar);
        //MenuItem filterZtoA = menu.findItem(R.id.filterZtoA);
        cal.setVisible(false);
        SearchView searchView = new SearchView(((Reports) getContext()).getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search Orders");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        // filterZtoA.setChecked(true);
        toolbar.addView(searchView);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BillAdapter adapter = new BillAdapter(requireContext(), customerList);
        customerRecyclerView.setAdapter(adapter);

    }

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getActivity().getSupportFragmentManager(), "Tag_Picker");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                from_to_date.setText(startDate + " - " + endDate);
                loader.setVisibility(View.VISIBLE);
                callDashboardCounter();
                callledgerOneapi(reportType, startDate, endDate);
                url = Globals.overAllReceivable + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Filter=&Code=&DueDaysGroup=" + overDueFilter;
            }
        });


    }

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
            boolean shouldPaginate = isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotaolMoreThanVisible && isScrollingpage;

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                // pageNo++;
                loader.setVisibility(View.VISIBLE);
                if (ALlItemList.size() != 0 && groupType.equalsIgnoreCase("Customer")) {
                    pageNo = ALlItemList.size() / Globals.QUERY_PAGE_SIZE;
                }
                else if ((allZoneSaleList.size() != 0 && groupType.equalsIgnoreCase("Zone"))) {
                    pageNo = allZoneSaleList.size() / Globals.QUERY_PAGE_SIZE;
                }
                pageNo++;
                if (Globals.checkInternet(requireContext())) {
                    if (groupType.equals("Zone")) {
                        callGroupZoneAllPageApi(reportType, startDate, endDate, groupType);
                    } else {
                        callLedgerAllPageApi(reportType, startDate, endDate);
                    }

                }

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


    ReceivableCustomerAdapter adapter;
    int pageNo = 1;

    List<ReceivableCustomerData> ALlItemList = new ArrayList();
    String orderBYName = "";
    String orderBYAmt = "";

    String overDueFilter = "";

    private void callledgerOneapi(String reportType, String startDate, String endDate) {
        db.myDataDao().deleteAll();
        loader.setVisibility(View.VISIBLE);
        pageNo = 1;
//        Prefs.putString(Globals.FROM_DATE_receivable, startDate);
//        Prefs.putString(Globals.TO_DATE_Receivable, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", "");
                hde.put("Code", "");
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);
                hde.put(Globals.payLoadDueDaysGroup, overDueFilter);

                Call<ReceivableResponse> call = NewApiClient.getInstance().getApiService().receivable_dashboard_post(hde);
                try {
                    Response<ReceivableResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (response.code() == 200) {
                                    ALlItemList.clear();
                                    if (response.body().getData().size() > 0) {
                                        no_datafound.setVisibility(View.GONE);
                                        db.myDataDao().deleteAll();
                                        ALlItemList.clear();
                                        ALlItemList.addAll(response.body().getData());
                                        if (adapter != null)
                                            customerRecyclerView.setAdapter(adapter);
                                        if (searchTextValue.isEmpty() && Prefs.getString(Globals.FROM_DATE_receivable, "All").equalsIgnoreCase("All")) {
                                            db.myDataDao().deleteAll();
                                            db.myDataDao().insertAll(ALlItemList);
                                            //adapter = new ReceivableCustomerAdapter(getActivity(), response.body().getData(), "Receivable");
                                            layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                            customerRecyclerView.setLayoutManager(layoutManager);
                                            customerRecyclerView.setAdapter(adapter);
                                        }


                                    } else {
                                        no_datafound.setVisibility(View.VISIBLE);
                                    }


                                    loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));

                                    //setRecyclerViewAdapter();
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshItem.setRefreshing(false);


                                } else {
                                    no_datafound.setVisibility(View.VISIBLE);
                                }


                                loader.setVisibility(View.GONE);

                                //  adapter.notifyDataSetChanged();
                                swipeRefreshItem.setRefreshing(false);

                                //Update UI element here
                            }
                        });
                        // Handle successful response

                    } else {
                        swipeRefreshItem.setRefreshing(false);
                        loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    swipeRefreshItem.setRefreshing(false);
                    Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Handle exception
                }
            }
        }).start();
    }

    private void callLedgerAllPageApi(String reportType, String startDate, String endDate) {
        loader.setVisibility(View.VISIBLE);
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", "");
                hde.put("Code", "");
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);
                hde.put(Globals.payLoadDueDaysGroup, overDueFilter);
                Call<ReceivableResponse> call = NewApiClient.getInstance().getApiService().receivable_dashboard_post(hde);
                try {
                    Response<ReceivableResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0)
                                        ALlItemList.addAll(response.body().getData());
                                    adapter.AllData(ALlItemList);
                                    loader.setVisibility(View.GONE);
                                    if (searchTextValue.isEmpty() && Prefs.getString(Globals.FROM_DATE_receivable, "All").equalsIgnoreCase("All") && orderBYName.equalsIgnoreCase("a-z")) {
                                        db.myDataDao().deleteAll();
                                        db.myDataDao().insertAll(ALlItemList);
                                    }


                                    // setData(response.body().getData().get(0));
                                    adapter.notifyDataSetChanged();

                                }

                                //Update UI element here
                            }
                        });
                        // Handle successful response

                    } else {
                        loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.calendar:
                showDateBottomSheetDialog(requireContext());
                //showDateBottomSheetDialog(requireContext());
                break;
            case R.id.filter:
                // Globals.selectDat(this);
                //dateRangeSelector();
                break;
            case R.id.share_received:
                // Toast.makeText(requireContext(), "Unable To Share\nThis Section is Under Maintenance", Toast.LENGTH_SHORT).show();

                shareLedgerData();
                break;

            case R.id.filterAtoZ:
                // Globals.selectDat(this);
                /***shubh****/
                orderBYName = Globals.ATOZ;
                orderBYAmt = "";
                if (groupType.equals("Zone")) {
                    all_customer.setText("Zone");
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    all_customer.setText("Customer");
                    callledgerOneapi(reportType, startDate, endDate);
                }
                menuItem.setChecked(!menuItem.isChecked());

                // adapter.sortingA2Z("AtoZ");


                break;

            case R.id.filterZtoA:
                // adapter.sortingA2Z("ZtoA");
                pageNo = 1;
                orderBYName = Globals.ZTOA;
                orderBYAmt = "";
                if (groupType.equals("Zone")) {
                    all_customer.setText("Zone");
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    all_customer.setText("Customer");
                    callledgerOneapi(reportType, startDate, endDate);
                }
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.filterAmount:
                // adapter.sortingAmount();
                pageNo = 1;
                orderBYName = "";
                orderBYAmt = Globals.DESC;
                if (groupType.equals("Zone")) {
                    all_customer.setText("Zone");
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    all_customer.setText("Customer");
                    callledgerOneapi(reportType, startDate, endDate);
                }
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.filterAmountAsc:
                // adapter.sortingAmount();
                pageNo = 1;
                orderBYName = "";
                orderBYAmt = Globals.ASC;
                if (groupType.equals("Zone")) {
                    all_customer.setText("Zone");
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    all_customer.setText("Customer");
                    callledgerOneapi(reportType, startDate, endDate);
                }
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.clearAllFilter:
                searchTextValue = "";
                searchView.setQuery("", false);
                searchView.setFocusable(false);
                orderBYAmt = "";
                orderBYName = "";

                if (groupType.equals("Zone")) {
                    all_customer.setText("Zone");
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                } else {
                    all_customer.setText("Customer");
                    callledgerOneapi(reportType, startDate, endDate);
                }
                menuItem.setChecked(!menuItem.isChecked());
                break;


            case R.id.search:
                pageNo = 1;
                //  adapter.sortingA2Z("ZtoA");
                if (searchLay.getVisibility() == View.GONE) {
                    searchLay.setVisibility(View.VISIBLE);
                } else {
                    searchLay.setVisibility(View.GONE);
                }

                // Toast.makeText(requireContext(), "shubh", Toast.LENGTH_SHORT).show();
                break;


        }
        return false;
    }


    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogOverDueSelectDateBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogOverDueSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });


        binding.rbNonDue.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("nondue"));

        binding.rblessThan30.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("30"));
        binding.rblessThan0.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("0"));

        binding.rblessThan60.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("60"));

        binding.rbAll.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("All"));


        binding.rgOverDue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == binding.rbNonDue.getId()) {
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getTodaysDate();
                    pageNo = 1;
                    overDueFilter = "-1";
                    Prefs.putString(Globals.FROM_DATE_receivable, "nondue");
                    callDashboardCounter();
                    if (groupType.equals("Zone")) {
                        all_customer.setText("Zone");
                        callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                    } else {
                        all_customer.setText("Customer");
                        customerRecyclerView.setAdapter(adapter);
                        callledgerOneapi(reportType, startDate, endDate);

                    }
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("Non due");


                } else if (i == binding.rblessThan0.getId()) {
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(0);
                    pageNo = 1;
                    overDueFilter = "0";
                    Prefs.putString(Globals.FROM_DATE_receivable, "0");
                    callDashboardCounter();
                    if (groupType.equals("Zone")) {
                        all_customer.setText("Zone");
                        callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                    } else {
                        all_customer.setText("Customer");
                        customerRecyclerView.setAdapter(adapter);
                        callledgerOneapi(reportType, startDate, endDate);
                    }
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("0-30");

                } else if (i == binding.rblessThan30.getId()) {
                    Log.e(TAG, "onCheckedChanged: " + Globals.getDateForReceivable(-30));
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(-30);
                    pageNo = 1;
                    Prefs.putString(Globals.FROM_DATE_receivable, "30");
                    overDueFilter = "30";
                    callDashboardCounter();
                    if (groupType.equals("Zone")) {
                        all_customer.setText("Zone");
                        callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                    } else {
                        all_customer.setText("Customer");
                        callledgerOneapi(reportType, startDate, endDate);
                    }
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("31-60");

                } else if (i == binding.rblessThan60.getId()) {
                    Log.e(TAG, "onCheckedChanged: " + Globals.getDateForReceivable(-60));
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(-60);
                    pageNo = 1;
                    overDueFilter = "60";
                    Prefs.putString(Globals.FROM_DATE_receivable, "60");
                    callDashboardCounter();
                    if (groupType.equals("Zone")) {
                        all_customer.setText("Zone");
                        callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                    } else {
                        all_customer.setText("Customer");
                        callledgerOneapi(reportType, startDate, endDate);
                    }
                    from_to_date.setText("61-90");
                    bottomSheetDialog.dismiss();
                }else if (i == binding.rbGreaterThan90.getId()) {
                    Log.e(TAG, "onCheckedChanged: " + Globals.getDateForReceivable(-90));
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(-90);
                    pageNo = 1;
                    overDueFilter = "90";
                    Prefs.putString(Globals.FROM_DATE_receivable, "90");
                    callDashboardCounter();
                    if (groupType.equals("Zone")) {
                        all_customer.setText("Zone");
                        callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                    } else {
                        all_customer.setText("Customer");
                        callledgerOneapi(reportType, startDate, endDate);
                    }
                    from_to_date.setText(">90");
                    bottomSheetDialog.dismiss();
                } else if (i == binding.rbAll.getId()) {
                    startDate = "";
                    endDate = "";
                    pageNo = 1;
                    overDueFilter = "";
                    Prefs.putString(Globals.FROM_DATE_receivable, "All");
                    from_to_date.setText("All");
                    callDashboardCounter();
                    if (groupType.equals("Zone")) {
                        all_customer.setText("Zone");
                        callGroupZoneOneApi(reportType, startDate, endDate, groupType);
                    } else {
                        all_customer.setText("Customer");
                        callledgerOneapi(reportType, startDate, endDate);
                    }
                    bottomSheetDialog.dismiss();

                }
            }
        });
        bottomSheetDialog.show();

    }


    private boolean isAppInstalled(String packageName) {
        try {
            getActivity().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }


}