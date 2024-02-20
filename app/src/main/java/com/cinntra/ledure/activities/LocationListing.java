package com.cinntra.ledure.activities;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.AllTripExpenseAdapter;
import com.cinntra.ledure.adapters.EmployeeHeirarchiDropdownAdapter;
import com.cinntra.ledure.adapters.LocationListingAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.FragmentLocationListingBinding;
import com.cinntra.ledure.fragments.Dashboard;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.AllTripExpenseResponse;
import com.cinntra.ledure.model.DataAllTripExpense;
import com.cinntra.ledure.model.DataDeliveryNotePendingByOrder;
import com.cinntra.ledure.model.EmployeeValue;
import com.cinntra.ledure.model.MapData;

import com.cinntra.ledure.model.MapResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationListing extends MainBaseActivity {
    FragmentLocationListingBinding binding;

    @BindView(R.id.customer_lead_List)
    RecyclerView recyclerView;
    @BindView(R.id.dateText)
    EditText dateText;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.showlisting)
    TextView showlisting;

    @BindView(R.id.empSpinner)
    Spinner empSpinner;

    String employeeid;

    String typeOfCheckIn = "Trip";

    ArrayAdapter<CharSequence> typeCheckInAdapter;

    LinearLayoutManager layoutManager;


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    //   TextView from_to_date;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    public static String startDate = "";
    public static String endDate = "";

    @Override
    protected void onResume() {
        super.onResume();

       /* if(Globals.checkInternet(this)){
            callexpenseapi();
        }*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLocationListingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);
        empSpinner.setAdapter(new EmployeeHeirarchiDropdownAdapter(LocationListing.this, Dashboard.teamList_Hearchi));
        binding.typeMapSpinner.setVisibility(View.GONE);
        typeCheckInAdapter = ArrayAdapter.createFromResource(LocationListing.this, R.array.locationScreenFilter, android.R.layout.simple_spinner_item);
        typeCheckInAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.typeMapSpinner.setAdapter(typeCheckInAdapter);
        binding.dateText.setText("All");
        binding.layoutLoader.loader.setVisibility(View.VISIBLE);
        callExpenseApiTrip(startDate, endDate);
      //  callgetlocationApi();
        //callgetlocationApi();
        //callgetlocationApi();
//
//        if (typeOfCheckIn.equalsIgnoreCase("Trip")) {
//            dateText.setText("All");
//        } else {
//            dateText.setText(Globals.getTodaysDatervrsfrmt());
//        }


        //   callExpenseApiTrip();

//        if (typeOfCheckIn.equalsIgnoreCase("Trip")){
//            callExpenseApiTrip();
//        }else {
//            callgetlocationApi();
//        }


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typeOfCheckIn.equalsIgnoreCase("Trip")) {

                    showDateBottomSheetDialog(LocationListing.this);
                } else {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(LocationListing.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                            "yyyy-MM-dd");
                                    try {
                                        Date strDate = dateFormatter.parse(s);
                                        dateText.setText(dateFormatter.format(strDate));
                                        if (typeOfCheckIn.equalsIgnoreCase("Trip")) {
                                            callExpenseApiTrip(startDate, endDate);
                                        } else {
                                          //  callgetlocationApi();
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
//                textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                                }
                            }, mYear, mMonth, mDay);

                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                }


            }
        });


        showlisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mdplist.size() > 0) {
                    Bundle b = new Bundle();
                    b.putSerializable("MapList", (Serializable) mdplist);
                    Intent intent = new Intent(LocationListing.this, MyMapLocation.class);
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    Toast.makeText(LocationListing.this, "No Location Found", Toast.LENGTH_LONG).show();
                }
            }
        });
        empSpinner.setVisibility(View.GONE);

        employeeid = Prefs.getString(Globals.EmployeeID, "");


        empSpinner.setSelection(getEmployeeData(employeeid, Dashboard.teamList_Hearchi));


        empSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                employeeid = Dashboard.teamList_Hearchi.get(i).getSalesEmployeeCode();
              //  callgetlocationApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        binding.typeMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                typeOfCheckIn = adapterView.getItemAtPosition(i).toString();
//                if (i == 0) {
//
//                } else {
//                    callgetlocationApi();
//                }
//
//
//            }

//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                typeOfCheckIn = typeCheckInAdapter.getItem(0).toString();
//                //   callExpenseApiTrip();
//
//            }
//        });


        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

               /* if (Globals.checkInternet(LocationListing.this)) {
                    callexpenseapi();
                } else*/
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        
        callgetlocationApiForMap();

        
    }

    private int getEmployeeData(String employeeid, ArrayList<EmployeeValue> teamList_hearchi) {
        int pos = 0;
        for (EmployeeValue emp : teamList_hearchi) {
            if (emp.getSalesEmployeeCode().equalsIgnoreCase(employeeid)) {
                pos = teamList_hearchi.indexOf(emp);
            }
        }
        return pos;
    }

    List<MapData> mdplist = new ArrayList<>();
    private ArrayList<DataAllTripExpense> dataAllTripExpenses = new ArrayList<>();

    private void callgetlocationApi() {
        Log.e("TAG", "callgetlocationApi: ");

        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("Emp_Id", employeeid);
        mapData.put("UpdateDate", "");
        mapData.put("shape", "");

        Log.e("TAG", "callgetlocationApi: "+mapData.size());
        Log.e("TAG", "callgetlocationApi: "+mapData.get("Emp_Id"));

        Call<MapResponse> call = NewApiClient.getInstance().getApiService().getmaplocation(mapData);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                if (response != null) {
                    ArrayList list = new ArrayList<MapData>();
                    list.addAll(response.body().getValue());

                    mdplist.clear();
                    Iterator<MapData> iterator = list.iterator();
                    while (iterator.hasNext()) {

                        String item = iterator.next().getAddress();
                        if (item.isEmpty()) {
                            iterator.remove();
                        }
                    }

                    mdplist.addAll(list);
//                    expenseAdapter = new LocationListingAdapter(LocationListing.this, mdplist);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(LocationListing.this, RecyclerView.VERTICAL, false));
//                    recyclerView.setAdapter(expenseAdapter);
//                    expenseAdapter.notifyDataSetChanged();
//                    nodatafoundimage();

                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

            }
        });

    }


    LocationListingAdapter expenseAdapter;
    AllTripExpenseAdapter allTripExpenseAdapter;


    private void callgetlocationApiForMap() {
        Log.e("TAG", "callgetlocationApiMap: ");

        HashMap<String, String> map = new HashMap<>();
        map.put("Emp_Id", employeeid);
        map.put("UpdateDate", "");
        map.put("shape", "");

        Log.e("TAG", "callgetlocationApimap: "+map.size());
        Log.e("TAG", "callgetlocationApimap: "+map.get("Emp_Id"));

        Call<MapResponse> call = NewApiClient.getInstance().getApiService().getmaplocation(map);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                if (response != null) {
                    ArrayList list = new ArrayList<MapData>();
                    list.addAll(response.body().getValue());

                    mdplist.clear();
                    Iterator<MapData> iterator = list.iterator();
                    while (iterator.hasNext()) {

                        String item = iterator.next().getAddress();
                        if (item.isEmpty()) {
                            iterator.remove();
                        }
                    }

                    mdplist.addAll(list);
//                    expenseAdapter = new LocationListingAdapter(LocationListing.this, mdplist);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(LocationListing.this, RecyclerView.VERTICAL, false));
//                    recyclerView.setAdapter(expenseAdapter);
//                    expenseAdapter.notifyDataSetChanged();
//                    nodatafoundimage();

                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

            }
        });

    }



    private void callExpenseApiTrip(String fromdate, String toDate) {


        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, "1"));
        mapData.put("FromDate", fromdate);
        mapData.put("ToDate", toDate);
