package com.cinntra.ledure.fragments;


import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;
import static com.cinntra.ledure.globals.Globals.numberToK;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.CreditNoteDashboard;
import com.cinntra.ledure.activities.LedgerReports;
import com.cinntra.ledure.activities.Sale_Group_Inovice_Reports;

import com.cinntra.ledure.adapters.ItemOnStockGroupAdapter;
import com.cinntra.ledure.adapters.LedgerAdapter_GroupZone;
import com.cinntra.ledure.adapters.LedgerGroupWiseAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.model.Customers_Report;
import com.cinntra.ledure.model.DashboardCounterResponse;
import com.cinntra.ledure.model.DataLedgerGroup;

import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.ResponseItemFilterDashboard;
import com.cinntra.ledure.webservices.NewApiClient;
import com.cinntra.roomdb.ItemsFilterDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ledger_CompGroup_Fragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private static final String TAG = "Ledger_CompGroup_Fragme";

    ArrayList<Customers_Report> customerList = new ArrayList<>();

    @BindView(R.id.customers_recyclerview)
    RecyclerView customerRecyclerView;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.sales_amount)
    TextView sales_amount;
    @BindView(R.id.pending_amount_value)
    TextView pending_amount_value;

    @BindView(R.id.all_customer)
    TextView all_customer;

    @BindView(R.id.no_datafound)
    ImageView no_datafound;

    @BindView(R.id.searchLay)
    RelativeLayout searchLay;

    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.cardCredit)
    LinearLayout cardCredit;

    @BindView(R.id.all_img)
    ImageView all_img;
    TextView salesvalue;
    TextView from_to_date;
    Spinner type_dropdown;
    Spinner type_group;
    String reportType = "Gross";
    String groupType = "";
    String orderType = "";

    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();
    LedgerAdapter_GroupZone adapter;
    LedgerGroupWiseAdapter ledgerGroupWiseAdapter;
    Toolbar toolbar;
    String fromWhere;
    String groupCode;
    String groupCodePdf;
    String groupFIlter;
    String intentGroupName = "";
    private String searchTextValue = "";
    private String startDateReverseFormat = "";
    private String endDateReverseFormat = "";

    String[] data = {"Gross", "Net"};

    public Ledger_CompGroup_Fragment() {
        // doesn't do anything special
    }

    boolean nextTime = false;

    public Ledger_CompGroup_Fragment(TextView salesvalue, TextView from_to_date,
                                     Spinner type_dropdown, Spinner type_group, String fromWhere, String groupCode, String groupFIlter) {
        // Required empty public constructor
        this.salesvalue = salesvalue;
        this.from_to_date = from_to_date;
        this.type_dropdown = type_dropdown;
        this.type_group = type_group;
        this.fromWhere = fromWhere;
        this.groupCode = groupCode;
        this.groupFIlter = groupFIlter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        if (getArguments() != null) {
            Bundle b = getArguments();
            customerList = (ArrayList<Customers_Report>) b.getSerializable(Globals.LedgerCompanyData);
        }


    }

    private ItemsFilterDatabase itemsFilterDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.ledger_item_layout, container, false);
        ButterKnife.bind(this, v);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        from_to_date = (TextView) getActivity().findViewById(R.id.from_to_date);
        itemsFilterDatabase = ItemsFilterDatabase.getDatabase(getActivity());

        Log.e("FROMWHEREGroup====>>", "onCreateView:" + fromWhere);
        //    groupType = getArguments().getString("group");
        ;
        //   groupCode = getArguments().getString("code");
        ;

        intentGroupName = getActivity().getIntent().getStringExtra("groupname");

        //toolbar.inflateMenu(R.menu.transaction_menu);
        toolbar.setOnMenuItemClickListener(this);
        loader.setVisibility(View.VISIBLE);

        //todo visible order by drop down
        if (groupFIlter.equalsIgnoreCase("Zone")) {
            //  type_group.setVisibility(View.VISIBLE);
            if (Prefs.getBoolean(Globals.ISPURCHASE, true)) {
                ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.order_by_list_by_zone_purchase, R.layout.spinner_textview_dashboard);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                type_group.setAdapter(spinnerArrayAdapter);
            }else {
                ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.order_by_list_by_zone, R.layout.spinner_textview_dashboard);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                type_group.setAdapter(spinnerArrayAdapter);
            }

            //   all_customer.setVisibility(View.VISIBLE);

        } else {
            // type_group.setVisibility(View.GONE);
            //  all_customer.setVisibility(View.INVISIBLE);
        }


        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");
        startDateReverseFormat = Globals.convertDateFormat(startDate);
        endDateReverseFormat = Globals.convertDateFormat(endDate);

        from_to_date.setText(startDateReverseFormat + " to " + endDateReverseFormat);

        from_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateBottomSheetDialog(requireContext());
            }
        });

        reportType = Prefs.getString(Globals.GROSS_NET, "Gross");

        AllitemsList = new ArrayList<>();
        allGroupSaleList = new ArrayList<>();
        customerRecyclerView.addOnScrollListener(scrollListener);
        Log.e("GroupInvoice==>>>", "onCreateView: " + groupCode + groupFIlter);
        callDashboardCounter();
