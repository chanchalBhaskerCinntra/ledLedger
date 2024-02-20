package com.cinntra.ledure.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.BPTypeSpinnerAdapter;
import com.cinntra.ledure.adapters.CountryAdapter;
import com.cinntra.ledure.adapters.IndustrySpinnerAdapter;
import com.cinntra.ledure.adapters.PaymentAdapter;
import com.cinntra.ledure.adapters.SalesEmployeeAdapter;
import com.cinntra.ledure.adapters.StateAdapter;
import com.cinntra.ledure.fragments.Dashboard;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.AddBusinessPartnerData;
import com.cinntra.ledure.model.BPAddress;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ContactEmployees;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.model.IndustryItem;
import com.cinntra.ledure.model.PayMentTerm;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.StateData;
import com.cinntra.ledure.model.StateRespose;
import com.cinntra.ledure.model.UTypeData;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.viewModel.CustomerViewModel;
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


public class AddBPCustomer extends MainBaseActivity implements View.OnClickListener {
    String TYPE        = "";
    String industryCode ="-1";
    String shippingType;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.name_value)
    EditText name_value;
    @BindView(R.id.contact_owner_value)
    EditText contact_owner_value;
    @BindView(R.id.tab_2)
    LinearLayout tab_2;
    @BindView(R.id.general)
    TextView general;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.tab_1)
    LinearLayout tab_1;
    @BindView(R.id.industry_view)
    RelativeLayout industry_view;
    @BindView(R.id.industry_spinner)
    Spinner industry_spinner;
    @BindView(R.id.type_view)
    RelativeLayout type_view;
    @BindView(R.id.type_spinner)
    Spinner type_spinner;
    @BindView(R.id.annual_revenue_value)
    EditText annual_revenue_value;
    @BindView(R.id.account_balance_value)
    EditText account_balance_value;
    @BindView(R.id.company_id_value)
    EditText company_id_value;
    @BindView(R.id.invoice_no_value)
    EditText invoice_no_value;
    @BindView(R.id.credit_limit_value)
    EditText credit_limit_value;
    @BindView(R.id.payment_term_value)
    Spinner payment_term_value;
    @BindView(R.id.parent_account_value)
    Spinner parent_account_value;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.mobile_value)
    EditText mobile_value;
    @BindView(R.id.email_value)
    EditText email_value;
    @BindView(R.id.website_value)
    EditText website_value;
    @BindView(R.id.bill_to_value)
    EditText bill_to_value;
    @BindView(R.id.ship_to_value)
    EditText ship_to_value;
    @BindView(R.id.remarks_value)
    EditText remarks_value;
    @BindView(R.id.upload_img)
    ImageView upload_img;
    @BindView(R.id.uploadview)
    RelativeLayout uploadview;
    @BindView(R.id.ship_block)
    LinearLayout ship_block;
    @BindView(R.id.general_frame)
    FrameLayout general_frame;
    @BindView(R.id.contact_frame)
    FrameLayout contact_frame;
    @BindView(R.id.create_button)
    Button create_button;
    @BindView(R.id.done_button)
    Button done_button;
    @BindView(R.id.sales_employee_spinner)
    Spinner sales_employee_spinner;
    @BindView(R.id.cardCode_value)
    EditText cardCode_value;
    @BindView(R.id.checkbox1)
    CheckBox checkbox1;

    @BindView(R.id.shipping_spinner)
    Spinner shipping_spinner;

    @BindView(R.id.shipping_spinner2)
    Spinner shipping_spinner2;
    @BindView(R.id.billing_name_value)
    EditText billing_name_value;
    @BindView(R.id.zip_code_value)
    EditText zip_code_value;
    @BindView(R.id.billing_address_value)
    EditText billing_address_value;

    @BindView(R.id.shipping_name_value)
    EditText shipping_name_value;
    @BindView(R.id.company_email_value)
    EditText company_email_value;
    @BindView(R.id.company_no_value)
    EditText company_no_value;
    @BindView(R.id.shipping_address_value)
    EditText shipping_address_value;
    @BindView(R.id.zipcode_value2)
    EditText zipcode_value2;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.country_value)
    Spinner country_value;
    @BindView(R.id.state_value)
    Spinner state_value;
    @BindView(R.id.ship_country_value)
    Spinner ship_country_value;
    @BindView(R.id.ship_state_value)
    Spinner ship_state_value;

    int salesEmployeeCode = 0;

    String payment_term = "";
    String parenT_account = "";
    String [] shippinngType;
    String billshipType ;
    String ship_shiptype;
    AppCompatActivity act;
    String billtoState,billtoStateCode,billtoCountrycode,billtoCountryName,shiptoState,shiptoCountrycode,shiptoCountryName,shiptoStateCode;
    CountryAdapter countryAdapter;
    StateAdapter stateAdapter,shipStateAdapter;
    ArrayList<StateData> stateList = new ArrayList<>();
    ArrayList<StateData> shipstateList = new ArrayList<>();
    LeadValue leadValue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    act = AddBPCustomer.this;
    setContentView(R.layout.fragment_add_partner2);
    ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent!= null&& Prefs.getString(Globals.AddBp,"").equalsIgnoreCase("Lead")){
            leadValue = intent.getParcelableExtra(Globals.AddBp);
            setData(leadValue);
        }
        act = this;
        shippinngType  = getResources().getStringArray(R.array.shippingType);
        billshipType = shippinngType[0];

        setDefaults();
        callBPlistApi();
        if(Globals.checkInternet(this)) {
            callSalessApi();

        }
        eventManager();
     }


    private void setData(LeadValue leadValue) {
        name_value.setText(leadValue.getCompanyName());
        contact_owner_value.setText(leadValue.getContactPerson());
        email_value.setText(leadValue.getEmail());
        mobile_value.setText(leadValue.getPhoneNumber());
        billing_name_value.setText(leadValue.getCompanyName());

    }

    private void setDefaults() {
        frameManager(general_frame,contact_frame,general,contact);
        done_button.setVisibility(View.GONE);
        head_title.setText(getResources().getString(R.string.add_bussiness));
        create_button.setOnClickListener(this);
        back_press.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        general.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        contact.setOnClickListener(this);


    }


    ArrayList<BusinessPartnerData> AllitemsList = new ArrayList<>();
    private void callBPlistApi() {


        CustomerViewModel model = ViewModelProviders.of(this).get(CustomerViewModel.class);
        model.getCustomersList(loader).observe(act, new Observer<List<BusinessPartnerData>>() {
            @Override
            public void onChanged(@Nullable List<BusinessPartnerData> itemsList) {

                if(itemsList.size()>=0) {
                    AllitemsList.clear();
                    BusinessPartnerData bpd = new BusinessPartnerData();
                    bpd.setCardName("No Parent Account");
                    AllitemsList.addAll(itemsList);


//                    parent_account_value.setAdapter(new ParentAccAdapter(act,filter(AllitemsList)));
                    parent_account_value.setAdapter(new ArrayAdapter(act, android.R.layout.simple_list_item_1, addDatatoCategoryList(AllitemsList)));
                    parenT_account = addDatatoCategoryList(AllitemsList).get(0);


                }
            }

        });


    }

    private ArrayList<String> addDatatoCategoryList(ArrayList<BusinessPartnerData> allitemsList) {
        ArrayList<String>  bplist = new ArrayList<>();
        for(BusinessPartnerData bpdata : allitemsList){
                   /* if(LeadID.isEmpty()){

                    }else{
                        if(bpdata.getU_LEADID().equalsIgnoreCase(LeadID)){
                            bplist.add(bpdata.getCardName());
                        }
                    }
*/
            bplist.add(bpdata.getCardName());
        }
        return bplist;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_press:
                onBackPressed();
                break;
            case R.id.tab_1:
            case R.id.general:
                frameManager(general_frame,contact_frame,general,contact);
                break;
            case R.id.tab_2:
            case R.id.contact:
                frameManager(contact_frame,general_frame,contact,general);
                break;


            case R.id. create_button:
                String name      = name_value.getText().toString().trim();
                String balance = account_balance_value.getText().toString().trim();
                String anlrvnue = annual_revenue_value.getText().toString().trim();
                String credit_limit = credit_limit_value.getText().toString().trim();
                String invoice = invoice_no_value.getText().toString().trim();
                String rating = String.valueOf(ratingBar.getRating());
                String cardCode  = cardCode_value.getText().toString().trim();
                String mobile  = mobile_value.getText().toString().trim();
                String email   = email_value.getText().toString().trim();
                String website = website_value.getText().toString().trim();
                String comp_email = company_email_value.getText().toString().trim();
                String comp_no = company_no_value.getText().toString().trim();

                if(validation(name,comp_email,comp_no,mobile,email,industryCode,billtoStateCode)){


                    /************************ BP Address *****************************/
                    ArrayList<BPAddress>      postbpAddresses = new ArrayList<>();

                    BPAddress bp = new BPAddress();
                    bp.setBPCode(cardCode);
                    bp.setAddressName(billing_name_value.getText().toString());
                    bp.setStreet(billing_address_value.getText().toString());
                    bp.setBlock("");
                    bp.setZipCode(zip_code_value.getText().toString());
                    bp.setCity("");
                    bp.setCountry(billtoCountrycode);  //countryCode
                    bp.setState(billtoStateCode);
                    bp.setUCountry(billtoCountryName);
                    bp.setUState(billtoState);
                    bp.setUShptyp(billshipType);
                    bp.setRowNum("0");
                    bp.setAddressType("bo_BillTo");
                    bp.setDistrict("");
                    postbpAddresses.add(bp);


                    BPAddress bp1 = new BPAddress();

                    if(checkbox1.isChecked()){
                        bp1.setZipCode(zipcode_value2.getText().toString());
                        bp1.setAddressName(shipping_name_value.getText().toString());
                        bp1.setStreet(shipping_address_value.getText().toString());
                        bp1.setUState(shiptoState);
                        bp1.setUCountry(shiptoCountryName);
                        bp1.setUShptyp(ship_shiptype);
                        bp1.setState(shiptoStateCode);
                        bp1.setCountry(shiptoCountrycode);


                    }else{
                        bp1.setAddressName(billing_name_value.getText().toString());
                        bp1.setStreet(billing_address_value.getText().toString());
                        bp1.setZipCode(zip_code_value.getText().toString());
                        bp1.setUCountry(billtoCountryName);
                        bp1.setUState(billtoState);
                        bp1.setUShptyp(billshipType);
                        bp1.setState(billtoStateCode);
                        bp1.setCountry(billtoCountrycode);

                    }
                    bp1.setRowNum("1");

                    bp1.setBPCode(cardCode);
                    bp1.setBlock("B");
                    bp1.setCity("");
                    bp1.setAddressType("bo_ShipTo");
                    /*bp1.setShipToDistrict("");
                    bp1.setBillToDistrict("");*/
                    postbpAddresses.add(bp1);

                    /********************* Con Employee ************************/
                    ArrayList<ContactEmployees>   postcontactEmployees  = new ArrayList<>();
                    ContactEmployees postemp = new ContactEmployees();
                    postemp.setName(contact_owner_value.getText().toString());
                    postemp.setE_Mail(email);
                    postemp.setMobilePhone(mobile);
                   /* postemp.setCardCode("Code");
                    postemp.setAddress("Cinntra");
                    postemp.setPosition("1");
                    postemp.setPhone1("96868768");
                    postemp.setPhone2("7863823132");
                    postemp.setMobilePhone("896787632");
                    postemp.setFax("jkdf");
                    postemp.setPager("4");
                    postemp.setRemarks1("Remarks1");
                    postemp.setRemarks2("Remarks1");
                    postemp.setGender("Male");
                    postemp.setTitle("Mr");
                    postemp.setPassword("Pass");
                    postemp.setFirstName("First");
                    postemp.setMiddleName("Middle");
                    postemp.setLastName("LastName");*/
                    postcontactEmployees.add(postemp);



                    AddBusinessPartnerData contactExtension = new AddBusinessPartnerData();
                    contactExtension.setUCurbal(balance);
                    contactExtension.setPayTermsGrpCode(payment_term);
                    contactExtension.setCreditLimit(credit_limit);
                    contactExtension.setURating(rating);
                    contactExtension.setUInvno(invoice);
                    contactExtension.setUParentacc(parenT_account);
                    contactExtension.setCardCode(cardCode);
                    contactExtension.setCardName(name);
                    contactExtension.setCardType("cCustomer"); //select value from spinner
                    contactExtension.setSalesPersonCode(String.valueOf(salesEmployeeCode));
                    contactExtension.setEmailAddress(comp_email);
                    contactExtension.setPhone1(comp_no);
                    contactExtension.setUType(TYPE);
                    contactExtension.setNotes(remarks_value.getText().toString());
                    contactExtension.setUAnlrvn(anlrvnue);
                    contactExtension.setIndustry(industryCode);
                    contactExtension.setUAccnt("");
                    contactExtension.setWebsite(website);
                    contactExtension.setDiscountPercent("");
                    contactExtension.setCurrency("INR");
                    contactExtension.setIntrestRatePercent("");
                    contactExtension.setCommissionPercent("");
                    contactExtension.setAttachmentEntry("");
                    contactExtension.setUBpgrp("");
                    contactExtension.setU_LEADID("");
                    contactExtension.setU_LEADNM("");
                    contactExtension.setGroupType("");
                    contactExtension.setCustomerType("");
                    contactExtension.setPriceCategory("");
                    contactExtension.setPaymantMode("");
                    contactExtension.setDeliveryMode("");
                    contactExtension.setTurnover("");
                    contactExtension.setTCS("");
                    contactExtension.setLink("");
                    contactExtension.setBeneficiaryName("");
                    contactExtension.setBankName("");
                    contactExtension.setACNumber("");
                    contactExtension.setIfscCode("");
                    contactExtension.setUnit("");
                    contactExtension.setFreeDelivery("");
                    contactExtension.setCreatedBy("");
                    contactExtension.setUContownr(contact_owner_value.getText().toString());
                   // contactExtension.setContactPerson(contact_owner_value.getText().toString());
                    contactExtension.setCreateDate(Globals.getTodaysDatervrsfrmt());
                    contactExtension.setCreateTime(Globals.getTCurrentTime());
                    contactExtension.setUpdateDate(Globals.getTodaysDatervrsfrmt());
                    contactExtension.setuLat(String.valueOf(Globals.currentlattitude));
                    contactExtension.setuLong(String.valueOf(Globals.currentlongitude));
                    contactExtension.setUpdateTime(Globals.getTCurrentTime());
                    contactExtension.setBPAddresses(postbpAddresses);
                    contactExtension.setContactEmployees(postcontactEmployees);
                    if(Globals.checkInternet(this)) {
                        loader.setVisibility(View.VISIBLE);
                        createBP(contactExtension);
                    }
                }


                break;

        }
       }

    private void frameManager(FrameLayout visiblle_frame, FrameLayout f1, TextView selected, TextView t1) {
        selected.setTextColor(getResources().getColor(R.color.colorPrimary));
        t1.setTextColor(getResources().getColor(R.color.black));
        visiblle_frame.setVisibility(View.VISIBLE);
        f1.setVisibility(View.GONE);

    }

    private void eventManager()
    {


        parent_account_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getPaymenterm.size() > 0)
                    parenT_account = addDatatoCategoryList(AllitemsList).get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parenT_account = addDatatoCategoryList(AllitemsList).get(0);
            }
        });


        countryAdapter = new CountryAdapter(AddBPCustomer.this, Dashboard.countrylist);
        country_value.setAdapter(countryAdapter);
        country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist,"India"));

        country_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billtoCountrycode = Dashboard.countrylist.get(position).getCode();
                billtoCountryName = Dashboard.countrylist.get(position).getName();
                billtoState = "";
                billtoStateCode = "";
                callStateApi(billtoCountrycode,"billto");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                billtoCountrycode = Dashboard.countrylist.get(0).getCode();
                billtoCountryName = Dashboard.countrylist.get(0).getName();
            }
        });

        state_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billtoState= stateList.get(position).getName();
                billtoStateCode= stateList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                billtoState= stateList.get(0).getName();
                billtoStateCode= stateList.get(0).getCode();
            }
        });

        ship_country_value.setAdapter(countryAdapter);
        ship_country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist,"India"));

        ship_country_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiptoCountrycode = Dashboard.countrylist.get(position).getCode();
                shiptoCountryName = Dashboard.countrylist.get(position).getName();
                shiptoState = "";
                shiptoStateCode = "";
                callStateApi(shiptoCountrycode,"shipto");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                shiptoCountrycode = Dashboard.countrylist.get(0).getCode();
                shiptoCountryName = Dashboard.countrylist.get(0).getName();
            }
        });

        ship_state_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiptoState= stateList.get(position).getName();
                shiptoStateCode= stateList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                shiptoState= stateList.get(0).getName();
                shiptoStateCode= stateList.get(0).getCode();
            }
        });

        if(stateList.isEmpty()){
            StateData sta = new StateData();
            sta.setName("Select State");
            stateList.add(sta);
        }

        sales_employee_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(salesEmployeeItemList.size()>0&&position>0)
                    salesEmployeeCode = Integer.parseInt(salesEmployeeItemList.get(position).getSalesEmployeeCode());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                salesEmployeeCode = Integer.parseInt(salesEmployeeItemList.get(0).getSalesEmployeeCode());
            }

        });

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(utypelist.size()>0){
                    TYPE = utypelist.get(position).getId().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TYPE = utypelist.get(0).getId().toString();
            }
        });

        shipping_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billshipType = shippinngType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                billshipType = shippinngType[0];

            }
        });

        shipping_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ship_shiptype = shippinngType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ship_shiptype = shippinngType[0];
            }
        });

        industry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(IndustryItemItemList.size()>0)
                    industryCode = IndustryItemItemList.get(position).getIndustryCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(IndustryItemItemList.size()>0)
                    industryCode = IndustryItemItemList.get(0).getIndustryCode();
            }
        });
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ship_block.setVisibility(View.VISIBLE);
                }else{
                    ship_block.setVisibility(View.GONE);
                }
            }
        });
        payment_term_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(getPaymenterm.size()>0)
                    payment_term = getPaymenterm.get(position).getGroupNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                payment_term = getPaymenterm.get(0).getGroupNumber();

            }
        });

    }

    private void callStateApi(String countryCode, String code) {

        StateData stateData = new StateData();
        stateData.setCountry(countryCode);
        Call<StateRespose> call = NewApiClient.getInstance().getApiService().getStateList(stateData);
        call.enqueue(new Callback<StateRespose>() {
            @Override
            public void onResponse(Call<StateRespose> call, Response<StateRespose> response) {

                if(response.code()==200)
                {
                    if(code.equalsIgnoreCase("billto")){
                        stateList.clear();
                        if(response.body().getData().size()>0) {

                            stateList.addAll(response.body().getData());
                        }else{
                            StateData sta = new StateData();
                            sta.setName("Select State");
                            stateList.add(sta);
                        }
                        stateAdapter = new StateAdapter(AddBPCustomer.this,stateList);
                        state_value.setAdapter(stateAdapter);
                        stateAdapter.notifyDataSetChanged();
                        billtoState= stateList.get(0).getName();
                        billtoStateCode= stateList.get(0).getCode();

                    }else{
                        shipstateList.clear();
                        if(response.body().getData().size()>0) {

                            shipstateList.addAll(response.body().getData());
                        }else{
                            StateData sta = new StateData();
                            sta.setName("Select State");
                            shipstateList.add(sta);
                        }
                        shipStateAdapter = new StateAdapter(AddBPCustomer.this,shipstateList);
                        ship_state_value.setAdapter(shipStateAdapter);
                        shipStateAdapter.notifyDataSetChanged();
                        shiptoState= stateList.get(0).getName();
                        shiptoStateCode= stateList.get(0).getCode();
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
                        Toast.makeText(AddBPCustomer.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<StateRespose> call, Throwable t) {

                Toast.makeText(AddBPCustomer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validation(String cowner,String comp_name,
                               String comp_no,String mobile, String email, String industryCode, String billtoStateCode)
    {


        if (cowner.isEmpty()){
            Globals.showMessage(act,"Enter Company name");
            return false;
        }
        else if (comp_name.isEmpty()||!Globals.isvalidateemail(company_email_value)){
            Globals.showMessage(act,"Enter Company Email");
            return false;
        }
        else if (comp_no.isEmpty()){
            Globals.showMessage(act,"Enter Company Contact No.");
            return false;
        }
        else if (mobile.isEmpty()){
            Globals.showMessage(act,"Enter Contact person mobile number");
            return false;
        }
        /*else if (industryCode.trim().equalsIgnoreCase("-1")) {
            Globals.showMessage(act, "Select Industry.");
            return false;
        }*/
        else if(email.length()!=0&&!Globals.isvalidateemail(email_value)){
        Globals.showMessage(act, "Enter email address");
        return false;
    }

        else if (billtoStateCode.isEmpty()) {
            Globals.showMessage(act, "Select Billing state");
            return false;
        }

        else if (billing_name_value.getText().toString().trim().isEmpty()) {
            Globals.showMessage(act, "Enter Billing Name");
            return false;
        }
        else if (billing_address_value.getText().toString().trim().isEmpty()) {
            Globals.showMessage(act, "Enter Billing Address");
            return false;
        }
        else if (zip_code_value.getText().toString().trim().isEmpty()) {
            Globals.showMessage(act, "Enter ZipCode");
            return false;
        }
        return true;
    }

    /********************* APIs ***************************/
    List<PayMentTerm> getPaymenterm = new ArrayList<>();
    private void callPaymentApi()
         {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getPaymentList().observe(act, new Observer<List<PayMentTerm>>() {
            @Override
            public void onChanged(List<PayMentTerm> payMentTermList) {
                if (payMentTermList == null || payMentTermList.size() == 0) {
                    Globals.setmessage(act);
                } else {
                    getPaymenterm.clear();
                    getPaymenterm = payMentTermList;
                    payment_term_value.setAdapter(new PaymentAdapter(act, getPaymenterm));
                    payment_term = getPaymenterm.get(0).getGroupNumber();

                }

            }
        });
        callUTypeApi();
    }

    List<UTypeData> utypelist = new ArrayList<>();
    private void callUTypeApi() {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getBpTypeList().observe(this, new Observer<List<UTypeData>>() {
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


    List<IndustryItem> IndustryItemItemList = new ArrayList<>();
    private void callStagesApi()
        {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getIndustryList().observe(this, new Observer<List<IndustryItem>>() {
            @Override
            public void onChanged(@Nullable List<IndustryItem> itemsList) {
                if(itemsList == null || itemsList.size()== 0){
                    Globals.setmessage( act);
                }else {
                    IndustryItemItemList = itemsList;
                    industry_spinner.setAdapter(new IndustrySpinnerAdapter(act,itemsList));
                    industryCode = IndustryItemItemList.get(0).getIndustryCode();

                }
            }
        });
            callPaymentApi();
    }


    public List<SalesEmployeeItem> salesEmployeeItemList = new ArrayList<>();
    private void callSalessApi()
          {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getSalesEmployeeList().observe(act, new Observer<List<SalesEmployeeItem>>() {
            @Override
            public void onChanged(@Nullable List<SalesEmployeeItem> itemsList) {
                if(itemsList == null || itemsList.size() == 0){
                    Globals.setmessage(act);
                }else {
                    salesEmployeeItemList = itemsList;
                    sales_employee_spinner.setAdapter(new SalesEmployeeAdapter(act,itemsList));
                    salesEmployeeCode = Integer.parseInt(salesEmployeeItemList.get(0).getSalesEmployeeCode());

                }
            }
        });
              callStagesApi();
    }

    /********************* Add Contact API *************************/

    private void createBP(AddBusinessPartnerData in)
         {

        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().addnewCustomer(in);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                loader.setVisibility(View.GONE);
                if(response.code()==200)
                {

                    if(response.body().getStatus()==200) {
                        Toasty.success(AddBPCustomer.this, "Add Successfully", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }else{
                        Toasty.warning(AddBPCustomer.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
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
                        Toast.makeText(act, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toasty.error(AddBPCustomer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        if(setAlertDataDiscard(AddBPCustomer.this))
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