//        mapData.put("UpdateDate", dateText.getText().toString());
//        mapData.put("shape", "");

        Call<AllTripExpenseResponse> call = NewApiClient.getInstance().getApiService().getAllTripExpense(mapData);
        call.enqueue(new Callback<AllTripExpenseResponse>() {
            @Override
            public void onResponse(Call<AllTripExpenseResponse> call, Response<AllTripExpenseResponse> response) {
                if (response != null) {
                    ArrayList list = new ArrayList<DataAllTripExpense>();
                    list.addAll(response.body().getData());
                    binding.layoutLoader.loader.setVisibility(View.GONE);
                    dataAllTripExpenses.clear();
//                    Iterator<DataAllTripExpense> iterator = list.iterator();
//                    while (iterator.hasNext()) {
//
//                        String item = iterator.next().getBPName();
//                        if (item.isEmpty()) {
//                            iterator.remove();
//                        }
//                    }


                    //  dataAllTripExpenses.addAll(response.body().getData().get(0));

                    dataAllTripExpenses.addAll(response.body().getData());
                    allTripExpenseAdapter = new AllTripExpenseAdapter(LocationListing.this, dataAllTripExpenses);
                    layoutManager = new LinearLayoutManager(LocationListing.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(allTripExpenseAdapter);
                    //  allTripExpenseAdapter.notifyDataSetChanged();
                    nodatafoundimageExpense();

                }
            }

            @Override
            public void onFailure(Call<AllTripExpenseResponse> call, Throwable t) {
                binding.layoutLoader.loader.setVisibility(View.GONE);
            }
        });

    }


    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding bindingDate;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        bindingDate = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingDate.getRoot());
        bindingDate.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvCustomDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            // Toast.makeText(context, "today", Toast.LENGTH_SHORT).show();

            bottomSheetDialog.dismiss();
            dateRangeSelector();

        });
        bindingDate.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            //   from_to_date.setText(startDate + " - " + endDate);
            //  Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode,startDate,endDate);
            // from_to_date.setText(binding.tvTodayDateBottomSheetSelectDate.getText().toString());

            //   listener.onDataPassed(startDate,endDate);
            binding.dateText.setText("Today");
            callExpenseApiTrip(startDate, endDate);
            bottomSheetDialog.dismiss();
        });

        bindingDate.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            binding.dateText.setText("Yesterday");
            callExpenseApiTrip(startDate, endDate);
            //  from_to_date.setText(startDate + " - " + endDate);
            //   Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode,startDate,endDate);
            //  from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            //  listener.onDataPassed(startDate,endDate);
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            // from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode,startDate,endDate);
            //  from_to_date.setText(binding.tvThisWeekDateBottomSheetSelectDate.getText().toString());
            //  listener.onDataPassed(startDate,endDate);
            binding.dateText.setText("" + startDate + " - " + endDate);
            callExpenseApiTrip(startDate, endDate);
            bottomSheetDialog.dismiss();
        });


        bindingDate.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            //  from_to_date.setText(startDate + " - " + endDate);
            // Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callledgerapi(reportType, startDate, endDate);
            //  customerLedger(cardCode,startDate,endDate);