//        callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
        groupCodePdf = groupCode.replaceAll(" ", "%20");

        url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
        Log.e(TAG, "showDateBottomSheetDialog:pdfURLoncreateBBB===>> " + url);


        all_img.setOnClickListener(view -> {
            // openpopup(requireContext(),view);
        });


        cardCredit.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CreditNoteDashboard.class);
            intent.putExtra("Type", reportType);
            intent.putExtra("filter", groupFIlter);
            intent.putExtra("code", groupCode);
            startActivity(intent);

        });


        ArrayAdapter typedropDownAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.type_gross_net, // Replace with your item array resource
                R.layout.spinner_white_textview);
        typedropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_dropdown.setAdapter(typedropDownAdapter);


        if (Globals.findPositionInStringArray(data, reportType) != -1) {
            // The string was found, so you can set the Spinner selection
            type_dropdown.setSelection(Globals.findPositionInStringArray(data, reportType));
        } else {
            type_dropdown.setSelection(0);
            // Handle the case where the string is not found
        }

        searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            }
        });

        searchView.setQueryHint("search customer");


        SearchViewUtils.setupSearchView(searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("QUERY>>>>>>>", "BHupi: " + query);
                searchView.clearFocus();
                searchTextValue = query;
                searchView.setQueryHint("Search Customer");
                pageNo = 1;


                // callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);


                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }

                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                Log.e("QUERY>>>>>>>", "BHupi: " + newText);
                searchView.clearFocus();
                searchTextValue = newText;
                searchView.setQueryHint("Search Customer");
                pageNo = 1;
//                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);

                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }

            }
        });


        type_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // reportType = type_dropdown.getSelectedItem().toString();
                startDatelng = (long) 0.0;
                endDatelng = (long) 0.0;
                if (nextTime)
                    reportType = type_dropdown.getSelectedItem().toString();
                loader.setVisibility(View.VISIBLE);

                callDashboardCounter();
//                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
                Log.e(TAG, "onItemSelected: " + reportType);
                url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

                nextTime = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        type_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                groupType = type_group.getSelectedItem().toString();
                orderType = type_group.getSelectedItem().toString();
                searchTextValue = "";
                pageNo = 1;

                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        return v;
    }


    List<BusinessPartnerData> AllitemsList;
    List<DataLedgerGroup> allGroupSaleList;
    LinearLayoutManager layoutManager;


    private void callDashboardCounter() {
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        HashMap obj = new HashMap<String, String>();
/*        obj.put("Filter", groupFIlter);
        obj.put("Code", groupCode);
        obj.put("Type", reportType);
        obj.put("FromDate", startDate);
        obj.put("ToDate", endDate);
        obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));*/

        Call<DashboardCounterResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            obj.put("Filter", groupFIlter);
            obj.put("Code", groupCode);
            obj.put("Type", reportType);
            obj.put("FromDate", startDate);
            obj.put("ToDate", endDate);
            obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
            obj.put("SearchText", "");
            obj.put("OrderByName", "");
            obj.put("OrderByAmt", "");
            obj.put("PageNo", "");
            obj.put("MaxSize", "");
            obj.put("DueDaysGroup", "");
            call = NewApiClient.getInstance().getApiService(getActivity()).getDashBoardCounterForPurchaseLedger(obj);
        } else {
            obj.put("Filter", groupFIlter);
            obj.put("Code", groupCode);
            obj.put("Type", reportType);
            obj.put("FromDate", startDate);
            obj.put("ToDate", endDate);
            obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
            call = NewApiClient.getInstance().getApiService(getActivity()).getDashBoardCounterForLedger(obj);
        }
//        obj.put("code", endDate);


