package com.cinntra.ledure.fragments;


import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;
import static com.cinntra.ledure.globals.Globals.numberToK;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.CreditNoteDashboard;
import com.cinntra.ledure.activities.LedgerReports;
import com.cinntra.ledure.activities.Reports;
import com.cinntra.ledure.activities.Sale_Group_Inovice_Reports;
import com.cinntra.ledure.adapters.ItemInSaleStockAdapter;
import com.cinntra.ledure.adapters.ItemOnStockGroupAdapter;
import com.cinntra.ledure.adapters.LedgerAdapter;
import com.cinntra.ledure.adapters.LedgerGroupWiseAdapter;
import com.cinntra.ledure.adapters.LedgerZoneWiseAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.model.Customers_Report;
import com.cinntra.ledure.model.DashboardCounterResponse;
import com.cinntra.ledure.model.DataItemInSalesCard;
import com.cinntra.ledure.model.DataLedgerGroup;
import com.cinntra.ledure.model.DataZoneGroup;
import com.cinntra.ledure.model.ResponseItemInSalesCard;
import com.cinntra.ledure.model.ResponseLedgerGroup;
import com.cinntra.ledure.model.ResponseZoneGroup;
import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.ResponseItemFilterDashboard;
import com.cinntra.ledure.webservices.NewApiClient;
import com.cinntra.roomdb.ItemsFilterDatabase;
import com.cinntra.roomdb.ItemsInSalesCardDatabase;
import com.cinntra.roomdb.LedgerGroupDatabase;
import com.cinntra.roomdb.LedgerZoneDatabase;
import com.cinntra.roomdb.SaleLedgerDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ledger_Comp_Fragment extends Fragment implements Toolbar.OnMenuItemClickListener {


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


    @BindView(R.id.cardCredit)
    LinearLayout cardCredit;


    @BindView(R.id.swipeRefreshItem)
    SwipeRefreshLayout swipeRefreshItem;

    @BindView(R.id.searchLay)
    RelativeLayout searchLay;

    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.all_img)
    ImageView all_img;
    @BindView(R.id._amount)
    TextView _amount;

    TextView salesvalue;
    TextView from_to_date;
    Spinner type_dropdown;
    Spinner type_group;
    String reportType = "Gross";
    String groupType = "";

    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();
    LedgerAdapter adapter;
    LedgerGroupWiseAdapter ledgerGroupWiseAdapter;
    LedgerZoneWiseAdapter ledgerZoneWiseAdapter;
    Toolbar toolbar;
    String fromWhere;
    String groupCode;
    String groupFIlter;

    String searchTextValue = "";
    String startDateReverseFormat = "";
    String endDateReverseFormat = "";
    String salePersonCode = "";

    Context context;

    // In your activity or fragment
    private View progressBarView;

    // Method to show the ProgressBar
    private void showProgressBar(Context context) {
        // Inflate the layout file
        LayoutInflater inflater = LayoutInflater.from(context);
        progressBarView = inflater.inflate(R.layout.progress_bar_full_screen, null);

        // Add the ProgressBar view to the root container
        ViewGroup rootContainer = getView().findViewById(android.R.id.content);
        rootContainer.addView(progressBarView);
    }


    public Ledger_Comp_Fragment() {
        // doesn't do anything special
    }

    public Ledger_Comp_Fragment(TextView salesvalue, TextView from_to_date,
                                Spinner type_dropdown, Spinner type_group,
                                String fromWhere, String groupCode, String groupFIlter) {


        this.fromWhere = fromWhere;
        this.groupCode = groupCode;
        this.groupFIlter = groupFIlter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private void setSaleAdapter() {
        callDashboardCounter();
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            db.myDataDao().deleteAll();
        } else {
            db.myDataDao().deleteAll();
        }
        AllitemsList = db.myDataDao().getAll();
        if (AllitemsList == null)
            AllitemsList = new ArrayList<>();
        else if (AllitemsList.size() > 0)
            loader.setVisibility(View.GONE);
        adapter = new LedgerAdapter(getActivity(), AllitemsList);

        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        customerRecyclerView.setLayoutManager(layoutManager);
        customerRecyclerView.setAdapter(adapter);

        pageNo_Sale = 1;
        if (AllitemsList.size() != 0)
            pageNo_Sale = AllitemsList.size() / Globals.QUERY_PAGE_SIZE;


        if (AllitemsList.size() == 0) {
            loader.setVisibility(View.VISIBLE);
            callledgerOneApi(reportType, startDate, endDate, "", "");
        }


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

    Boolean autoCall = false;
    Boolean autoCallGroup = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.ledger_item_layout, container, false);
        ButterKnife.bind(this, v);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        // startDate = Prefs.getString(Globals.FROM_DATE, "");
        //  endDate = Prefs.getString(Globals.TO_DATE, "");
        // reportType = Prefs.getString(Globals.GROSS_NET, "");

       /* orderBYAmt = startDate;
        orderBYName = startDate;*/
        loader.setVisibility(View.VISIBLE);

        salesvalue = (TextView) getActivity().findViewById(R.id.salesvalue);
        from_to_date = (TextView) getActivity().findViewById(R.id.from_to_date);
        type_dropdown = (Spinner) getActivity().findViewById(R.id.type_dropdown);
        type_group = (Spinner) getActivity().findViewById(R.id.groupby_dropdown);


        db = SaleLedgerDatabase.getDatabase(getActivity());
        ledgerGroupDatabase = LedgerGroupDatabase.getDatabase(getActivity());
        ledgerZoneDatabase = LedgerZoneDatabase.getDatabase(getActivity());
        itemsFilterDatabase = ItemsFilterDatabase.getDatabase(getActivity());
        itemsInSalesCardDatabase = ItemsInSalesCardDatabase.getDatabase(getActivity());
        salePersonCode = Prefs.getString(Globals.SalesEmployeeCode, "");

        String[] data = {"Gross", "Net"};
        // Log.e(TAG, "onCreateView: " + Globals.findPositionInStringArray(data, reportType));

        //todo new arrayadapter
     /*   ArrayAdapter typedropDownAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.type_gross_net, // Replace with your item array resource
                R.layout.spinner_textview_dashboard);
        typedropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_dropdown.setAdapter(typedropDownAdapter);*/

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


        setSaleAdapter();


        //toolbar.inflateMenu(R.menu.transaction_menu);
        toolbar.setOnMenuItemClickListener(this);
        loader.setVisibility(View.GONE);
        type_group.setVisibility(View.VISIBLE);

        type_dropdown.setClickable(false);
        type_dropdown.setFocusable(false);
        /*startDate=Prefs.getString(Globals.FROM_DATE,"");
        endDate=Prefs.getString(Globals.TO_DATE,"");*/
        if (startDate.isEmpty()) {
            from_to_date.setText("All");
        } else {
            from_to_date.setText("" + Globals.convertDateFormatInReadableFormat(startDate) +
                    "" + " To " + Globals.convertDateFormatInReadableFormat(endDate));
        }


        from_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateBottomSheetDialog(requireContext());
            }
        });


        if (Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("admin") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Director") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Accounts")) {
            ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.ledger_dropdown, // Replace with your item array resource
                    R.layout.spinner_textview_dashboard);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type_group.setAdapter(spinnerArrayAdapter);
        } else {

            ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.ledger_for_sale, // Replace with your item array resource
                    R.layout.spinner_textview_dashboard);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type_group.setAdapter(spinnerArrayAdapter);
        }


        cardCredit.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CreditNoteDashboard.class);
            intent.putExtra("Type", reportType);
            intent.putExtra("filter", "");
            intent.putExtra("code", "");
            startActivity(intent);

        });


        swipeRefreshItem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Globals.checkInternet(getActivity())) {
                    pageNo_Sale = 1;
                    db.myDataDao().deleteAll();
                    groupTypeManager();

                    swipeRefreshItem.setRefreshing(false);
                }

                swipeRefreshItem.setRefreshing(false);
            }
        });


        startDateReverseFormat = Globals.convertDateFormat(startDate);
        endDateReverseFormat = Globals.convertDateFormat(endDate);


        from_to_date.setText(startDateReverseFormat + " to " + endDateReverseFormat);
        if (startDate.isEmpty() && endDate.isEmpty()) {
            from_to_date.setText("All");
        }


        customerRecyclerView.addOnScrollListener(scrollListener);


        all_img.setOnClickListener(view -> {
            // openpopup(requireContext(),view);
        });

        type_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // reportType = type_dropdown.getSelectedItem().toString();
                startDatelng = (long) 0.0;
                endDatelng = (long) 0.0;
                pageNo = 1;
                reportType = type_dropdown.getSelectedItem().toString();
                //Prefs.putString(Globals.GROSS_NET, reportType);
                if (autoCall) {
                    groupTypeManager();
                }

                autoCall = true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        type_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groupType = type_group.getSelectedItem().toString();

                reportType = type_dropdown.getSelectedItem().toString();
