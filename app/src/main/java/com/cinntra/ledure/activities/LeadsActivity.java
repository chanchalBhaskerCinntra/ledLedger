package com.cinntra.ledure.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.LeadsAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.LeadFilter;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeadsActivity extends MainBaseActivity implements View.OnClickListener, com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener {


    Context mContext;
    @BindView(R.id.customer_lead_List)
    RecyclerView recyclerView;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.idPBLoading)
    ProgressBar idPBLoading;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.lead_typeSpinner)
    Spinner lead_typeSpinner;
    @BindView(R.id.dotcolor)
    ImageView dotcolor;
    @BindView(R.id.dateText)
    TextView dateText;
    @BindView(R.id.calendar)
    ImageView calendar;
    @BindView(R.id.all_list)
    TextView all_list;
    @BindView(R.id.nestedSV)
    NestedScrollView nestedSV;
    List<LeadValue> leadValueList = new ArrayList<>();

    LeadsAdapter adapter;
    LeadFilter value = new LeadFilter();

    int pageno= 1 ;
    boolean recallapi = true;

    public LeadsActivity() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lead);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);

        mContext = LeadsActivity.this;
        value.setAssignedTo(Globals.TeamEmployeeID);
       value.setLeadType("All");
        calendar.setOnClickListener(this);
        all_list.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(LeadsActivity.this)) {
                    leadValueList.clear();
                    pageno = 1;
                    recallapi = true;
                    callApi(value);
                    dateText.setVisibility(View.GONE);
                    lead_typeSpinner.setSelection(0);

                } else
                    swipeRefreshLayout.setRefreshing(false);

            }
        });



        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.




                        if(Globals.checkInternet(LeadsActivity.this)&&recallapi) {

                            pageno++;
                            idPBLoading.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.VISIBLE);
                            callApi(value);
                        }



                }

            }


        });




    /*    lead_typeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    dotcolor.setBackground(getDrawable(R.drawable.ic_green_dot));
                    callApi(value);
                    nodatafoundimage();
                }
            }
        });*/
        lead_typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adapter!=null) {
                    dateText.setVisibility(View.GONE);
                    if (position == 0) {
                        dotcolor.setBackground(getDrawable(R.drawable.red_dot));
                        adapter.priorityfilter("Hot");
                        nodatafoundimage();
                    } else if (position == 1) {
                        dotcolor.setBackground(getDrawable(R.drawable.ic_blue_dot));
                        adapter.priorityfilter("Cold");
                        nodatafoundimage();
                    } else {
                        dotcolor.setBackground(getDrawable(R.drawable.orange_dot));
                        adapter.priorityfilter("Warm");
                        nodatafoundimage();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Globals.checkInternet(mContext)) {
            loader.setVisibility(View.VISIBLE);
            leadValueList.clear();
            pageno = 1;
            recallapi = true;
            callApi(value);
        } else {
            Toasty.error(mContext, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void callApi(LeadFilter leadValue) {
            leadValue.setPageNo(pageno);
            leadValue.setMaxItem(50);


//        leadValue.setLeadType("2");
        Call<LeadResponse> call = NewApiClient.getInstance().getApiService().getAllLead(leadValue);
        call.enqueue(new Callback<LeadResponse>() {
            @Override
            public void onResponse(Call<LeadResponse> call, Response<LeadResponse> response) {
                if(response.code()==200)
                {
                    recallapi = response.body().getData().size()>=50;
                    leadValueList.addAll(response.body().getData());
                    adapter = new LeadsAdapter(mContext,leadValueList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else
                {

                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(LeadsActivity.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                 }
                nodatafoundimage();
                swipeRefreshLayout.setRefreshing(false);
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<LeadResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                loader.setVisibility(View.GONE);
                Toast.makeText(LeadsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.search:

                break;
            case R.id.plus:
                startActivity(new Intent(this, AddLead.class));
                break;
            case R.id.follow_up:

                adapter.leadfilter("Follow Up");
                nodatafoundimage();

                break;
                case R.id.all:

                adapter.leadfilter("");
                nodatafoundimage();

                break;
            case R.id.meeting:

                adapter.leadfilter("Purposal shared");
                nodatafoundimage();
                break;
            case R.id.demo:

                adapter.leadfilter("Demo");
                nodatafoundimage();
                break;
            case R.id.dead:

                adapter.leadfilter("Dead");
                nodatafoundimage();
                break;
            case R.id.hold:

                adapter.leadfilter("Hold");
                nodatafoundimage();
                break;
            case R.id.negotiation:

                adapter.leadfilter("Negotiation");
                nodatafoundimage();
                break;


            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lead_filter, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(((LeadsActivity) mContext).getSupportActionBar().getThemedContext());

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setQueryHint("Search Lead");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                if(adapter!=null)
                    adapter.filter(newText);


                return true;
            }
        });


        return true;
    }
    @Override
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    public  void  nodatafoundimage(){
        if(adapter.getItemCount()==0){
            no_datafound.setVisibility(View.VISIBLE);
        }else{
            no_datafound.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calendar:
                //openDateDialog();
                opencalendardialog();
                break;
            case R.id.all_list:
                loader.setVisibility(View.VISIBLE);
                leadValueList.clear();
                pageno = 1;
                recallapi = true;
                callApi(value);
                dateText.setVisibility(View.GONE);
                break;
        }
    }

    private void opencalendardialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        com.borax12.materialdaterangepicker.date.DatePickerDialog datePickerDialog = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(LeadsActivity.this,mYear,mMonth,mDay);
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");

    }
     String[] Date = new String[2];

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(com.borax12.materialdaterangepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Date[0] = dayOfMonth +"-"+(monthOfYear+1)+"-"+year;
        Date[1] = dayOfMonthEnd +"-"+(monthOfYearEnd+1)+"-"+yearEnd;

        SimpleDateFormat format1 = new SimpleDateFormat("dd-M-yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startdate = null;
        java.util.Date enddate = null;
        try {
            startdate = format1.parse(Date[0]);
            enddate = format1.parse(Date[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        dateText.setVisibility(View.VISIBLE);
        dateText.setText(format2.format(startdate) + " to " + format2.format(enddate));
       adapter.datefilter(format2.format(startdate),format2.format(enddate));

        nodatafoundimage();
    }
}