//        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService(getActivity()).getDashBoardCounterForLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    loader.setVisibility(View.GONE
                    );
                    //    alertDialog.dismiss();
                    //setCounter(response.body().getData().get(0));
                    if (response.body().getData().size() > 0) {
                        try {
                            try {
                                pending_amount_value.setText(getResources().getString(R.string.Rs) + " " + numberToK(response.body().getData().get(0).getTotalCreditNote()));
                            } catch (IllegalStateException e) {
                                Log.e(TAG, "onResponse: " + e.getMessage());
                            }
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }
                        double tatalSaleValue = Double.valueOf(response.body().getData().get(0).getTotalSales()) - Double.valueOf(response.body().getData().get(0).getTotalCreditNote());


                        try {
                            salesvalue.setText(requireContext().getResources().getString(R.string.Rs) + " " + numberToK(String.valueOf(tatalSaleValue)));
                            sales_amount.setText(getResources().getString(R.string.Rs) + " " + numberToK(response.body().getData().get(0).getTotalSales()));
                            pending_amount_value.setText(getResources().getString(R.string.Rs) + " " + numberToK(response.body().getData().get(0).getTotalCreditNote()));
                        } catch (IllegalStateException e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }

                    } else {

                        try {
                            salesvalue.setText(getResources().getString(R.string.Rs) + " " + 0);
                            sales_amount.setText(getResources().getString(R.string.Rs) + " " + 0);
                            pending_amount_value.setText(getResources().getString(R.string.Rs) + " " + 0);
                        } catch (IllegalStateException e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }

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


    ArrayList<DataItemFilterDashBoard> AllItemsGroupStockList = new ArrayList<>();
    ItemOnStockGroupAdapter stockGroupAdapter;

    //todo calling category selected api here---
    private void callApiGroupItemStock(String searchValue, String startDate, String endDate, String type) {
        pageNo = 1;
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);

        loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hde = new HashMap<>();
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchValue);
        hde.put("FromDate", startDate);
        hde.put("ToDate", endDate);
        hde.put("Zone", groupCode);
        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put(Globals.payLoadOrderByName, orderBYName);
        hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

        try {
            Call<ResponseItemFilterDashboard> call;
            if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                call = NewApiClient.getInstance().getApiService(getActivity()).getFilterGroupItemStockPurchase(hde);
            } else {
                call = NewApiClient.getInstance().getApiService(getActivity()).getFilterGroupItemStock(hde);
            }

            call.enqueue(new Callback<ResponseItemFilterDashboard>() {
                @Override
                public void onResponse(Call<ResponseItemFilterDashboard> call, Response<ResponseItemFilterDashboard> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            //  AllitemsList.clear();
                            AllItemsGroupStockList.clear();
                            if (response.body().getData().size() > 0) {
                                no_datafound.setVisibility(View.GONE);
                                AllItemsGroupStockList.addAll(response.body().getData());
                                itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);

                            } else {
                                no_datafound.setVisibility(View.VISIBLE);
                            }

                            try {
                                all_customer.setText(getResources().getString(R.string.grp_stock));
                            } catch (Resources.NotFoundException e) {
                                Log.e(TAG, "run: " + e.getMessage());
                            }

                            loader.setVisibility(View.GONE);
                            // setData(response.body().getData().get(0));
                            try {
                                stockGroupAdapter = new ItemOnStockGroupAdapter(requireContext(), AllItemsGroupStockList, "zoneSub", groupCode);
                            } catch (Exception e) {
                                Log.e(TAG, "runGRoupFIlter: " + e.getMessage());
                            }
                            try {
                                stockGroupAdapter.AllData(AllItemsGroupStockList);
                            } catch (Exception e) {

                            }
                            try {
                                layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
                                customerRecyclerView.setLayoutManager(layoutManager);
                                customerRecyclerView.setAdapter(stockGroupAdapter);
                                stockGroupAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                Log.e(TAG, "runGroupLINEAR: " + e.getMessage());
                            }


                        } else {
                            Globals.showMessage(getContext(), response.message());
                        }
                    } else {
                        Globals.showMessage(getContext(), response.message());
                    }

                }

                @Override
                public void onFailure(Call<ResponseItemFilterDashboard> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "callApiGroupItemStock: " + e.getMessage());
        }

    }


    //todo pagination api here for category---
    private void callAllPagesApi_GroupItemStock() {

        HashMap<String, String> hde = new HashMap<>();
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        hde.put("SearchText", searchTextValue);
        hde.put("FromDate", startDate);
        hde.put("ToDate", endDate);
        hde.put("Zone", groupCode);
        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put(Globals.payLoadOrderByName, orderBYName);
        hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

        try {
            Call<ResponseItemFilterDashboard> call;
            if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                call = NewApiClient.getInstance().getApiService(getActivity()).getFilterGroupItemStockPurchase(hde);
            } else {
                call = NewApiClient.getInstance().getApiService(getActivity()).getFilterGroupItemStock(hde);
            }
            call.enqueue(new Callback<ResponseItemFilterDashboard>() {
                @Override
                public void onResponse(Call<ResponseItemFilterDashboard> call, Response<ResponseItemFilterDashboard> response) {
                    if (response.body().getStatus() == 200) {

                        if (response.body().getData().size() > 0) ;
                        AllItemsGroupStockList.addAll(response.body().getData());
                        stockGroupAdapter.AllData(AllItemsGroupStockList);
                        itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);
                        loader.setVisibility(View.GONE);
                        // setData(response.body().getData().get(0));
                        stockGroupAdapter.notifyDataSetChanged();
                        try {
                            all_customer.setText(getResources().getString(R.string.grp_stock));
                        } catch (Resources.NotFoundException e) {
                            Log.e(TAG, "run: " + e.getMessage());
                        }

                    } else if (response.body().getStatus() == 201) {
                        Globals.showMessage(getContext(), response.body().getMessage());
                    } else {
                        loader.setVisibility(View.GONE);
                        Log.e(TAG, "onFailure: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseItemFilterDashboard> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: " + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "callAllPagesApi_GroupItemStock: " + e.getMessage());
        }


    }


    String orderBYName = "";
    String orderBYAmt = "";


    private void callNewLedgerOneapi(String reportType, String startDate, String endDate, String filter, String groupCode) {
        Log.e(TAG, "callNewLedgerapi: calling");
        pageNo = 1;

        loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hde = new HashMap<>();
        hde.put("Type", reportType);
        hde.put("FromDate", startDate);
        hde.put("ToDate", endDate);
        hde.put("Filter", filter);
        hde.put("Code", groupCode);
        hde.put("SearchText", searchTextValue);
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        // hde.put("Zone", groupCode);
        hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        hde.put(Globals.payLoadOrderByName, orderBYName);
        hde.put(Globals.payLoadOrderByAMt, orderBYAmt);


        try {
            Call<CustomerBusinessRes> call;

            if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                call = NewApiClient.getInstance().getApiService(getActivity()).getledgerlistPurchasePost(hde);

            } else {
                call = NewApiClient.getInstance().getApiService(getActivity()).getledgerlistPost(hde);
            }
//                Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).getledgerlistPost(hde);

            call.enqueue(new Callback<CustomerBusinessRes>() {
                @Override
                public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            //   salesvalue.setText("Rs." + response.body().getTotalSales());
                            layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                            customerRecyclerView.setLayoutManager(layoutManager);
                            AllitemsList.clear();
                            AllitemsList.addAll(response.body().getData());
                            try {
                                try {
                                    try {
                                        try {
                                            all_customer.setText(getResources().getString(R.string.all_customers));
                                        } catch (IllegalStateException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (IllegalStateException e) {
                                        e.printStackTrace();
                                    }
                                } catch (Resources.NotFoundException e) {

                                }
                            } catch (Resources.NotFoundException e) {
                                Log.e(TAG, "run: " + e.getMessage());
                            }
                            if (response.body().getData().size() == 0) {
                                no_datafound.setVisibility(View.VISIBLE);
                            } else {
                                no_datafound.setVisibility(View.GONE);

                            }
                            Prefs.putString(Globals.GROSS_NET, reportType);
                            adapter = new LedgerAdapter_GroupZone(getActivity(), AllitemsList);
                            customerRecyclerView.setAdapter(adapter);
                            loader.setVisibility(View.GONE);
                            // Update UI element here
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                    loader.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            loader.setVisibility(View.GONE);
            Log.e(TAG, "callNewLedgerOneapi: " + e.getMessage());
        }

    }


    private void callledgerAllPageApi(String reportType, String startDate, String endDate, String filter, String groupCode) {
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", filter);
                hde.put("Code", groupCode);
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                // hde.put("Zone", groupCode);
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<CustomerBusinessRes> call;

                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getledgerlistPurchasePost(hde);

                } else {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getledgerlistPost(hde);
                }

//                Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).getledgerlistPost(hde);
                try {
                    Response<CustomerBusinessRes> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                sales_amount.setText("Rs." + response.body().getTotalSales());
//                                salesvalue.setText("Rs." + response.body().getTotalSales());
//                                layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//                                customerRecyclerView.setLayoutManager(layoutManager);
//                                AllitemsList.clear();

                                AllitemsList.addAll(response.body().getData());
                                Log.e(TAG, "run: " + AllitemsList.size());
                                adapter.AllData(AllitemsList);
                                adapter.notifyDataSetChanged();


                                if (response.body().getData().size() == 0) {
                                    pageNo++;
                                    //  no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    //  no_datafound.setVisibility(View.GONE);

                                }

//                                adapter = new LedgerAdapter(getActivity(), AllitemsList);
//                                customerRecyclerView.setAdapter(adapter);
                                loader.setVisibility(View.GONE);
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

    private void callledgerapi(String reportType, String startDate, String endDate, String filter, String groupCode) {
        Log.e(TAG, "callLedgerapi: calling");
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", filter);
                hde.put("Code", groupCode);
                hde.put("SearchText", searchTextValue);
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);
                Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).getledgerlistPost(hde);
                try {
                    Response<CustomerBusinessRes> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                sales_amount.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(response.body().getTotalSales()));
                                salesvalue.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(response.body().getTotalSales()));
                                layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                customerRecyclerView.setLayoutManager(layoutManager);
                                //AllitemsList.clear();
                                AllitemsList.addAll(response.body().getData());
                                all_customer.setText(getResources().getString(R.string.all_customers));
                                if (response.body().getData().size() == 0) {
                                    no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    no_datafound.setVisibility(View.GONE);

                                }

                               /* adapter = new LedgerAdapter(getActivity(), AllitemsList);
                                customerRecyclerView.setAdapter(adapter);*/
                                adapter.AllData(AllitemsList);
                                adapter.notifyDataSetChanged();
                                loader.setVisibility(View.GONE);
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


    int pageNo = 1;
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
            boolean shouldPaginate =
                    isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotaolMoreThanVisible
                            && isScrollingpage;

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                pageNo++;

                loader.setVisibility(View.VISIBLE);

                if (orderType.equalsIgnoreCase("Category")) {
                    callAllPagesApi_GroupItemStock();
//                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callledgerAllPageApi(reportType, startDate, endDate, groupFIlter, groupCode);
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

    private void callLedgerOnePageInvoice(String reportType, String startDate, String endDate) {
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).payment_collection_invoiceBases(hde);
                try {
                    Response<CustomerBusinessRes> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // pending_amount_value .setText("Rs." + Globals.numberToK(String.valueOf(response.body().getDifferenceAmount())));
                                sales_amount.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalReceivePayment())));
                                salesvalue.setText(getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalSales())));
                                AllitemsList.clear();
                                AllitemsList.addAll(response.body().getData());
                                Log.e(TAG, "run: " + AllitemsList.size());

                                adapter = new LedgerAdapter_GroupZone(getActivity(), AllitemsList);
                                customerRecyclerView.setAdapter(adapter);
                                customerRecyclerView.setLayoutManager(layoutManager);
                                loader.setVisibility(View.GONE);
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


    }


    Menu menuOut;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menuOut = menu;
        inflater.inflate(R.menu.transaction_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.search1);
        MenuItem share = menu.findItem(R.id.share_received);
        MenuItem amountFilter = menu.findItem(R.id.filterAmount);
        MenuItem dateFilter = menu.findItem(R.id.filterDate);
        MenuItem ledger = menu.findItem(R.id.ledger);
        MenuItem ledgermenu = menu.findItem(R.id.ledger_menu);
        ledger.setVisible(false);
        ledgermenu.setVisible(false);
        item.setVisible(false);
        amountFilter.setVisible(true);
        dateFilter.setVisible(false);
        //  share.setVisible()

