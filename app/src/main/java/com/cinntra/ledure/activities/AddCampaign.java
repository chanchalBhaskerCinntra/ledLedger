package com.cinntra.ledure.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CountryAdapter;
import com.cinntra.ledure.adapters.IndustrySpinnerAdapter;
import com.cinntra.ledure.adapters.LeadTypeAdapter;
import com.cinntra.ledure.adapters.SalesEmployeeAdapter;
import com.cinntra.ledure.adapters.StateAdapter;
import com.cinntra.ledure.animation.ViewAnimationUtils;
import com.cinntra.ledure.fragments.Dashboard;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.AddCampaignModel;
import com.cinntra.ledure.model.CampaignResponse;
import com.cinntra.ledure.model.IndustryItem;
import com.cinntra.ledure.model.LeadTypeData;
import com.cinntra.ledure.model.LeadTypeResponse;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.StateData;
import com.cinntra.ledure.model.StateRespose;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.viewModel.ItemViewModel;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCampaign extends MainBaseActivity implements View.OnClickListener {



    @BindView(R.id.companyname)
    EditText companyname;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.from_date)
    EditText from_date;
    @BindView(R.id.to_date)
    EditText to_date;
    @BindView(R.id.oppfrom_date)
    EditText oppfrom_date;
    @BindView(R.id.oppto_date)
    EditText oppto_date;
    @BindView(R.id.customerfrom_date)
    EditText customerfrom_date;
    @BindView(R.id.customerto_date)
    EditText customerto_date;
    @BindView(R.id.campaign_spinner)
    Spinner campaign_spinner;
    @BindView(R.id.source_spinner)
    Spinner source_spinner;
    @BindView(R.id.priority_spinner)
    Spinner priority_spinner;
    @BindView(R.id.status_spinner)
    Spinner status_spinner;
    @BindView(R.id.type_spinner)
    Spinner type_spinner;
    @BindView(R.id.customertype_spinner)
    Spinner customertype_spinner;
    @BindView(R.id.country_spinner)
    Spinner country_spinner;
    @BindView(R.id.state_spinner)
    Spinner state_spinner;
    @BindView(R.id.industry_spinner)
    Spinner industry_spinner;
    @BindView(R.id.customersales_employee_spinner)
    Spinner customersales_employee_spinner;
    @BindView(R.id.salesemployee_spinner)
    Spinner salesemployee_spinner;
    @BindView(R.id.create)
    Button create;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.all_bp)
    CheckBox all_bp;
    @BindView(R.id.all_opp)
    CheckBox all_opp;
    @BindView(R.id.all_lead)
    CheckBox all_lead;
    @BindView(R.id.lead_view)
    LinearLayout lead_view;
    @BindView(R.id.opp_view)
    LinearLayout opp_view;
    @BindView(R.id.bPview)
    LinearLayout bpView;

    CountryAdapter countryAdapter;
    String Countrycode,CountryName, StateName, StateCode, industryCode,sourcetype,leadtype;
    ArrayList<LeadTypeData> sourceData = new ArrayList<>();
    ArrayList<LeadTypeData> leadTypeData = new ArrayList<>();
    List<SalesEmployeeItem> salesEmployeeItemList = new ArrayList<>();

    String CustomerType, OppType, CampaignAccess = "--None--";
    String OppsalesEmployeeCode, OppsalesEmployeename, CustomersalesEmployeeCode, CustomersalesEmployeename;
    String status = "Follow Up";
    StateAdapter stateAdapter;
    ArrayList<StateData> stateList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_campaign);
        ButterKnife.bind(this);
        eventmanager();






    }

    private void eventmanager() {

        head_title.setText("Add Campaign Set");
        back_press.setOnClickListener(this);
        from_date.setOnClickListener(this);
        to_date.setOnClickListener(this);
        oppfrom_date.setOnClickListener(this);
        oppto_date.setOnClickListener(this);
        customerfrom_date.setOnClickListener(this);
        customerto_date.setOnClickListener(this);
        create.setOnClickListener(this);


      all_opp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(isChecked)
                  ViewAnimationUtils.collapse(opp_view);
//                  opp_view.setVisibility(View.GONE);
              else
              ViewAnimationUtils.expand(opp_view);
//                  opp_view.setVisibility(View.VISIBLE);
          }
      });

        all_lead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    ViewAnimationUtils.collapse(lead_view);
//                    lead_view.setVisibility(View.GONE);
                else
                    ViewAnimationUtils.expand(lead_view);
//                    lead_view.setVisibility(View.VISIBLE);
            }
        });
        all_bp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    ViewAnimationUtils.collapse(bpView);
//                    bpView.setVisibility(View.GONE);
                else
                    ViewAnimationUtils.expand(bpView);