//                type_dropdown.setClickable(true);
//                type_dropdown.setFocusable(true);

                pageNo = 1;
             /*   if (autoCallGroup) {

                    groupTypeManager();
                }
                autoCallGroup = true;*/

                groupTypeManager();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconifiedByDefault(true);
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
            }
        });

        if (groupType.equalsIgnoreCase("Group")) {
            searchView.setQueryHint("search Group");
        } else {
            searchView.setQueryHint("search customer");
        }


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchTextValue = "";
                if (groupType.equalsIgnoreCase("Group")) {
                    loader.setVisibility(View.VISIBLE);
                    //  searchView.setQueryHint("search Group");

                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Zone")) {
                    loader.setVisibility(View.VISIBLE);
                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Category")) { //Item Stock Group
                    all_customer.setText(getActivity().getResources().getString(R.string.grp_stock));
                    loader.setVisibility(View.VISIBLE);
                    type_dropdown.setVisibility(View.INVISIBLE);

                    type_dropdown.setOnItemSelectedListener(null);

                    _amount.setText("Nos");
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else {
                    loader.setVisibility(View.VISIBLE);
                    pageNo_Sale = 1;
                    callledgerOneApi(reportType, startDate, endDate, "", "");
                    //callledgerOneapi(reportType, startDate, endDate);

                }


                return false;
            }
        });


       /* SearchViewUtils.setupSearchView(searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("QUERY>>>>>>>", "BHupi: " + query);
                searchView.clearFocus();
                searchTextValue = query;
                if (groupType.equalsIgnoreCase("Group") && !searchTextValue.isEmpty()) {
                    loader.setVisibility(View.VISIBLE);
                    searchView.setQueryHint("search Group");

                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Zone") && !searchTextValue.isEmpty()) {
                    loader.setVisibility(View.VISIBLE);
                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Stock Group")) { //Item Stock Group
                    all_customer.setText(getActivity().getResources().getString(R.string.grp_stock));
                    loader.setVisibility(View.VISIBLE);
                    type_dropdown.setVisibility(View.INVISIBLE);

                    type_dropdown.setOnItemSelectedListener(null);

                    _amount.setText("Nos");
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else {
                    searchView.setQueryHint("search customer");
                    if (!searchTextValue.isEmpty()) {
                        loader.setVisibility(View.VISIBLE);
                        pageNo_Sale = 1;
                        callledgerOneApi(reportType, startDate, endDate, "", "");
                        //  callledgerOneapi(reportType, startDate, endDate);
                    }
                }

                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                searchView.clearFocus();
                searchTextValue = newText;
                if (groupType.equalsIgnoreCase("Group") && !searchTextValue.isEmpty()) {
                    loader.setVisibility(View.VISIBLE);
                    searchView.setQueryHint("search Group");

                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Zone") && !searchTextValue.isEmpty()) {
                    loader.setVisibility(View.VISIBLE);
                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Stock Group")) { //Item Stock Group
                    all_customer.setText(getActivity().getResources().getString(R.string.grp_stock));
                    loader.setVisibility(View.VISIBLE);
                    type_dropdown.setVisibility(View.INVISIBLE);
                    type_dropdown.setOnItemSelectedListener(null);
                    _amount.setText("Nos");
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else {
                    searchView.setQueryHint("search customer");
                    if (!searchTextValue.isEmpty()) {
                        loader.setVisibility(View.VISIBLE);
                        pageNo_Sale = 1;
                        callledgerOneApi(reportType, startDate, endDate, "", "");
                        //callledgerOneapi(reportType, startDate, endDate);
                    }
                }


            }
        }); */


        SearchViewUtils.setupSearchView(searchView, 900, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("QUERY>>>>>>>", "BHupi: " + query);
                searchView.clearFocus();
                searchTextValue = query;
                if (groupType.equalsIgnoreCase("Group")) {
                    loader.setVisibility(View.VISIBLE);
                    searchView.setQueryHint("search Group");

                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Zone")) {
                    loader.setVisibility(View.VISIBLE);
                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Category")) { //Stock Group
                    all_customer.setText(getActivity().getResources().getString(R.string.grp_stock));
                    loader.setVisibility(View.VISIBLE);
                    type_dropdown.setVisibility(View.INVISIBLE);

                    type_dropdown.setOnItemSelectedListener(null);

                    _amount.setText("Nos");
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else {
                    searchView.setQueryHint("search customer");
                    if (!searchTextValue.isEmpty()) {
                        loader.setVisibility(View.VISIBLE);
                        pageNo_Sale = 1;
                        callledgerOneApi(reportType, startDate, endDate, "", "");
                        //  callledgerOneapi(reportType, startDate, endDate);
                    }
                }

                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                searchView.clearFocus();
                searchTextValue = newText;
                if (groupType.equalsIgnoreCase("Group")) {
                    loader.setVisibility(View.VISIBLE);
                    searchView.setQueryHint("search Group");

                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Zone")) {
                    loader.setVisibility(View.VISIBLE);
                    pageNo = 1;
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Category")) { //Stock Group
                    all_customer.setText(getActivity().getResources().getString(R.string.grp_stock));
                    loader.setVisibility(View.VISIBLE);
                    type_dropdown.setVisibility(View.INVISIBLE);
                    type_dropdown.setOnItemSelectedListener(null);
                    _amount.setText("Nos");
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else {
                    searchView.setQueryHint("search customer");

                    loader.setVisibility(View.VISIBLE);
                    pageNo_Sale = 1;
                    callledgerOneApi(reportType, startDate, endDate, "", "");
                    //callledgerOneapi(reportType, startDate, endDate);

                }


            }
        });


        return v;
    }


    private void groupTypeManager_from_Calender() {
        if (groupType.equalsIgnoreCase("Group")) {
            loader.setVisibility(View.VISIBLE);
            callDashboardCounter();
            callGroupledgerOneApi(reportType, startDate, endDate, groupType);
            //  callDashboardCounter();
            url = Globals.saleGroupReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        } else if (groupType.equalsIgnoreCase("Customer")) {
            all_customer.setText(getActivity().getResources().getString(R.string.customer));
            loader.setVisibility(View.VISIBLE);
            callledgerOneApi(reportType, startDate, endDate, "", "");
            callDashboardCounter();
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        } else if (groupType.equalsIgnoreCase("Category")) { //Item Stock Group
            all_customer.setText(getActivity().getResources().getString(R.string.grp_stock));
            loader.setVisibility(View.VISIBLE);
            _amount.setText("Nos");
            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            //  callDashboardCounter();
            url = Globals.GroupStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + Globals.QUERY_SALEPERSON_CODE_PDF + salePersonCode;


        } else if (groupType.equalsIgnoreCase("Item Stock")) { //Item Stock
            all_customer.setText(getActivity().getResources().getString(R.string.item_stock));
            loader.setVisibility(View.VISIBLE);

            callApiItemStock(searchTextValue, startDate, endDate, groupType);
            //  callDashboardCounter();
            url = Globals.ItemStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        } else {
            callDashboardCounter();
            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
            //   callDashboardCounter();
            url = Globals.saleGroupReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + "" + "&ToDate=" + "" + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;

        }
    }

    private void groupTypeManager() {
        _amount.setText("Amount");
        type_dropdown.setEnabled(true);
        type_dropdown.setVisibility(View.VISIBLE);
        if (groupType.equalsIgnoreCase("Group") || groupFIlter.equalsIgnoreCase("Group")) {
            all_customer.setText(getActivity().getResources().getString(R.string.all_group));
            Log.e("typegroup==>>", "onCreateView: group ");
            loader.setVisibility(View.VISIBLE);
            callGroupledgerOneApi(reportType, startDate, endDate, groupType);
            callDashboardCounter();
            url = Globals.saleGroupReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        } else if (groupType.equalsIgnoreCase("Customer")) {
            //  pageNo=db.myDataDao().getAll().size()/Globals.QUERY_PAGE_SIZE;
            loader.setVisibility(View.VISIBLE);
            all_customer.setText(getActivity().getResources().getString(R.string.all_customers));
            Log.e("typegroup==>>", "onCreateView: ledger");
            setSaleAdapter();
            callledgerOneApi(reportType, startDate, endDate, "", "");
            callDashboardCounter();
            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        } else if (groupType.equalsIgnoreCase("Zone")) {
            all_customer.setText(getActivity().getResources().getString(R.string.all_zone));
            loader.setVisibility(View.VISIBLE);

            Log.e("typegroup==>>", "onCreateView: Zone");
            callGroupZoneOneApi(reportType, startDate, endDate, groupType);
            callDashboardCounter();
            url = Globals.saleGroupReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        } else if (groupType.equalsIgnoreCase("Category")) { //Item Stock Group
            all_customer.setText(getActivity().getResources().getString(R.string.grp_stock));
            loader.setVisibility(View.VISIBLE);
            type_dropdown.setVisibility(View.INVISIBLE);
            // type_dropdown.setSelection(1);
            //   type_dropdown.setClickable(false);
            //   type_dropdown.setFocusable(false);
            //  type_dropdown.setEnabled(false);
            type_dropdown.setOnItemSelectedListener(null);
            Log.e("typegroup==>>", "onCreateView: Zone");
            _amount.setText("Nos");
            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);
            callDashboardCounter();
            url = Globals.GroupStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + Globals.QUERY_SALEPERSON_CODE_PDF + salePersonCode;


        } else if (groupType.equalsIgnoreCase("Item Stock")) { //Item Stock
            all_customer.setText(getActivity().getResources().getString(R.string.item_stock));
            loader.setVisibility(View.VISIBLE);
            Log.e("typegroup==>>", "onCreateView: Zone");
            //  type_dropdown.setSelection(1);
            type_dropdown.setVisibility(View.INVISIBLE);
            // type_dropdown.setClickable(false);
            //  type_dropdown.setFocusable(false);
            //  type_dropdown.setEnabled(false);
            type_dropdown.setOnItemSelectedListener(null);
            callApiItemStock(searchTextValue, startDate, endDate, groupType);
            callDashboardCounter();
            url = Globals.ItemStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        }


    }


    List<BusinessPartnerData> AllitemsList = new ArrayList<>();
    List<DataLedgerGroup> allGroupSaleList = new ArrayList<>();
    List<DataZoneGroup> allZoneSaleList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    private SaleLedgerDatabase db;
    private LedgerGroupDatabase ledgerGroupDatabase;
    private LedgerZoneDatabase ledgerZoneDatabase;
    private ItemsFilterDatabase itemsFilterDatabase;
    private ItemsInSalesCardDatabase itemsInSalesCardDatabase;
    // public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition);

    private void callledgerOneApi(String reportType, String startDate, String endDate, String filter, String groupCode) {
        db.myDataDao().deleteAll();
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        pageNo_Sale = 1;
        loader.setVisibility(View.VISIBLE);

        // pageNo = 1;

        new Thread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", filter);
                hde.put("Code", groupCode);
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo_Sale));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<CustomerBusinessRes> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getledgerlistPurchasePost(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService().getledgerlistPost(hde);
                }