//
//                //   MenuItem itemCalendar = menu.findItem(R.id.calendar);
        SearchView searchView = new SearchView(((Sale_Group_Inovice_Reports) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                pageNo = 1;
                searchTextValue = query;
                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
//                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("TAG", "onQueryTextChange: changed");
                Log.e("TAG", "" + newText);
//                if (adapter != null)
//                    adapter.filter(newText);
                searchTextValue = newText;

                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
                return true;
            }
        });

        toolbar.addView(searchView);
    }


    private boolean mSomeValue = true;

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.calendar:
                // Globals.selectDat(this);
                //Toast.makeText(requireContext(), "sales", Toast.LENGTH_SHORT).show();
                menuItem.setChecked(!menuItem.isChecked());
                showDateBottomSheetDialog(requireContext());


                break;
            case R.id.salebases:
                // Globals.selectDat(this);
                startDatelng = (long) 0.0;
                endDatelng = (long) 0.0;
                //reportType =type_dropdown.getSelectedItem().toString();
                // dateRangeSelector();
                //  callledgerapi(reportType,"","");
                break;
            case R.id.invoicebases:
                // Globals.selectDat(this);
                startDatelng = (long) 0.0;
                endDatelng = (long) 0.0;
                reportType = type_dropdown.getSelectedItem().toString();
                callLedgerOnePageInvoice(reportType, startDate, endDate);
                break;

            case R.id.ledger_menu:
                Prefs.putString("where", "Customer");
                Prefs.putString(Globals.FROM_DATE, startDate);
                Prefs.putString(Globals.TO_DATE, endDate);
                Intent intent = new Intent(getActivity(), LedgerReports.class);
                // intent.putExtra("cardCode", cardCode);
                intent.putExtra("where", "customer");
                intent.putExtra("filter", groupFIlter);
                intent.putExtra("code", groupCode);
                startActivity(intent);
                break;

            case R.id.share_sales:

                shareLedgerData();
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.filterAtoZ:
                // Globals.selectDat(this);

                /***shubh****/


                orderBYName = Globals.ATOZ;
                orderBYAmt = "";
                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
