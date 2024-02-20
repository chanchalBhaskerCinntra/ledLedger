package com.cinntra.ledure.fragments;

import static com.cinntra.ledure.globals.Globals.PAGE_NO_STRING;
import static com.cinntra.ledure.globals.Globals.numberToK;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.Reports;
import com.cinntra.ledure.adapters.BillAdapter;
import com.cinntra.ledure.adapters.PaymentDueCustomerListAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogDueBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.model.Customers_Report;
import com.cinntra.ledure.model.DashboardCounterResponse;
import com.cinntra.ledure.newapimodel.DataPaymentDueDashboardCustomerList;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.ResponsePayMentDueCounter;
import com.cinntra.ledure.newapimodel.ResponsePaymentDueDashboardCustomerList;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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


public class OverDueFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    LinearLayoutManager layoutManager;

    //  private ReceivableDatabase db;

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
    @BindView(R.id.receive_pending_layout)
    LinearLayout receive_pending_layout;

    @BindView(R.id.no_datafound)
    ImageView no_datafound;

    @BindView(R.id.swipeRefreshItem)
    SwipeRefreshLayout swipeRefreshItem;
    TextView salesvalue;
    TextView from_to_date;
    Spinner type_dropdown;
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

    String searchTextValue = "";

    public OverDueFragment(TextView salesvalue, TextView from_to_date, Spinner type_dropdown) {
        // Required empty public constructor
        this.salesvalue = salesvalue;
        this.from_to_date = from_to_date;
        this.type_dropdown = type_dropdown;
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
                callledgerOneapi(reportType, startDate, endDate);

                //     adapter.sortingA2Z("AtoZ");


                break;

            case R.id.filterZtoA:
                orderBYName = Globals.ZTOA;
                orderBYAmt = "";
                callledgerOneapi(reportType, startDate, endDate);
                // adapter.sortingA2Z("ZtoA");
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void shareLedgerData() {
        String title = getString(R.string.share_customer_list);

        url = Globals.overAllReceivable + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Filter=&Code=&DueDaysGroup=" + overDueFilter;

        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb, url, title);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                "");
    }


    String rupeeSymbol = "\u20B9";


    private void setAdapter(List<DataPaymentDueDashboardCustomerList> arrayList) {
        adapter = new PaymentDueCustomerListAdapter(getActivity(), arrayList, "Receivable");
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        customerRecyclerView.setLayoutManager(layoutManager);
        customerRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //    adapter.notifyItemInserted(arrayList.size() - 1);

    }


    private void setRecyclerViewAdapter() {
        salesvalue.setText("\u20B9 " + Prefs.getString(Globals.Total_Receivables, "0"));
        //    salesvalue.setText("\u20B9 " + Prefs.getString(Globals.Total_Receivables, "0"));
        //   callDashboardCounter();
        callPaymentDueCounter();
        overDueFilter = "7";


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    if (db.myDataDao().getAll().size() > 0)
//                      {
//                    ALlItemList.addAll(db.myDataDao().getAll());
//                      } else {
//                  if (Globals.checkInternet(requireContext()))
//                       {
//
//                      callledgerOneapi(reportType, startDate, endDate);
//                        }
//
//                    }
                    callledgerOneapi(reportType, startDate, endDate);
                    adapter = new PaymentDueCustomerListAdapter(getActivity(), ALlItemList, "Receivable");
                    layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    customerRecyclerView.setLayoutManager(layoutManager);
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

    private void callPaymentDueCounter() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
        jsonObject.addProperty("DueDaysGroup", overDueFilter);

        Call<ResponsePayMentDueCounter> call = NewApiClient.getInstance().getApiService().getPaymentDueCounter(jsonObject);
        call.enqueue(new Callback<ResponsePayMentDueCounter>() {
            @Override
            public void onResponse(Call<ResponsePayMentDueCounter> call, Response<ResponsePayMentDueCounter> response) {
                if (response.code() == 200) {
                    //  Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show();
                    // alertDialog.dismiss();
                    loader.setVisibility(View.GONE);
                    if (response.body().getStatus() == 200) {
                        //  Toast.makeText(requireContext(), "success 200", Toast.LENGTH_SHORT).show();
                        salesvalue.setText(requireActivity().getResources().getString(R.string.Rs) + " " + Globals.numberToK(String.valueOf(response.body().getTotalPaybal())));

                    } else {
                        //  Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    loader.setVisibility(View.GONE);


//                    Gson gson = new GsonBuilder().create();
//                    LeadResponse mError = new LeadResponse();
//                    try {
//                        String s = response.errorBody().string();
//                        mError = gson.fromJson(s, LeadResponse.class);
//                    } catch (IOException e) {
//                        //handle failure to read error
//                    }

                }

            }

            @Override
            public void onFailure(Call<ResponsePayMentDueCounter> call, Throwable t) {
                loader.setVisibility(View.GONE);

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // TODO: Rename and change types and number of parameters
   /* public static Bills_Fragment newInstance(String param1, String param2) {
        Bills_Fragment fragment = new Bills_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }*/

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
        //db = ReceivableDatabase.getDatabase(requireContext());
        loader.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(requireContext());

        slaes_amount.setText("Received Amount");
        pending_amount.setText("Pending Amount");
        receive_pending_layout.setVisibility(View.GONE);
        from_to_date.setText("7");
        from_to_date.setOnClickListener(view -> {
            showDateBottomSheetDialog(requireContext());
        });

        callPaymentDueCounter();
        callledgerOneapi(reportType, startDate, endDate);
//
//        if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("nondue")) {
//            from_to_date.setText("Not due");
//            overDueFilter = "-1";
//
//        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("0")) {
//            from_to_date.setText("> 0");
//            overDueFilter = "0";
//        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("30")) {
//            from_to_date.setText("> 30");
//            overDueFilter = "30";
//        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("60")) {
//            from_to_date.setText("> 60");
//            overDueFilter = "60";
//
//        } else if (Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("All")) {
//            from_to_date.setText("All");
//            overDueFilter = "";
//        } else {
//            from_to_date.setText("All");
//            overDueFilter = "";
//        }

        // setSaleAdapter();
        // setRecyclerViewAdapter();


        swipeRefreshItem.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                if (Globals.checkInternet(getActivity())) {
                    pageNo = 1;
                    //  db.myDataDao().deleteAll();
                    ALlItemList.clear();
                    callledgerOneapi(reportType, startDate, endDate);


                    swipeRefreshItem.setRefreshing(false);
                }

                swipeRefreshItem.setRefreshing(false);


            }
        });

        url = Globals.overAllReceivable + "FromDate=" + startDate + "&ToDate=" + endDate + "&" + PAGE_NO_STRING + "" + pageNo + Globals.QUERY_MAX_PAGE_PDF + Globals.QUERY_PAGE_SIZE + "&Filter=&Code=&DueDaysGroup=" + overDueFilter;

        type_dropdown.setVisibility(View.GONE);
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
                if (!searchTextValue.isEmpty()) {
                    pageNo = 1;
                    callledgerOneapi(reportType, startDate, endDate);
                }

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
                    callledgerOneapi(reportType, startDate, endDate);
                }
                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                searchView.clearFocus();

                searchTextValue = newText;
                if (!searchTextValue.isEmpty()) {
                    pageNo = 1;
                    callledgerOneapi(reportType, startDate, endDate);
                }
            }
        });


        // now create the instance of the material date
        // picker


        return v;
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
                //   callDashboardCounter();
                callPaymentDueCounter();
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
            boolean shouldPaginate =
                    isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotaolMoreThanVisible
                            && isScrollingpage;

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                // pageNo++;
                loader.setVisibility(View.VISIBLE);
                if (ALlItemList.size() != 0)
                    pageNo = ALlItemList.size() / Globals.QUERY_PAGE_SIZE;
                pageNo++;
                if (Globals.checkInternet(requireContext())) {
                    callLedgerAllPageApi(reportType, startDate, endDate);
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


    PaymentDueCustomerListAdapter adapter;
    int pageNo = 1;

    List<DataPaymentDueDashboardCustomerList> ALlItemList = new ArrayList();
    String orderBYName = "a-z";
    String orderBYAmt = "";

    String overDueFilter = "7";

    private void callledgerOneapi(String reportType, String startDate, String endDate) {
        // db.myDataDao().deleteAll();
        loader.setVisibility(View.VISIBLE);
        pageNo = 1;
//        Prefs.putString(Globals.FROM_DATE_receivable, startDate);
//        Prefs.putString(Globals.TO_DATE_Receivable, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
                HashMap<String, String> hde = new HashMap<>();
                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);
                hde.put(Globals.payLoadDueDaysGroup, overDueFilter);

                Call<ResponsePaymentDueDashboardCustomerList> call = NewApiClient.getInstance().getApiService().getPaymentDueDashboardCustomerList(hde);
                try {
                    Response<ResponsePaymentDueDashboardCustomerList> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                if (response.code() == 200) {
                                    ALlItemList.clear();
                                    if (response.body().getData().size() > 0) {
                                        no_datafound.setVisibility(View.GONE);
                                        //  db.myDataDao().deleteAll();
                                        ALlItemList.clear();
                                        ALlItemList.addAll(response.body().getData());

                                        adapter = new PaymentDueCustomerListAdapter(getActivity(), ALlItemList, "Receivable");
                                        adapter.AllData(ALlItemList);

                                        customerRecyclerView.setLayoutManager(layoutManager);
                                        customerRecyclerView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                        if (searchTextValue.isEmpty() && Prefs.getString(Globals.FROM_DATE_receivable, "All").equalsIgnoreCase("All") && orderBYName.equalsIgnoreCase("a-z")) {
                                            //  db.myDataDao().deleteAll();
                                            //   db.myDataDao().insertAll(ALlItemList);
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
                        Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
//                {
//                    "SalesPersonCode": "-1",
//                        "SearchText": "",
//                        "OrderByName": "",
//                        "OrderByAmt": "",
//                        "DueDaysGroup": "7",
//                        "PageNo": "1",
//                        "MaxSize": "20"
//                }
                HashMap<String, String> hde = new HashMap<>();


                hde.put("SearchText", searchTextValue);
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, orderBYName);
                hde.put(Globals.payLoadOrderByAMt, orderBYAmt);
                hde.put(Globals.payLoadDueDaysGroup, overDueFilter);
                Call<ResponsePaymentDueDashboardCustomerList> call = NewApiClient.getInstance().getApiService().getPaymentDueDashboardCustomerList(hde);
                try {
                    Response<ResponsePaymentDueDashboardCustomerList> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0) ;
                                    ALlItemList.addAll(response.body().getData());
                                    adapter.AllData(ALlItemList);
                                    adapter.notifyDataSetChanged();
                                    loader.setVisibility(View.GONE);
                                    if (searchTextValue.isEmpty() && Prefs.getString(Globals.FROM_DATE_receivable, "All").equalsIgnoreCase("All") && orderBYName.equalsIgnoreCase("a-z")) {
                                        // db.myDataDao().deleteAll();
                                        // db.myDataDao().insertAll(response.body().getData());
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
                callledgerOneapi(reportType, startDate, endDate);
                menuItem.setChecked(!menuItem.isChecked());

                // adapter.sortingA2Z("AtoZ");


                break;

            case R.id.filterZtoA:
                // adapter.sortingA2Z("ZtoA");
                pageNo = 1;
                orderBYName = Globals.ZTOA;
                orderBYAmt = "";
                callledgerOneapi(reportType, startDate, endDate);
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.filterAmount:
                // adapter.sortingAmount();
                pageNo = 1;
                orderBYName = "";
                orderBYAmt = Globals.DESC;
                callledgerOneapi(reportType, startDate, endDate);
                menuItem.setChecked(!menuItem.isChecked());
                break;

            case R.id.clearAllFilter:
                searchTextValue = "";
                searchView.setQuery("", false);
                searchView.setFocusable(false);
                orderBYAmt = "";
                orderBYName = "";

                callledgerOneapi(reportType, startDate, endDate);
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
        BottomSheetDialogDueBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogDueBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });


        //   binding.rbSeven.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("nondue"));

        //   binding.rbFive.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("30"));
        //   binding.rbTwo.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("0"));

//        binding.rblessThan60.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("60"));

        //    binding.rbAll.setChecked(Prefs.getString(Globals.FROM_DATE_receivable, "").equalsIgnoreCase("All"));

        binding.rbSeven.setChecked(true);

        binding.rgOverDue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == binding.rbSeven.getId()) {
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getTodaysDate();
                    pageNo = 1;
                    overDueFilter = "7";
                    // Prefs.putString(Globals.FROM_DATE_receivable, "nondue");
                    //   callDashboardCounter();
                    callPaymentDueCounter();
                    callledgerOneapi(reportType, startDate, endDate);
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("7");


                } else if (i == binding.rbFive.getId()) {
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(0);
                    pageNo = 1;
                    overDueFilter = "5";
                    //  Prefs.putString(Globals.FROM_DATE_receivable, "0");
                    //   callDashboardCounter();
                    callPaymentDueCounter();
                    callledgerOneapi(reportType, startDate, endDate);
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("5");

                } else if (i == binding.rbTwo.getId()) {
                    Log.e(TAG, "onCheckedChanged: " + Globals.getDateForReceivable(-30));
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(-30);
                    pageNo = 1;
                    //  Prefs.putString(Globals.FROM_DATE_receivable, "30");
                    overDueFilter = "2";
                    //   callDashboardCounter();
                    callPaymentDueCounter();
                    callledgerOneapi(reportType, startDate, endDate);
                    bottomSheetDialog.dismiss();
                    from_to_date.setText("2");

                } else if (i == binding.rbAll.getId()) {
                    Log.e(TAG, "onCheckedChanged: " + Globals.getDateForReceivable(-60));
                    startDate = Globals.getTodaysDate();
                    endDate = Globals.getDateForReceivable(-60);
                    pageNo = 1;
                    overDueFilter = "";
                    //  Prefs.putString(Globals.FROM_DATE_receivable, "60");
                    //   callDashboardCounter();
                    callPaymentDueCounter();
                    callledgerOneapi(reportType, startDate, endDate);
                    from_to_date.setText("All");
                    bottomSheetDialog.dismiss();
                } else if (i == binding.rbAll.getId()) {
                    startDate = "";
                    endDate = "";
                    pageNo = 1;
                    overDueFilter = "";
                    //  Prefs.putString(Globals.FROM_DATE_receivable, "All");
                    from_to_date.setText("All");
                    //   callDashboardCounter();
                    callPaymentDueCounter();
                    callledgerOneapi(reportType, startDate, endDate);
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