//                    bpView.setVisibility(View.VISIBLE);
            }
        });




        callleadTypeApi();

        countryAdapter = new CountryAdapter(AddCampaign.this, Dashboard.countrylist);
        country_spinner.setAdapter(countryAdapter);
        country_spinner.setSelection(Globals.getCountrypos(Dashboard.countrylist,"India"));

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Countrycode = Dashboard.countrylist.get(position).getCode();
               CountryName = Dashboard.countrylist.get(position).getName();
                callStateApi(Countrycode);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Countrycode = Dashboard.countrylist.get(0).getCode();
                CountryName = Dashboard.countrylist.get(0).getName();
            }
        });

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StateName= stateList.get(position).getName();
                StateCode= stateList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                StateName= stateList.get(0).getName();
                StateCode= stateList.get(0).getCode();
            }
        });


        customertype_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomerType= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CustomerType= parent.getSelectedItem().toString();
            }
        });


         status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                status= parent.getSelectedItem().toString();
            }
        });

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OppType= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                OppType= parent.getSelectedItem().toString();
            }
        });


         campaign_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CampaignAccess= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CampaignAccess= parent.getSelectedItem().toString();
            }
        });


        priority_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leadtype= leadTypeData.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                leadtype= leadTypeData.get(0).getName();
            }
        });



        source_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourcetype= sourceData.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sourcetype= sourceData.get(0).getName();
            }
        });

        industry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                industryCode= IndustryItemItemList.get(position).getIndustryCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                industryCode= IndustryItemItemList.get(0).getIndustryCode();
            }
        });




    }



    private void callleadTypeApi() {

        Call<LeadTypeResponse> call = NewApiClient.getInstance().getApiService(this).getLeadType();
        call.enqueue(new Callback<LeadTypeResponse>() {
            @Override
            public void onResponse(Call<LeadTypeResponse> call, Response<LeadTypeResponse> response) {

                if(response.code()==200)
                {
                    leadTypeData.clear();
                    leadTypeData.addAll(response.body().getData());
                    priority_spinner.setAdapter(new LeadTypeAdapter(AddCampaign.this,leadTypeData));
                    leadtype = leadTypeData.get(0).getName();
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, LeadResponse.class);
                        Toast.makeText(AddCampaign.this, mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }

            }
            @Override
            public void onFailure(Call<LeadTypeResponse> call, Throwable t) {

                Toast.makeText(AddCampaign.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        callSourceApi();
    }



    private void callSourceApi() {


        Call<LeadTypeResponse> call = NewApiClient.getInstance().getApiService(this).getsourceType();
        call.enqueue(new Callback<LeadTypeResponse>() {
            @Override
            public void onResponse(Call<LeadTypeResponse> call, Response<LeadTypeResponse> response) {

                if(response.code()==200)
                {
                    sourceData.clear();
                    sourceData.addAll(response.body().getData());
                    source_spinner.setAdapter(new LeadTypeAdapter(AddCampaign.this,sourceData));
                    sourcetype = sourceData.get(0).getName();
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, LeadResponse.class);
                        Toast.makeText(AddCampaign.this, mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }

            }
            @Override
            public void onFailure(Call<LeadTypeResponse> call, Throwable t) {

                Toast.makeText(AddCampaign.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        callIndustryApi();
    }






    List<IndustryItem> IndustryItemItemList = new ArrayList<>();
    private void callIndustryApi()
    {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getIndustryList().observe(this, new Observer<List<IndustryItem>>() {
            @Override
            public void onChanged(@Nullable List<IndustryItem> itemsList) {
                if(itemsList == null || itemsList.size()== 0){
                    Globals.setmessage(AddCampaign.this);
                }else {
                    IndustryItemItemList = itemsList;
                    industry_spinner.setAdapter(new IndustrySpinnerAdapter(AddCampaign.this,itemsList));
                    industryCode = IndustryItemItemList.get(0).getIndustryCode();

                }
            }
        });


        callSalesEmployeeApi();
    }

    private void callSalesEmployeeApi() {

        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getSalesEmployeeList().observe(AddCampaign.this, new Observer<List<SalesEmployeeItem>>() {
            @Override
            public void onChanged(@Nullable List<SalesEmployeeItem> itemsList) {
                if(itemsList == null || itemsList.size() == 0){
                    Globals.setmessage(AddCampaign.this);
                }else {
                    salesEmployeeItemList = itemsList;
                    salesemployee_spinner.setAdapter(new SalesEmployeeAdapter(AddCampaign.this,itemsList));
                    customersales_employee_spinner.setAdapter(new SalesEmployeeAdapter(AddCampaign.this,itemsList));
                    OppsalesEmployeeCode = salesEmployeeItemList.get(0).getSalesEmployeeCode();
                    OppsalesEmployeename = salesEmployeeItemList.get(0).getSalesEmployeeName();
                    CustomersalesEmployeeCode = salesEmployeeItemList.get(0).getSalesEmployeeCode();
                    CustomersalesEmployeename =salesEmployeeItemList.get(0).getSalesEmployeeName();

                }
            }
        });
    }

    private void callStateApi(String countryCode) {

        StateData stateData = new StateData();
        stateData.setCountry(countryCode);
        Call<StateRespose> call = NewApiClient.getInstance().getApiService(this).getStateList(stateData);
        call.enqueue(new Callback<StateRespose>() {
            @Override
            public void onResponse(Call<StateRespose> call, Response<StateRespose> response) {

                if(response.code()==200)
                {

                        stateList.clear();
                        if(response.body().getData().size()>0) {

                            stateList.addAll(response.body().getData());
                        }else{
                            StateData sta = new StateData();
                            sta.setName("Select State");
                            stateList.add(sta);
                        }
                        stateAdapter = new StateAdapter(AddCampaign.this,stateList);
                        state_spinner.setAdapter(stateAdapter);
                        stateAdapter.notifyDataSetChanged();
                        StateName= stateList.get(0).getName();
                        StateCode= stateList.get(0).getCode();



                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(AddCampaign.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<StateRespose> call, Throwable t) {

                Toast.makeText(AddCampaign.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_press:
                onBackPressed();
                break;
            case R.id.from_date:
                Globals.selectDate(this,from_date);
                break;
            case R.id.to_date:
                Globals.selectDate(this,to_date);
                break;
            case R.id.oppfrom_date:
                Globals.selectDate(this,oppfrom_date);
                break;
            case R.id.oppto_date:
                Globals.selectDate(this,oppto_date);
                break;
            case R.id.customerfrom_date:
                Globals.selectDate(this,customerfrom_date);
                break;
            case R.id.customerto_date:
                Globals.selectDate(this,customerto_date);
                break;
            case R.id.create:

                if(validation(CampaignAccess,companyname,description)){
                    AddCampaignModel campaignModel = new AddCampaignModel();
                    campaignModel.setCampaignSetName(companyname.getText().toString());
                    campaignModel.setCampaignAccess(CampaignAccess);
                    campaignModel.setDescription(description.getText().toString());
                    campaignModel.setLeadSource(sourcetype);
                    campaignModel.setLeadPriority(leadtype);
                    campaignModel.setLeadFromDate(from_date.getText().toString());
                    campaignModel.setLeadToDate(to_date.getText().toString());
                    campaignModel.setBPFromDate(customerfrom_date.getText().toString());
                    campaignModel.setBPToDate(customerto_date.getText().toString());
                    campaignModel.setOppFromDate(oppfrom_date.getText().toString());
                    campaignModel.setOppToDate(oppto_date.getText().toString());
                    campaignModel.setLeadStatus(status);
                    campaignModel.setOppType(OppType);
                    campaignModel.setBPType(CustomerType);
                    campaignModel.setBPState(StateName);
                    campaignModel.setBPStateCode(StateCode);
                    campaignModel.setBPCountry(CountryName);
                    campaignModel.setBPCountryCode(Countrycode);
                    campaignModel.setCreateDate(Globals.getTodaysDate());
                    campaignModel.setCreateTime(Globals.getTCurrentTime());
                    campaignModel.setBPIndustry(industryCode);
                    campaignModel.setOppSalePerson(OppsalesEmployeeCode);
                    campaignModel.setBPSalePerson(CustomersalesEmployeeCode);
                    campaignModel.setCreateBy(Globals.TeamSalesEmployeCode);
                    campaignModel.setCampaignSetOwner(Globals.TeamSalesEmployeCode);
                    campaignModel.setMemberList("");
                    campaignModel.setOppStage("");
                    campaignModel.setStatus(0);
                    campaignModel.setAllBP(all_bp.isChecked()?1:0);
                    campaignModel.setAllLead(all_lead.isChecked()?1:0);
                    campaignModel.setAllOpp(all_opp.isChecked()?1:0);

                    if(Globals.checkInternet(AddCampaign.this))
                        createnewCampaign(campaignModel);


                }


                break;
        }
    }

    private void createnewCampaign(AddCampaignModel campaignModel) {
        Call<CampaignResponse> call = NewApiClient.getInstance().getApiService(this).createCampaign(campaignModel);
        call.enqueue(new Callback<CampaignResponse>() {
            @Override
            public void onResponse(Call<CampaignResponse> call, Response<CampaignResponse> response) {

                if(response.code()==200)
                {

                    Toasty.success(AddCampaign.this,"Posted Successfull", Toasty.LENGTH_LONG).show();
                    onBackPressed();


                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(AddCampaign.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CampaignResponse> call, Throwable t) {

                Toast.makeText(AddCampaign.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validation(String campaignAccess, EditText companyname, EditText description) {
    if(campaignAccess.equalsIgnoreCase("--None--")){
        Toasty.warning(this,"Select Campaign Access",Toasty.LENGTH_SHORT).show();
        return false;
    }else if(companyname.length()==0){
        Toasty.warning(this,"Enter Campaign Set Name",Toasty.LENGTH_SHORT).show();
        return false;
    }else if(description.length()==0){
        Toasty.warning(this,"Enter Description",Toasty.LENGTH_SHORT).show();
        return false;
    }
        return true;
    }


    @Override
    public void onBackPressed()
    {
        if(setAlertDataDiscard(AddCampaign.this))
        {
            super.onBackPressed();
        }


    }
    boolean Dcstatus = true;
    private boolean setAlertDataDiscard(Context context)
    {
        Dcstatus =false;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.data_discard)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Dcstatus =true;
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Dcstatus =false;
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        // alert.setTitle(R.string.data_discard_sub);
        alert.show();
        return Dcstatus;

    }
}
