package com.cinntra.ledure.activities;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.BPTypeSpinnerAdapter;
import com.cinntra.ledure.adapters.ContactPersonAdapter;
import com.cinntra.ledure.adapters.SalesEmployeeAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ContactPerson;
import com.cinntra.ledure.model.ContactPersonData;
import com.cinntra.ledure.model.NewOppResponse;
import com.cinntra.ledure.model.OwnerItem;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.SalesOpportunitiesLines;
import com.cinntra.ledure.model.UTypeData;
import com.cinntra.ledure.newapimodel.AddOpportunityModel;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.viewModel.ItemViewModel;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddOpportunityActivity extends MainBaseActivity implements View.OnClickListener {

    public static int PARTNERCODE = 100;
    public static int OWNERCODE   = 1001;
    public static int LeadCode = 101;
    Activity act;
    int salesEmployeeCode = 0;
    String salesEmployeename = "";
    String ContactPersonName     = "";
    String ContactPersonCode     = "";

    String stagesCode     = "No";
    String TYPE           = "";
    String LEAD_SOURCE    = "";

     @BindView(R.id.head_title)
     TextView head_title;
     @BindView(R.id.back_press)
     RelativeLayout back_press;
     @BindView(R.id.opportunity_name_value)
     EditText opportunity_name_value;
     @BindView(R.id.business_partner_value)
     EditText business_partner_value;
     @BindView(R.id.contact_person_spinner)
     Spinner contact_person_spinner;
     @BindView(R.id.close_date_value)
     EditText close_date_value;
     @BindView(R.id.opportunity_owner_value)
     EditText opportunity_owner_value;
     @BindView(R.id.sales_employee_spinner)
     Spinner sales_employee_spinner;
     @BindView(R.id.type_spinner)
     Spinner type_spinner;
     @BindView(R.id.probability_value)
     EditText probability_value;
     @BindView(R.id.potential_amount_value)
     EditText potential_amount_value;
     @BindView(R.id.lead_source_spinner)
     Spinner lead_source_spinner;
     @BindView(R.id.stage_spinner)
     Spinner stage_spinner;
     @BindView(R.id.description_value)
     EditText description_value;
     @BindView(R.id.bussinessPartner)
     RelativeLayout bussinessPartner;
     @BindView(R.id.owener)
     RelativeLayout owener;
     @BindView(R.id.submit_button)
     Button submit_button;
     @BindView(R.id.startDate)
     RelativeLayout startDate;
     @BindView(R.id.loader)
     ProgressBar loader;
     @BindView(R.id.start_date_value)
     EditText start_date_value;
     @BindView(R.id.startcalender)
     ImageView startcalender;
    @BindView(R.id.closeDate)
    RelativeLayout closeDate;
    @BindView(R.id.closeCalender)
    ImageView closeCalender;
    @BindView(R.id.lead_view)
    RelativeLayout lead_view;
    @BindView(R.id.lead_value)
    EditText lead_value;


    String DataOwnershipfield = "";
    String LeadID = "";
    String CardName = "";
    String cardValue="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.add_opportunity);
    ButterKnife.bind(this);
    act = AddOpportunityActivity.this;
    setDefaults();
    eventManager();
    if(Globals.checkInternet(this)){
        callSalessApi();
    }
     }



    List<UTypeData> utypelist = new ArrayList<>();
    private void callUTypeApi() {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getOPpTypeList().observe(this, new Observer<List<UTypeData>>() {
            @Override
            public void onChanged(@Nullable List<UTypeData> itemsList) {
                if(itemsList == null || itemsList.size()== 0){
                    Globals.setmessage( act);
                }else {
                    utypelist = itemsList;
                    type_spinner.setAdapter(new BPTypeSpinnerAdapter(act,itemsList));
                    TYPE = utypelist.get(0).getId().toString();

                }
            }
        });
    }

    private void eventManager() {
        sales_employee_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          if (salesEmployeeItemList.size() > 0 && position > 0) {
              salesEmployeename = salesEmployeeItemList.get(position).getSalesEmployeeName();
              salesEmployeeCode = Integer.valueOf(salesEmployeeItemList.get(position).getSalesEmployeeCode());
          }
      }
      @Override
      public void onNothingSelected(AdapterView<?> parent)

          {
              salesEmployeename = salesEmployeeItemList.get(0).getSalesEmployeeName();
              salesEmployeeCode = Integer.valueOf(salesEmployeeItemList.get(0).getSalesEmployeeCode());

          }
        });

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
          {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             if(utypelist.size()>0)
                TYPE = utypelist.get(position).getId().toString();
            }
         @Override
         public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        lead_source_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
          {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LEAD_SOURCE = lead_source_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                LEAD_SOURCE = lead_source_spinner.getSelectedItem().toString();
            }
        });

        contact_person_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ContactPersonName = ContactEmployeesList.get(position).getFirstName();
                ContactPersonCode = ContactEmployeesList.get(position).getInternalCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ContactPersonName = ContactEmployeesList.get(0).getFirstName();
                ContactPersonCode = ContactEmployeesList.get(0).getInternalCode();
            }
        });

    }


    private void setDefaults()
            {
     head_title.setText(getResources().getString(R.string.add_opportunity));
     back_press.setOnClickListener(this);
     bussinessPartner.setOnClickListener(this);
     owener.setOnClickListener(this);
     submit_button.setOnClickListener(this);
     business_partner_value.setOnClickListener(this);
     start_date_value.setOnClickListener(this);
     opportunity_owner_value.setOnClickListener(this);
     startDate.setOnClickListener(this);
     startcalender.setOnClickListener(this);
     closeDate.setOnClickListener(this);
     close_date_value.setOnClickListener(this);
     startcalender.setOnClickListener(this);
    lead_view.setOnClickListener(this);
    lead_value.setOnClickListener(this);
     start_date_value.setText(Globals.getTodaysDate());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
     if (requestCode == PARTNERCODE&&resultCode == RESULT_OK) {

       BusinessPartnerData customerItem = (BusinessPartnerData) data.getSerializableExtra(Globals.CustomerItemData);
       setData(customerItem);

        }
     else  if(requestCode == OWNERCODE&&resultCode == RESULT_OK)
     {
    OwnerItem ownerItem = (OwnerItem) data.getSerializableExtra(Globals.OwnerItemData);
    opportunity_owner_value.setText(ownerItem.getFirstName()+" "+ownerItem.getMiddleName()+" "+ownerItem.getLastName());
         DataOwnershipfield = ownerItem.getEmployeeID();
       }else if(requestCode==LeadCode &&resultCode==RESULT_OK)
     {
         LeadValue leadValue =data.getParcelableExtra(Globals.Lead_Data);
         lead_value.setText(leadValue.getCompanyName());
         LeadID = leadValue.getId().toString();
     }
    }

       @Override
       public void onClick(View v)
         {
    switch (v.getId())
           {
    case R.id.back_press:
        finish();
        break;
        case R.id.startcalender:
               case R.id.start_date_value:
               case R.id.startDate:
                   startDate();
          break;
               case R.id.closeCalender:
               case R.id.close_date_value:
               case R.id.closeDate:
                   closetDate();
            break;

               case R.id.bussinessPartner:
        case R.id.business_partner_value:
            selectBPartner();
           /* if(LeadID.isEmpty()){
                Toasty.warning(this,"Select Lead First",Toasty.LENGTH_SHORT).show();
            }else {

            }*/
        break;
        case R.id.lead_value:
               case R.id.lead_view:
                   Prefs.putString(Globals.BussinessPageType,"AddOpportunityLead");
                   Intent i = new Intent(AddOpportunityActivity.this, LeadsActivity.class);
                   startActivityForResult(i,LeadCode);
                   break;
        case R.id.owener:
        case R.id.opportunity_owner_value:

            Intent ii = new Intent(AddOpportunityActivity.this,OwnerList.class);
            startActivityForResult(ii,OWNERCODE);
            break;
        case R.id.submit_button:


             String       remark = description_value.getText().toString().trim();
             if(validation(cardValue,salesEmployeeCode,potential_amount_value.getText().toString().trim(),remark))
                {
             jsonlist.clear();
             SalesOpportunitiesLines dc = new SalesOpportunitiesLines();
             dc.setSalesPerson(salesEmployeeCode);
             dc.setDocumentType("bodt_MinusOne");
             String vv= potential_amount_value.getText().toString().trim();
             if(vv.isEmpty())
                 vv=""+0;
             dc.setMaxLocalTotal(Float.valueOf(vv));
             dc.setStageKey("");
             jsonlist.add(dc);

             AddOpportunityModel obj = new AddOpportunityModel();
             obj.setOpportunityName(opportunity_name_value.getText().toString().trim());
             obj.setClosingDate(close_date_value.getText().toString().trim());
             obj.setPredictedClosingDate(close_date_value.getText().toString().trim());
             obj.setStartDate(Globals.getTodaysDate());
             obj.setUType(TYPE);
             obj.setCustomerName(CardName);
             obj.setUFav("N");
             obj.setULsource(LEAD_SOURCE);
             obj.setUProblty(probability_value.getText().toString().trim());
            // obj.setDataOwnershipfield(DataOwnershipfield);
             obj.setCardCode(cardValue); //cardcode
             obj.setSalesPerson(String.valueOf(salesEmployeeCode));
             obj.setContactPerson(ContactPersonCode);
             obj.setMaxLocalTotal(potential_amount_value.getText().toString().trim());//Potential Ammount
             obj.setRemarks(remark);
             obj.setMaxSystemTotal("0.7576");
             obj.setStatus("sos_Open");
             obj.setReasonForClosing("None");
             obj.setTotalAmountLocal("5.0");
             obj.setTotalAmounSystem("0.075");
             obj.setCurrentStageNo("2");
             obj.setIndustry("None");
             obj.setOppItem(new ArrayList<>());
             obj.setLinkedDocumentType("None");
             obj.setStatusRemarks("None");
             obj.setProjectCode("None");
             obj.setClosingType("sos_Days");
             obj.setOpportunityType("boOpSales");
             obj.setUpdateDate(Globals.getTodaysDate());
             obj.setUpdateTime(Globals.getTCurrentTime());
             obj.setSalesOpportunitiesLines(jsonlist);
             obj.setSource("None");
             obj.setDataOwnershipfield(String.valueOf(salesEmployeeCode));
            obj.setSalesPersonName(salesEmployeename);
            obj.setContactPersonName(ContactPersonName);
            obj.setDataOwnershipName(salesEmployeename);
             obj.setStartDate(Globals.getTodaysDate());
             obj.setU_LEADID(LeadID);
             obj.setU_LEADNM(lead_value.getText().toString());
             if(Globals.checkInternet(getApplicationContext()))
                 addQuotation(obj);
                 }


            break;

          }
       }

    private void startDate()
        {
       Globals.selectDate(AddOpportunityActivity.this,start_date_value);
        }
    private void closetDate() {
        Globals.selectDate(AddOpportunityActivity.this,close_date_value);
    }


    private void selectBPartner()
         {
        Prefs.putString(Globals.BussinessPageType,"AddOpportunity");
        Intent i = new Intent(AddOpportunityActivity.this, BussinessPartners.class);
        startActivityForResult(i,PARTNERCODE);
           }

     ArrayList<SalesOpportunitiesLines> jsonlist = new ArrayList<>();
      private ArrayList<ContactPersonData>   ContactEmployeesList;
      private ContactPersonAdapter  contactPersonAdapter;
     private void setData(BusinessPartnerData customerItem)
         {

          ContactEmployeesList = new ArrayList<>();
             CardName= customerItem.getCardName();
             cardValue= customerItem.getCardCode();
             callContactEmployeeApi(customerItem.getCardCode());




          business_partner_value.setText(customerItem.getCardName());

          if(ContactEmployeesList.size()>0)
             ContactPersonCode = ContactEmployeesList.get(0).getInternalCode();

           }

    private void callContactEmployeeApi(String id) {
         ContactPersonData contactPersonData = new ContactPersonData();
         contactPersonData.setCardCode(id);
        loader.setVisibility(View.VISIBLE);
        Call<ContactPerson> call = NewApiClient.getInstance().getApiService(this).contactemplist(contactPersonData);
        call.enqueue(new Callback<ContactPerson>() {
            @Override
            public void onResponse(Call<ContactPerson> call, Response<ContactPerson> response) {
                loader.setVisibility(View.GONE);
                if(response.code()==200)
                {
                    if(response.body().getData().size()>0) {
                        ContactEmployeesList.clear();
                        ContactEmployeesList.addAll(response.body().getData());
                    }else{

                            ContactPersonData contactEmployees = new ContactPersonData();
                            contactEmployees.setFirstName("No Contact Person");
                            contactEmployees.setInternalCode("-1");

                            ContactEmployeesList.add(contactEmployees);

                    }
                    contactPersonAdapter =new ContactPersonAdapter(AddOpportunityActivity.this,ContactEmployeesList);
                    contact_person_spinner.setAdapter(contactPersonAdapter);

                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(AddOpportunityActivity.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ContactPerson> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(AddOpportunityActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }




    public List<SalesEmployeeItem> salesEmployeeItemList = new ArrayList<>();
    private void callSalessApi()
      {
    ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
    model.getSalesEmployeeList().observe(this, new Observer<List<SalesEmployeeItem>>() {
     @Override
     public void onChanged(@Nullable List<SalesEmployeeItem> itemsList) {
         if(itemsList == null || itemsList.size() == 0){
             Globals.setmessage(getApplicationContext());
         }else{
             salesEmployeeItemList = itemsList;
             sales_employee_spinner.setAdapter(new SalesEmployeeAdapter(AddOpportunityActivity.this,itemsList));
             salesEmployeename = salesEmployeeItemList.get(0).getSalesEmployeeName();
             salesEmployeeCode = Integer.valueOf(salesEmployeeItemList.get(0).getSalesEmployeeCode());

         }
            }
        });
          callUTypeApi();
     }


    private void addQuotation(AddOpportunityModel in)
       {
         loader.setVisibility(View.VISIBLE);
        Call<NewOppResponse> call = NewApiClient.getInstance().getApiService(this).createopportunity(in);
        call.enqueue(new Callback<NewOppResponse>() {
            @Override
            public void onResponse(Call<NewOppResponse> call, Response<NewOppResponse> response) {
                loader.setVisibility(View.GONE);
                if(response.code()==200)
                {

                    if(response.body().getStatus().equalsIgnoreCase("200")){
                        Toasty.success(AddOpportunityActivity.this, "Add Successfully", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }else{
                        Toasty.warning(AddOpportunityActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(AddOpportunityActivity.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<NewOppResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toasty.error(AddOpportunityActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validation(
    String cardCode,int
    salesEmployeeCode,
    String potentialAmount,String remark)
        {
   if(cardCode.isEmpty())
      {
    Globals.showMessage(act,getString(R.string.select_bp));
    return false;
      }

   else if(ContactPersonCode.equalsIgnoreCase("-1")){
       Globals.showMessage(act,getString(R.string.enter_cp));
       return false;
   }

   else if(opportunity_name_value.getText().toString().trim().length()==0){
       opportunity_name_value.requestFocus();
       opportunity_name_value.setError(getString(R.string.enter_opp));
       Globals.showMessage(act,getString(R.string.enter_opp));
       return false;
   }
   else if(close_date_value.getText().toString().trim().length()==0){
       Globals.showMessage(act,"Enter closing date");
       return false;
   }
   else if(TYPE.equalsIgnoreCase("-None-")){
       Globals.showMessage(act,getString(R.string.enter_tye));
       return false;
   }
   else if(LEAD_SOURCE.equalsIgnoreCase("-None")){
       Globals.showMessage(act,getString(R.string.enter_lead_source));
       return false;
   }

   else if(salesEmployeeCode==0){
       Globals.showMessage(act,getString(R.string.enter_sp));
       return false;
   }

   else if(remark.isEmpty()){
       description_value.requestFocus();
       description_value.setError(getString(R.string.remark_error));
       Globals.showMessage(act,getString(R.string.remark_error));
       return false;
   }

    return true;
     }

    @Override
    public void onBackPressed()
    {
        if(setAlertDataDiscard(AddOpportunityActivity.this))
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