//            from_to_date.setText(binding.tvThisMonthBottomSheetSelectDate.getText().toString());

            binding.dateText.setText("This Month");
            callExpenseApiTrip(startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();

            binding.dateText.setText("Last Month");
            callExpenseApiTrip(startDate, endDate);

            //   from_to_date.setText(startDate + " - " + endDate);
            //   Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode, startDate, endDate);
//            callledgerapi(reportType, startDate, endDate);
            //  from_to_date.setText(binding.tvLastMonthDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();

            binding.dateText.setText("This Quarter");
            callExpenseApiTrip(startDate, endDate);
            //     from_to_date.setText(startDate + " - " + endDate);
            //    Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);

            //  customerLedger(cardCode, startDate, endDate);
            //  from_to_date.setText(binding.tvThisQuarterDateBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisYearFirstDate();
            endDate = Globals.thisYearLastDate();
            //    from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callledgerapi(reportType, startDate, endDate);
            //   customerLedger(cardCode, startDate, endDate);
            //   from_to_date.setText(binding.tvThisYearDateBottomSheetSelectDate.getText().toString());
            binding.dateText.setText("This Year");
            callExpenseApiTrip(startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callledgerapi(reportType, startDate, endDate);
            // customerLedger(cardCode, startDate, endDate);
            //   from_to_date.setText(binding.tvLastYearBottomSheetSelectDate.getText().toString());
            binding.dateText.setText("Last Year");
            callExpenseApiTrip(startDate, endDate);
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvAllBottomSheetSelectDate.setOnClickListener(view -> {

            binding.dateText.setText("All");
            callExpenseApiTrip("", "");
            // from_to_date.setText(binding.tvAllBottomSheetSelectDate.getText().toString());
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }
//        materialDatePicker.surface

        materialDatePicker.show(getSupportFragmentManager(), "Tag_Picker");
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
                // from_to_date.setText(startDate + " - " + endDate);
                binding.dateText.setText("" + startDate + " - " + endDate);
                callExpenseApiTrip(startDate, endDate);
                //   customerLedger(cardCode, startDate, endDate);
            }
        });


    }


    public void nodatafoundimage() {
        if (expenseAdapter.getItemCount() == 0) {
            no_datafound.setVisibility(View.VISIBLE);
        } else {
            no_datafound.setVisibility(View.GONE);
        }
    }

    public void nodatafoundimageExpense() {
        if (allTripExpenseAdapter.getItemCount() == 0) {
            no_datafound.setVisibility(View.VISIBLE);
        } else {
            no_datafound.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.campaign_filter, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView((this).getSupportActionBar().getThemedContext());
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.plus).setVisible(false);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setQueryHint("Search Expense");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return true;
            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:

                break;
            case R.id.plus:
                //   startActivity(new Intent(this, AddExpense.class));
                break;


            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;
    }
}