//                Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().getledgerlistPost(hde);
                try {
                    Response<CustomerBusinessRes> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                db.myDataDao().deleteAll();

                                Prefs.putString(Globals.GROSS_NET, reportType);
                                AllitemsList.clear();
                                AllitemsList.addAll(response.body().getData());
                                if (reportType.equalsIgnoreCase("Gross") && searchTextValue.isEmpty())
                                    setLedgerDB(response.body().getData());

                                Log.e("TAG", "run: " + AllitemsList.size());

                                if (response.body().getData().size() == 0) {
                                    no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    no_datafound.setVisibility(View.GONE);

                                }


                                Log.e(TAG, "run: " + db.myDataDao().getAll());
                                adapter.notifyDataSetChanged();
                                // countrylistFromLocal.addAll(AllitemsList);
//
                                loader.setVisibility(View.GONE);

                                if (adapter != null && adapter.getItemCount() == 0) {
                                    no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    no_datafound.setVisibility(View.GONE);

                                }
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

    private void setLedgerDB(List<BusinessPartnerData> arrayList) {

        List<BusinessPartnerData> localData = db.myDataDao().getAll();
        if (!localData.equals(arrayList)) {
            db.myDataDao().insertAll(arrayList);

        }
    }

    String totalSale = "0";
    String credit_return = "0";
    String Sale_AfterCal = "0";

    private void callDashboardCounter() {

        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        HashMap obj = new HashMap<String, String>();


        Call<DashboardCounterResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            obj.put("Filter", "");
            obj.put("Code", "");
            obj.put("Type", "Gross");
            obj.put("FromDate", startDate);
            obj.put("ToDate", endDate);
            obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
            obj.put("SearchText", "");
            obj.put("OrderByName", "");
            obj.put("OrderByAmt", "");
            obj.put("PageNo", "");
            obj.put("MaxSize", "");
            obj.put("DueDaysGroup", "");
            call = NewApiClient.getInstance().getApiService().getDashBoardCounterForPurchaseLedger(obj);
        } else {
            obj.put("Filter", groupType);
            obj.put("Code", groupCode);
            obj.put("Type", reportType);
            obj.put("FromDate", startDate);
            obj.put("ToDate", endDate);
            obj.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
            call = NewApiClient.getInstance().getApiService().getDashBoardCounterForLedger(obj);
        }

//        Call<DashboardCounterResponse> call = NewApiClient.getInstance().getApiService().getDashBoardCounterForLedger(obj);
        call.enqueue(new Callback<DashboardCounterResponse>() {
            @Override
            public void onResponse(Call<DashboardCounterResponse> call, Response<DashboardCounterResponse> response) {
                if (response.code() == 200) {
                    loader.setVisibility(View.GONE
                    );
                    //    alertDialog.dismiss();
                    //setCounter(response.body().getData().get(0));
                    if (response.body().getData().size() > 0) {
                        double tatalSaleValue = Double.valueOf(response.body().getData().get(0).getTotalSales()) - Double.valueOf(response.body().getData().get(0).getTotalCreditNote());

                        salesvalue.setText(context.getResources().getString(R.string.Rs) + " " + numberToK(String.valueOf(tatalSaleValue)));
                        sales_amount.setText(context.getResources().getString(R.string.Rs) + " " + numberToK(response.body().getData().get(0).getTotalSales()));
                        pending_amount_value.setText(context.getResources().getString(R.string.Rs) + " " + numberToK(response.body().getData().get(0).getTotalCreditNote()));
                        Sale_AfterCal = numberToK(String.valueOf(tatalSaleValue));
                        totalSale = numberToK(response.body().getData().get(0).getTotalSales());
                        credit_return = numberToK(response.body().getData().get(0).getTotalCreditNote());
                    }

                } else {

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
                //Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //  callBplistApi(bp_spinner, cp_spinner);
    }


    private void callledgerAllPageApi(String reportType, String startDate, String endDate, String filter, String groupCode) {
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        Log.e("ledgerALl===>", "callledgerOneApi: " + pageNo);
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
                hde.put("PageNo", String.valueOf(pageNo_Sale));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<CustomerBusinessRes> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getledgerlistPurchasePost(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService().getledgerlistPost(hde);
                }

//                Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().getledgerlistPost(hde);
                try {
                    Response<CustomerBusinessRes> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                AllitemsList.addAll(response.body().getData());
                                adapter.AllData(AllitemsList);
                                if (searchTextValue.isEmpty())
                                    setLedgerDB(response.body().getData());
                                adapter.notifyDataSetChanged();
                                Log.e("TAG", "run:B " + AllitemsList.size());

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


    private void callGroupledgerOneApi(String reportType, String startDate, String endDate, String groupType) {


        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        pageNo = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", groupType);
                hde.put("Code", "");
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ResponseLedgerGroup> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getLedgerGroupPurchase(hde);

                } else {
                    call = NewApiClient.getInstance().getApiService().getLedgerGroupSales(hde);
                }
//                Call<ResponseLedgerGroup> call = NewApiClient.getInstance().getApiService().getLedgerGroupSales(hde);
                try {
                    Response<ResponseLedgerGroup> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //   sales_amount.setText("Rs." + response.body().getTotalSales());
                                //     salesvalue.setText("Rs." + response.body().getTotalSales());
                                layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                customerRecyclerView.setLayoutManager(layoutManager);
                                Prefs.putString(Globals.GROSS_NET, reportType);
                                allGroupSaleList.clear();
                                if (response.body().data.size() > 0) {
                                    allGroupSaleList.addAll(response.body().getData());
                                    ledgerGroupDatabase.myDataDao().insertAll(response.body().getData());

                                }

                                if (response.body().getData().size() == 0) {
                                    no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    no_datafound.setVisibility(View.GONE);

                                }

                                ledgerGroupWiseAdapter = new LedgerGroupWiseAdapter(getActivity(), allGroupSaleList);
                                ledgerGroupWiseAdapter.AllData(allGroupSaleList);
                                customerRecyclerView.setAdapter(ledgerGroupWiseAdapter);
                                loader.setVisibility(View.GONE);
                                ledgerGroupWiseAdapter.notifyDataSetChanged();

                                ledgerGroupWiseAdapter.setOnItemClickListener(new LedgerGroupWiseAdapter.OnItemGroupClickListener() {

                                    @Override
                                    public void onItemGroupClick(int position, String code, String name) {
                                        groupCode = code;
                                        Intent intent = new Intent(requireActivity(), Sale_Group_Inovice_Reports.class);
                                        intent.putExtra("group", groupType);
                                        intent.putExtra("code", code);
                                        intent.putExtra("groupname", name);
                                        intent.putExtra("stockFormWhere", "");
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

    private void callGroupZoneOneApi(String reportType, String startDate, String endDate, String groupType) {

        Log.e("ZONE==>", "callGroupledgerOneApi: " + reportType + startDate + endDate + groupType);

        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        pageNo = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", groupType);
                hde.put("Code", "");
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ResponseZoneGroup> call;

                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getLedgerZoneGroupPurchase(hde);

                } else {
                    call = NewApiClient.getInstance().getApiService().getLedgerZoneGroupSales(hde);
                }

//                Call<ResponseZoneGroup> call = NewApiClient.getInstance().getApiService().getLedgerZoneGroupSales(hde);
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
                                    ledgerZoneDatabase.myDataDao().insertAll(allZoneSaleList);

                                }

                                if (response.body().getData().size() == 0) {
                                    no_datafound.setVisibility(View.VISIBLE);
                                } else {
                                    no_datafound.setVisibility(View.GONE);

                                }
                                Prefs.putString(Globals.GROSS_NET, reportType);
                                ledgerZoneWiseAdapter = new LedgerZoneWiseAdapter(getActivity(), allZoneSaleList);
                                ledgerZoneWiseAdapter.AllData(allZoneSaleList);
                                customerRecyclerView.setAdapter(ledgerZoneWiseAdapter);
                                loader.setVisibility(View.GONE);
                                ledgerZoneWiseAdapter.notifyDataSetChanged();
                                ledgerZoneWiseAdapter.setOnItemClickListener(new LedgerZoneWiseAdapter.OnItemGroupClickListener() {
                                    @Override
                                    public void onItemGroupClick(int position, String code, String name) {
                                        groupCode = code;
                                        Intent intent = new Intent(requireActivity(), Sale_Group_Inovice_Reports.class);
                                        intent.putExtra("group", groupType);
                                        intent.putExtra("code", code);
                                        intent.putExtra("groupname", name);
                                        intent.putExtra("stockFormWhere", "");
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", groupType);
                hde.put("Code", "");
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ResponseZoneGroup> call;
                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getLedgerZoneGroupPurchase(hde);

                } else {
                    call = NewApiClient.getInstance().getApiService().getLedgerZoneGroupSales(hde);
                }
//                Call<ResponseZoneGroup> call = NewApiClient.getInstance().getApiService().getLedgerZoneGroupSales(hde);
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
                                    ledgerZoneDatabase.myDataDao().insertAll(allZoneSaleList);
                                    ledgerZoneWiseAdapter.notifyDataSetChanged();
                                }

                                Log.e("TAG", "run: " + allGroupSaleList.size());
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
                                        groupCode = code;
                                        Intent intent = new Intent(requireActivity(), Sale_Group_Inovice_Reports.class);
                                        intent.putExtra("group", groupType);
                                        intent.putExtra("code", code);
                                        intent.putExtra("groupname", code);
                                        intent.putExtra("stockFormWhere", "");

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

    private void callGroupledgerAllPageApi(String reportType, String startDate, String endDate, String groupType) {
        Log.e("ZONE===>", "callGroupledgerAllPageApi: ");
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("Type", reportType);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", groupType);
                hde.put("Code", "");
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ResponseLedgerGroup> call;

                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService().getLedgerGroupPurchase(hde);

                } else {
                    call = NewApiClient.getInstance().getApiService().getLedgerGroupSales(hde);
                }
//                Call<ResponseLedgerGroup> call = NewApiClient.getInstance().getApiService().getLedgerGroupSales(hde);
                try {
                    Response<ResponseLedgerGroup> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (response.body().data.size() > 0) {
                                    allGroupSaleList.addAll(response.body().getData());
                                    ledgerGroupWiseAdapter.AllData(allGroupSaleList);
                                    ledgerGroupDatabase.myDataDao().insertAll(allGroupSaleList);
                                    ledgerGroupWiseAdapter.notifyDataSetChanged();
                                }

                                Log.e("TAG", "run: " + allGroupSaleList.size());
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

                                ledgerGroupWiseAdapter.setOnItemClickListener(new LedgerGroupWiseAdapter.OnItemGroupClickListener() {

                                    @Override
                                    public void onItemGroupClick(int position, String code, String name) {
                                        groupCode = code;
                                        Intent intent = new Intent(requireActivity(), Sale_Group_Inovice_Reports.class);
                                        intent.putExtra("group", groupType);
                                        intent.putExtra("code", code);
                                        intent.putExtra("groupname", name);
                                        intent.putExtra("stockFormWhere", "");

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

// LinearLayoutManager layoutManager;

    ///ArrayList<BusinessPartnerData> AllItemList=new ArrayList<BusinessPartnerData>();
    int pageNo = 1;
    int pageNo_Sale = 1;
    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {


        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            Log.e("'TAG'", "onScrolled: ");

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


                if (groupType.equalsIgnoreCase("Group") || groupFIlter.equalsIgnoreCase("Group")) {
                    loader.setVisibility(View.VISIBLE);
                    Log.e("typedropdown==>>", "onCreateView: group");
                    pageNo++;
                    callGroupledgerAllPageApi(reportType, startDate, endDate, groupType);
                    url = Globals.saleGroupReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


                    isScrollingpage = false;

                } else if (groupType.equalsIgnoreCase("Zone")) {
                    loader.setVisibility(View.VISIBLE);
                    pageNo++;
                    Log.e("typegroup==>>", "onCreateView: Zone");
                    callGroupZoneAllPageApi(reportType, startDate, endDate, groupType);
                    url = Globals.saleGroupReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


                    isScrollingpage = false;

                } else if (groupType.equalsIgnoreCase("Item Stock")) {
                    loader.setVisibility(View.VISIBLE);
                    pageNo++;
                    Log.e("typegroup==>>", "onCreateView: Zone");
                    callAllPagesApi_ItemStock();
                    url = Globals.ItemStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


                    isScrollingpage = false;

                } else if (groupType.equalsIgnoreCase("Category")) {//Stock Group
                    loader.setVisibility(View.VISIBLE);
                    pageNo++;
                    Log.e("typegroup==>>", "onCreateView: Stock Group");
                    callAllPagesApi_GroupItemStock();
                    url = Globals.GroupStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + Globals.QUERY_SALEPERSON_CODE_PDF + salePersonCode;


                    isScrollingpage = false;

                } else {
                    Log.e("BB==>>", "onCreateView:ledger ");
                    loader.setVisibility(View.VISIBLE);
                    pageNo_Sale++;
                    callledgerAllPageApi(reportType, startDate, endDate, groupType, groupCode);
                    url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


                    isScrollingpage = false;
                }


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
                Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().payment_collection_invoiceBases(hde);
                try {
                    Response<CustomerBusinessRes> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                AllitemsList.clear();
                                AllitemsList.addAll(response.body().getData());
                                Prefs.putString(Globals.GROSS_NET, reportType);
                                adapter = new LedgerAdapter(getContext(), AllitemsList);
                                customerRecyclerView.setAdapter(adapter);
                                customerRecyclerView.setLayoutManager(layoutManager);
                                loader.setVisibility(View.GONE);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        type_group.setVisibility(View.VISIBLE);

    }


    Menu menuOut;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menuOut = menu;
        inflater.inflate(R.menu.transaction_menu_filter, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem filterAtoZ = menu.findItem(R.id.filterAtoZ);
        MenuItem item = menu.findItem(R.id.search1);
        MenuItem share = menu.findItem(R.id.share_received);
        MenuItem amountFilter = menu.findItem(R.id.filterAmount);
        MenuItem dateFilter = menu.findItem(R.id.filterDate);
        MenuItem ledger = menu.findItem(R.id.ledger);
        MenuItem calendar = menu.findItem(R.id.calendar);
        ledger.setVisible(false);
        item.setVisible(false);
        amountFilter.setVisible(true);
        dateFilter.setVisible(false);
        calendar.setVisible(false);
//
        //filterAtoZ.setChecked(true);

        //  share.setVisible()

//
//                //   MenuItem itemCalendar = menu.findItem(R.id.calendar);
        SearchView searchView = new SearchView(((Reports) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();


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
                intent.putExtra("filter", groupType);
                intent.putExtra("code", groupCode);
                startActivity(intent);
                break;

            case R.id.share_sales:
                shareLedgerData();
                // showBottomSheetDialog();
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.filterAtoZ:
                // Globals.selectDat(this);

                /***shubh****/
                orderBYName = Globals.ATOZ;
                orderBYAmt = "";
                Prefs.putString(Globals.PREFS_ATOZ, orderBYName);
                Prefs.putString(Globals.PREFS_Amount, orderBYAmt);

                if (Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ATOZ)) {
                    menuItem.setChecked(true);
                } else {
                    menuItem.setChecked(false);
                }


       /*         if (groupType.equalsIgnoreCase("Group")) {

                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Zone")) {
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Stock Group")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Item Stock")) {
                    callApiItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    pageNo_Sale = 1;
                    callledgerOneApi(reportType, startDate, endDate, "", "");
                }*/
                groupTypeManager_from_Calender();


                menuItem.setChecked(!menuItem.isChecked());


                break;

            case R.id.filterZtoA:
                orderBYName = Globals.ZTOA;
                orderBYAmt = "";


                Prefs.putString(Globals.PREFS_ATOZ, orderBYName);
                Prefs.putString(Globals.PREFS_Amount, orderBYAmt);

                if (Prefs.getString(Globals.PREFS_ATOZ, "").equalsIgnoreCase(Globals.ZTOA)) {
                    menuItem.setChecked(true);
                } else {
                    menuItem.setChecked(false);
                }


            /*    if (groupType.equalsIgnoreCase("Group")) {

                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Zone")) {
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Stock Group")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Item Stock")) {
                    callApiItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    pageNo_Sale = 1;
                    callledgerOneApi(reportType, startDate, endDate, "", "");
                }*/

                groupTypeManager_from_Calender();

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
                orderBYName = "";
                orderBYAmt = Globals.DESC;

               /* if (Prefs.getString(Globals.PREFS_Amount, "").equalsIgnoreCase(Globals.DESC)) {
                    menuItem.setChecked(true);
                } else {
                    menuItem.setChecked(false);
                }*/

                Prefs.putString(Globals.PREFS_ATOZ, orderBYName);
                Prefs.putString(Globals.PREFS_Amount, orderBYAmt);

                if (groupType.equalsIgnoreCase("Group")) {

                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Zone")) {
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Stock Group")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Item Stock")) {
                    callApiItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    pageNo_Sale = 1;
                    callledgerOneApi(reportType, startDate, endDate, "", "");
                }

                menuItem.setChecked(!menuItem.isChecked());

                break;


            case R.id.filterAmountAsc:
                orderBYName = "";
                orderBYAmt = Globals.ASC;

               /* if (Prefs.getString(Globals.PREFS_Amount, "").equalsIgnoreCase(Globals.DESC)) {
                    menuItem.setChecked(true);
                } else {
                    menuItem.setChecked(false);
                }*/

                Prefs.putString(Globals.PREFS_ATOZ, orderBYName);
                Prefs.putString(Globals.PREFS_Amount, orderBYAmt);

                if (groupType.equalsIgnoreCase("Group")) {

                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Zone")) {
                    callGroupZoneOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Stock Group")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Item Stock")) {
                    callApiItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    pageNo_Sale = 1;
                    callledgerOneApi(reportType, startDate, endDate, "", "");
                }

                menuItem.setChecked(!menuItem.isChecked());

                break;

            case R.id.filterDate:
                adapter.sortingByDate();


                break;

            case R.id.clearAllFilter:
                searchTextValue = "";
                searchView.setQuery("", false);
                orderBYName = "";
                orderBYAmt = "";
                Prefs.putString(Globals.PREFS_ATOZ, "");
                Prefs.putString(Globals.PREFS_Amount, "");

                if (groupType.equalsIgnoreCase("Group")) {

                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);
                } else if (groupType.equalsIgnoreCase("Zone")) {
                    callGroupledgerOneApi(reportType, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Stock Group")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType);

                } else if (groupType.equalsIgnoreCase("Item Stock")) {
                    callApiItemStock(searchTextValue, startDate, endDate, groupType);
                } else {
                    pageNo_Sale = 1;
                    callledgerOneApi(reportType, startDate, endDate, "", "");
                }


                menuItem.setChecked(!menuItem.isChecked());


        }
        return false;
    }

    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;


    /*************** Bhupi *********************/ // Calling one BottomSheet for Ledger Sharing
    private void shareLedgerData() {
        String title;
        if (groupType.equalsIgnoreCase("Group") || groupFIlter.equalsIgnoreCase("Group")) {

            url = Globals.saleGroupReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("SALESREPORTSHARINGDRO", "onCreateView: " + url);
            // binding.headingBottomSheetShareReport.setText(R.string.share_group_list);
            title = getString(R.string.share_group_list);
        } else if (groupType.equalsIgnoreCase("Zone")) {

            url = Globals.saleGroupReportsPdf + "Type=" + reportType + "&Filter=" + groupType + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            Log.e("SALESREPORTSHARINGDRO", "onCreateView: " + url);
            // binding.headingBottomSheetShareReport.setText(R.string.share_zone_list);
            title = getString(R.string.share_group_list);

        } else if (groupType.equalsIgnoreCase("Stock Group")) { //Item Stock Group
            title = getActivity().getResources().getString(R.string.grp_stock);
            url = Globals.GroupStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + Globals.QUERY_SALEPERSON_CODE_PDF + salePersonCode;


        } else if (groupType.equalsIgnoreCase("Item Stock")) { //Item Stock

            title = getActivity().getResources().getString(R.string.item_stock);
            url = Globals.ItemStockReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;


        } else {
            Log.e("typedropdown==>>", "onCreateView:ledger ");

            url = Globals.saleReportsPdf + "Type=" + reportType + "&Filter=" + "&FromDate=" + startDate + "&ToDate=" + endDate + "&Code=" + groupCode + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE;
            title = getString(R.string.share_customer_list);
        }
        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                "");
    }

    /***shubh****/
    WebView dialogWeb;
    String url;


    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(this.getActivity().getSupportFragmentManager(), "Tag_Picker");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                loader.setVisibility(View.VISIBLE);
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                startDateReverseFormat = Globals.convertDateFormat(startDate);
                endDateReverseFormat = Globals.convertDateFormat(endDate);
                from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);
                groupTypeManager_from_Calender();


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
            from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);
            //  Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            groupTypeManager_from_Calender();

            // callledgerapi(reportType, startDate, endDate, "", "");
            from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
            from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);


            groupTypeManager_from_Calender();


            from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);
            groupTypeManager_from_Calender();
            from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();


            from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);

            groupTypeManager_from_Calender();

            from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);

            groupTypeManager_from_Calender();
            from_to_date.setText(binding.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);
            groupTypeManager_from_Calender();
            from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);

            groupTypeManager_from_Calender();

            from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            from_to_date.setText(startDateReverseFormat + " - " + endDateReverseFormat);

            groupTypeManager_from_Calender();

            from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        binding.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            loader.setVisibility(View.VISIBLE);
            startDate = "";
            endDate = "";
            groupTypeManager_from_Calender();
            from_to_date.setText(binding.tvAllBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume: ");
        type_group.setVisibility(View.VISIBLE);
    }

    private void loadLedgerCompFragment(Fragment fragment) {
        Bundle b = new Bundle();
        b.putSerializable(Globals.LedgerCompanyData, customerList);
        b.putString("group", groupType);
        b.putString("code", groupCode);

//        Ledger_Comp_Fragment fragment = new Ledger_Comp_Fragment();
        fragment.setArguments(b);
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.addToBackStack(null);
        if (fm.getBackStackEntryCount() > 0)
            transaction.add(R.id.container, fragment);
        else
            transaction.add(R.id.container, fragment);
        transaction.commit();


    }


    private static final String TAG = "Ledger_Comp_Fragment";


    ArrayList<DataItemInSalesCard> AllitemsStockList = new ArrayList<>();
    ItemInSaleStockAdapter stock_adapter;

    ArrayList<DataItemFilterDashBoard> AllItemsGroupStockList = new ArrayList<>();
    ItemOnStockGroupAdapter stockGroupAdapter;

    private void callApiItemStock(String searchValue, String startDate, String endDate, String type) {
        pageNo = 1;
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("GroupCode", ItemGroupCode);
                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ResponseItemInSalesCard> call = NewApiClient.getInstance().getApiService().getItemInSaleCard(hde);
                try {
                    Response<ResponseItemInSalesCard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    //  AllitemsList.clear();
                                    AllitemsStockList.clear();
                                    if (response.body().getData().size() > 0) {
                                        no_datafound.setVisibility(View.GONE);
                                        AllitemsStockList.addAll(response.body().getData());
                                        itemsInSalesCardDatabase.myDataDao().insertAll(AllitemsStockList);

                                    } else {
                                        no_datafound.setVisibility(View.VISIBLE);
                                    }


                                    loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));
                                    try {
                                        stock_adapter = new ItemInSaleStockAdapter(requireContext(), AllitemsStockList);
                                    } catch (Exception e) {
                                        Log.e(TAG, "runSTOCK: " + e.getMessage());
                                    }
                                    stock_adapter.AllData(AllitemsStockList);
                                    try {
                                        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
                                    } catch (Exception e) {
                                        Log.e(TAG, "runSTOCKLINEAR: " + e.getMessage());
                                    }
                                    customerRecyclerView.setLayoutManager(layoutManager);
                                    customerRecyclerView.setAdapter(stock_adapter);
                                    stock_adapter.notifyDataSetChanged();

                                }
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


    private void callApiGroupItemStock(String searchValue, String startDate, String endDate, String type) {
        pageNo = 1;
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ResponseItemFilterDashboard> call = NewApiClient.getInstance().getApiService().getFilterGroupItemStock(hde);
                try {
                    Response<ResponseItemFilterDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    //  AllitemsList.clear();
                                    AllItemsGroupStockList.clear();
                                    if (response.body().getData().size() > 0) {
                                        no_datafound.setVisibility(View.GONE);
                                        AllItemsGroupStockList.addAll(response.body().getData());
                                        itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);

                                    } else {
                                        no_datafound.setVisibility(View.VISIBLE);
                                    }


                                    loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));
                                    try {
                                        stockGroupAdapter = new ItemOnStockGroupAdapter(requireContext(), AllItemsGroupStockList, "Sales", "");
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


                                }
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

    private String ItemGroupCode = "";

    private void callAllPagesApi_ItemStock() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchTextValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("GroupCode", ItemGroupCode);
                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);
                Call<ResponseItemInSalesCard> call = NewApiClient.getInstance().getApiService().getItemInSaleCard(hde);
                try {
                    Response<ResponseItemInSalesCard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0) ;
                                    AllitemsStockList.addAll(response.body().getData());
                                    stock_adapter.AllData(AllitemsStockList);
                                    itemsInSalesCardDatabase.myDataDao().insertAll(AllitemsStockList);
                                    loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));
                                    stock_adapter.notifyDataSetChanged();

                                }
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

    String orderBYName = "";
    String orderBYAmt = "";

    private void callAllPagesApi_GroupItemStock() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchTextValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);

                Call<ResponseItemFilterDashboard> call = NewApiClient.getInstance().getApiService().getFilterGroupItemStock(hde);
                try {
                    Response<ResponseItemFilterDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0) ;
                                    AllItemsGroupStockList.addAll(response.body().getData());
                                    stockGroupAdapter.AllData(AllItemsGroupStockList);
                                    itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);
                                    loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));
                                    stockGroupAdapter.notifyDataSetChanged();

                                }
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


}