//                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                menuItem.setChecked(!menuItem.isChecked());


                break;

            case R.id.filterZtoA:
                orderBYName = Globals.ZTOA;
                orderBYAmt = "";
                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
//                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.search1:
                //  adapter.sortingA2Z("ZtoA");
                if (searchLay.getVisibility() == View.GONE) {
                    searchLay.setVisibility(View.VISIBLE);
                } else {
                    searchLay.setVisibility(View.GONE);
                }


                break;

            case R.id.filterAmount:
                orderBYAmt = Globals.DESC;
                orderBYName = "";
                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
//                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                menuItem.setChecked(!menuItem.isChecked());

                break;


            case R.id.filterAmountAsc:
                orderBYAmt = Globals.ASC;
                orderBYName = "";
                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
//                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                menuItem.setChecked(!menuItem.isChecked());

                break;

            case R.id.filterDate:
                adapter.sortingByDate();


                break;

            case R.id.clearAllFilter:
                searchTextValue = "";
                searchView.setQuery("", false);
                callledgerapi(reportType, "", "", "", "");
                menuItem.setChecked(!menuItem.isChecked());


        }
        return false;
    }

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

    /***shubh****/
    WebView dialogWeb;
    String url;
    URL uri = null;
    String encodedString;

    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
        String title;
        url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        title = getString(R.string.share_customer_list);


        try {

            encodedString = URLEncoder.encode(url, "UTF-8");

            Log.e(TAG, "encodedString: " + encodedString);
            System.out.println(encodedString);
        } catch (UnsupportedEncodingException e) {
            // Handle encoding exception
            e.printStackTrace();
        }

        if (orderType.equalsIgnoreCase("Category")) {
            title = getString(R.string.grp_stock);
            //todo pdf
            if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                url = Globals.GroupStockReportsPurchasePdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + Globals.QUERY_SALEPERSON_CODE_PDF + Prefs.getString(Globals.SalesEmployeeCode,"")+"&zone="+groupCode;

            } else {
                url = Globals.GroupStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + Globals.QUERY_SALEPERSON_CODE_PDF + Prefs.getString(Globals.SalesEmployeeCode,"")+"&zone="+groupCode;

            }
        } else {
            title = getString(R.string.share_customer_list);
            //todo pdf
            if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                url = Globals.saleReportsPurchasePdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

            }else {
                url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

            }
        }


//        url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                "");
    }

    /***shubh****/
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");


        BottomSheetDialogShareReportBinding binding;
        binding = BottomSheetDialogShareReportBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());

        try {
            // Encode the string with URL encoding


            encodedString = URLEncoder.encode(url, "UTF-8");
            // The "UTF-8" parameter specifies the character encoding

            // Print the encoded string
            Log.e(TAG, "encodedString: " + encodedString);
            System.out.println(encodedString);
        } catch (UnsupportedEncodingException e) {
            // Handle encoding exception
            e.printStackTrace();
        }
        progressDialog.dismiss();
        url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        setUpWebViewDialog(binding.webViewBottomSheetDialog, url, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);


        bottomSheetDialog.show();

        binding.headingBottomSheetShareReport.setText(R.string.share_customer_list);

        binding.ivCloseBottomSheet.setOnClickListener(view -> {
            progressDialog.dismiss();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialog.dismiss();
            }
        });


        binding.linearWhatsappShare.setOnClickListener(view ->
        {
            String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));

            lab_pdf(dialogWeb, f_name);

        });


        binding.linearOtherShare.setOnClickListener(view ->
                {
                    String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
                    lab_other_pdf(dialogWeb, f_name);

                }
        );
        binding.linearGmailShare.setOnClickListener(view -> {

                    String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
                    lab_gmail_pdf(dialogWeb, f_name);
                }
        );

    }

    /***shubh****/
    private void lab_gmail_pdf(WebView webView, String f_name) {
        //  String path = Environment.getExternalStorageDirectory().getPath()+"/hana/";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(requireActivity(), webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                gmailShare(fileName);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }

    /***shubh****/
    private void gmailShare(String fName) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                requireContext(),
                requireActivity()
                        .getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(requireContext(), "File Not exist", Toast.LENGTH_SHORT).show();

        }
        //    Uri path = Uri.fromFile(file);
        //  Log.e("Path==>", path.toString());
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, apkURI);

        // share.setData(Uri.parse("mailto:" + recipientEmail));


        share.setPackage("com.google.android.gm");

        startActivity(share);


    }

    /***shubh****/
    private void lab_other_pdf(WebView webView, String f_name) {
        //  String path = Environment.getExternalStorageDirectory().getPath()+"/hana/";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(getActivity(), webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                otherShare(fileName);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }


    /***shubh****/
    private void whatsappShare(String fName) {
        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = null;
        try {
            apkURI = FileProvider.getUriForFile(
                    requireContext(),
                    getActivity()
                            .getPackageName() + ".FileProvider", file);
        } catch (Exception e) {
            Log.e("whatsapp", "showBottomSheetDialog: ");
            e.printStackTrace();
        }


        if (!file.exists()) {
            Toast.makeText(requireContext(), "File Not exist", Toast.LENGTH_SHORT).show();

        }
        //    Uri path = Uri.fromFile(file);
        //  Log.e("Path==>", path.toString());

        try {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, apkURI);
            if (isAppInstalled("com.whatsapp"))
                share.setPackage("com.whatsapp");
            else if (isAppInstalled("com.whatsapp.w4b"))
                share.setPackage("com.whatsapp.w4b");

            startActivity(share);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), " WhatsApp is not currently installed on your phone.", Toast.LENGTH_LONG).show();
        }

//        if (share.resolveActivity(requireActivity().getPackageManager()) != null) {
//            //startActivity(Intent.createChooser(intent, "Share PDF using"));
//            startActivity(share);
//        } else {
//            startActivity(Intent.createChooser(share, "Share PDF using"));
//        }

    }


    /***shubh****/
    private void otherShare(String fName) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                requireContext(),
                getActivity()
                        .getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(requireContext(), "File Not exist", Toast.LENGTH_SHORT).show();

        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, apkURI);


        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Share PDF using"));
        }
    }

    /***shubh****/
    private void lab_pdf(WebView webView, String f_name) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        //        try {
        //            if (!f.getParentFile().exists())
        //                f.getParentFile().mkdirs();
        //            if (!f.exists())
        //                f.createNewFile();
        //        } catch (IOException e) {
        //            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        //        }
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(requireActivity(), webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                whatsappShare(fileName);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }


    /***shubh****/
    private void setUpWebViewDialog(WebView webView, String url, Boolean isZoomAvailable, ProgressBar dialog, LinearLayout whatsapp, LinearLayout gmail, LinearLayout other) {

        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webView.getSettings().setBuiltInZoomControls(isZoomAvailable);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        // webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        // Setting we View Client
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.clearSslPreferences();
        whatsapp.setEnabled(false);
        gmail.setEnabled(false);
        other.setEnabled(false);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap btm) {
                super.onPageStarted(view, url, null);
                dialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // initializing the printWeb Object
                dialog.setVisibility(View.GONE);
                dialogWeb = webView;
                whatsapp.setEnabled(true);
                gmail.setEnabled(true);
                other.setEnabled(true);

            }
        });


        webView.loadUrl(url);
    }


    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(this.getActivity().getSupportFragmentManager(), "Tag_Picker");
       /* materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Log.e("FromDate=>",String.valueOf(selection));

            }
        });
*/
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                startDateReverseFormat = Globals.convertDateFormat(startDate);
                endDateReverseFormat = Globals.convertDateFormat(endDate);
                from_to_date.setText(startDate + " - " + endDate);

//                callledgerapi(reportType, startDate, endDate, "", "");
                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
            }
        });


    }


    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        binding.tvCustomDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            // Toast.makeText(context, "today", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
            dateRangeSelector();

        });

        binding.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }

            // callledgerapi(reportType, startDate, endDate, "", "");
            from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Globals.yesterDayCal().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(endDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);


//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }
            //   callledgerapi(reportType, startDate, endDate, "", "");
            from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);


//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }

            //   callledgerapi(reportType, startDate, endDate, "", "");
            from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();


            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);


//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }

            //   callledgerapi(reportType, startDate, endDate, "", "");

            from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);

//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }
            //    callledgerapi(reportType, startDate, endDate, "", "");
            from_to_date.setText(binding.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);


//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }

            //callledgerapi(reportType, startDate, endDate, "", "");
            from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);
//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }


            //   callledgerapi(reportType, startDate, endDate, "", "");
            from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);

//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }

            //   callledgerapi(reportType, startDate, endDate, "", "");
            from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        binding.tvLastYearTillDateBottomSheetSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatelng = Globals.lastyearCal().getTimeInMillis();
                endDatelng = Globals.thisyearCal().getTimeInMillis();
                startDate = Globals.lastYearFirstDate();
                endDate = Globals.getCurrentDateInLastFinancialYear();
                from_to_date.setText(startDate + " - " + endDate);
                Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
                loader.setVisibility(View.VISIBLE);
                callDashboardCounter();
                if (orderType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                }
//            callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
                url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
                Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);

//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, startDate, endDate, groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, startDate, endDate, "", "");
//            }

                //   callledgerapi(reportType, startDate, endDate, "", "");
                from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
                bottomSheetDialog.dismiss();
            }
        });


      /*  binding.tvLastYearTillDateBottomSheetSelectDate.setOnClickListener(view -> {

        });*/


        binding.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            if (orderType.equalsIgnoreCase("Category")) {
                callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            } else {
                callNewLedgerOneapi(reportType, startDate, endDate, groupFIlter, groupCode);
            }
//            callNewLedgerOneapi(reportType, "", "", groupFIlter, groupCode);
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupFIlter + "&FromDate=" + "" + "&ToDate=" + "" + "&Code=" + groupCodePdf + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e(TAG, "showDateBottomSheetDialog:pdfURL===>> " + url);
//            if (groupType.equalsIgnoreCase("Group")) {
//                callGroupledgerapi(reportType, "", "", groupType);
//
//            } else if (groupType.equalsIgnoreCase("Ledger")) {
//                callledgerapi(reportType, "", "", "", "");
//            }

            //  callledgerapi(reportType, "", "", "", "");
            from_to_date.setText(binding.tvAllBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

    private void openpopup(Context context, View view) {
// .addItem(new PowerMenuItem("New BP",R.drawable.ic_newbp, false)) // add an item.

        PowerMenu powerMenu = new PowerMenu.Builder(context)
                .addItem(new PowerMenuItem("A to Z", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Z to A", R.drawable.ic_filter_black, false)) // aad an item list.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(context, R.color.black))
                .setTextGravity(Gravity.START)
                .setTextSize(12)
                .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "poppins_regular.ttf"))
                .setSelectedTextColor(Color.BLACK)
                .setWidth(Globals.pxFromDp(context, 220f))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .build();
        powerMenu.showAsDropDown(view);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item

                switch (position) {
                    /*case 0:
                    Prefs.putString(Globals.BussinessPageType,"DashBoard");
                    Prefs.putString(Globals.AddBp,"");
                    startActivity(new Intent(MainActivity_B2C.this, AddBPCustomer.class));
                    break;*/
                    case 0:
                        Collections.sort(AllitemsList, new Comparator<BusinessPartnerData>() {
                            @Override
                            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
                                String s1 = item.getCardName();
                                String s2 = t1.getCardName();
                                return s1.compareToIgnoreCase(s2);

                            }

                        });
                        adapter.notifyDataSetChanged();

                        break;
                    case 1:
                        Collections.sort(AllitemsList, new Comparator<BusinessPartnerData>() {
                            @Override
                            public int compare(BusinessPartnerData item, BusinessPartnerData t1) {
                                String s1 = item.getCardName();
                                String s2 = t1.getCardName();
                                return s2.compareToIgnoreCase(s1);
                            }

                        });
                        adapter.notifyDataSetChanged();

                        break;


                }

                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
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