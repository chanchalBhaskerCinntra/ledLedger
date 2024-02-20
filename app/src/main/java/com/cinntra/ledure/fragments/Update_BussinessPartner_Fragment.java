package com.cinntra.ledure.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.ParticularCustomerInfo;
import com.cinntra.ledure.adapters.BusinessPartnerDetail;
import com.cinntra.ledure.adapters.ContactPersonAdapter;
import com.cinntra.ledure.adapters.CountryAdapter;
import com.cinntra.ledure.adapters.IndustrySpinnerAdapter;
import com.cinntra.ledure.adapters.ParticularCustomerTransactionAdapter;
import com.cinntra.ledure.adapters.PaymentAdapter;
import com.cinntra.ledure.adapters.SalesEmployeeAdapter;
import com.cinntra.ledure.adapters.StateAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.DatabaseClick;
import com.cinntra.ledure.model.AddBusinessPartnerData;
import com.cinntra.ledure.model.BPAddress;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ContactEmployees;
import com.cinntra.ledure.model.ContactPerson;
import com.cinntra.ledure.model.ContactPersonData;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.model.IndustryItem;
import com.cinntra.ledure.model.LedgerCustomerData;
import com.cinntra.ledure.model.LedgerCustomerResponse;
import com.cinntra.ledure.model.PayMentTerm;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.StateData;
import com.cinntra.ledure.model.StateRespose;
import com.cinntra.ledure.viewModel.ItemViewModel;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Update_BussinessPartner_Fragment extends Fragment implements View.OnClickListener {

    String TYPE        = "";
    String industryCode = "-1";
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.name_value)
    EditText name_value;
    @BindView(R.id.contact_owner_value)
    Spinner contact_owner_value;

    @BindView(R.id.industry_view)
    RelativeLayout industry_view;
    @BindView(R.id.industry_spinner)
    Spinner industry_spinner;
    @BindView(R.id.type_view)
    RelativeLayout type_view;
    @BindView(R.id.type_spinner)
    TextView type_spinner;
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
    @BindView(R.id.credit_limit_left_value)
    EditText credit_limit_left_value;
    @BindView(R.id.payment_term_value)
    Spinner payment_term_value;
    @BindView(R.id.parent_account_value)
    EditText parent_account_value;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.mobile_value)
    EditText mobile_value;
    @BindView(R.id.email_value)
    EditText email_value;
    @BindView(R.id.website_value)
    EditText website_value;

    @BindView(R.id.remarks_value)
    EditText remarks_value;
    @BindView(R.id.upload_img)
    ImageView upload_img;
    @BindView(R.id.uploadview)
    RelativeLayout uploadview;
    @BindView(R.id.create_button)
    Button create_button;
    @BindView(R.id.done_button)
    Button done_button;
    @BindView(R.id.sales_employee_spinner)
    Spinner sales_employee_spinner;
    @BindView(R.id.cardCode_value)
    EditText cardCode_value;
    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.billing_name_value)
    EditText billing_name_value;
    @BindView(R.id.zip_code_value)
    EditText zip_code_value;

    @BindView(R.id.shipping_spinner)
    Spinner shipping_spinner;
    @BindView(R.id.billing_address_value)
    EditText billing_address_value;
    @BindView(R.id.checkboxManager)
    RelativeLayout checkboxManager;
    @BindView(R.id.ship_block)
    LinearLayout ship_block;

    @BindView(R.id.shipping_name_value)
    EditText shipping_name_value;
    @BindView(R.id.shipping_address_value)
    EditText shipping_address_value;
    @BindView(R.id.zipcode_value2)
    EditText zipcode_value2;

    @BindView(R.id.company_email_value)
    EditText company_email_value;
    @BindView(R.id.company_no_value)
    EditText company_no_value;
    @BindView(R.id.shipping_spinner2)
    Spinner shipping_spinner2;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.country_value)
    Spinner country_value;
    @BindView(R.id.state_value)
    Spinner state_value;
    @BindView(R.id.ship_country_value)
    EditText ship_country_value;
    @BindView(R.id.ship_state_value)
    EditText ship_state_value;
    @BindView(R.id.fetch_credit_limit)
     ImageView fetch_credit_limit;
    String payment_term = "";
    FragmentActivity act;
    BusinessPartnerData customerItem;
    String [] shippinngType ;
    String salesEmployeeCode = "";
    String salesPersonCode = "";
    String billrownum = "";
    String shiprownum = "";
    String internalCode = "";
    String billshiptype = "";
    String ship_shiptype = "";
    String billtoState,billtoCountrycode,billtoCountryName,shiptoState,shiptoCountrycode,shiptoCountryName,shiptoStateCode,billtoStateCode;
    CountryAdapter countryAdapter;
    StateAdapter stateAdapter ;
    String contactpersonName= "";
    String contactpersonCode= "-1";
    String contactpersonmobile= "";
    String contactpersonemail= "";
    ContactPersonAdapter contactPersonAdapter;
    ArrayList<StateData> stateList = new ArrayList<>();
    DatabaseClick dbClick;
    List<ContactPersonData> contactPersonDataList = new ArrayList<>();
    private static  boolean ESCAPED = true;
    public Update_BussinessPartner_Fragment(BusinessPartnerDetail businessPartnerDetail, BusinessPartnerData customerItem) {
      this.customerItem = customerItem;
      this.dbClick = ( DatabaseClick)businessPartnerDetail;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.update_partner, container, false);
        ButterKnife.bind(this,v);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        setDefaults();
        callbponeapi(customerItem.getCardCode());

        if(Globals.checkInternet(getActivity()))
            callContactEmployeeApi(customerItem.getCardCode());


        eventManager();

        fetch_credit_limit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            crediLimit(customerItem.getCardCode());
            }
        });


        return v;
    }

    private void callbponeapi(String cardCode) {
        BusinessPartnerData contactPersonData = new BusinessPartnerData();
        contactPersonData.setCardCode(cardCode);
        Call<CustomerBusinessRes> call= NewApiClient.getInstance().getApiService().particularcustomerdetails(contactPersonData);
        call.enqueue(new Callback<CustomerBusinessRes>()
        {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                loader.setVisibility(View.GONE);

                if(response.code()==200) {
                    // loadQuotation(loader, fromWhere, currentPage);
                    customerItem = response.body().getData().get(0);
                    setData(customerItem);
                }


            }
            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!Globals.branchData.isEmpty()){
            shipping_name_value.setText(Globals.branchData.get(0).getAddressName());
            zipcode_value2.setText(Globals.branchData.get(0).getZipCode());
            shipping_address_value.setText(Globals.branchData.get(0).getStreet());
            shiptoState = Globals.branchData.get(0).getU_STATE();
            shiptoStateCode = Globals.branchData.get(0).getState();
            shiptoCountrycode = Globals.branchData.get(0).getCountry();
            shiptoCountryName = Globals.branchData.get(0).getU_COUNTRY();
        }
        if(shiptoCountryName != null) {
            ship_country_value.setText(shiptoCountryName);

        }
        if(shiptoState != null) {
            ship_state_value.setText(shiptoState);

        }



    }


    private void callContactEmployeeApi(String id) {
        ContactPersonData contactPersonData = new ContactPersonData();
        contactPersonData.setCardCode(id);

        Call<ContactPerson> call = NewApiClient.getInstance().getApiService().contactemplist(contactPersonData);
        call.enqueue(new Callback<ContactPerson>() {
            @Override
            public void onResponse(Call<ContactPerson> call, Response<ContactPerson> response) {

                if(response.code()==200)
                {
                    contactPersonDataList.clear();

                    if(response.body().getData().size()>0) {
                        contactPersonDataList.addAll(response.body().getData());
                    }else{
                        ContactPersonData cd = new ContactPersonData();
                        cd.setFirstName("No Contact");
                        cd.setLastName("Person");
                        contactPersonDataList.add(cd);
                    }
                    contactPersonAdapter = new ContactPersonAdapter(getContext(), contactPersonDataList);
                    contact_owner_value.setAdapter(contactPersonAdapter);

                    contactPersonAdapter.notifyDataSetChanged();
                    if(customerItem.getUContownr()!=null&& !customerItem.getUContownr().trim().isEmpty())
                    contact_owner_value.setSelection(Globals.getContactPos(contactPersonDataList, customerItem.getUContownr().trim()));

                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(getContext(), mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ContactPerson> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        callSalessApi();

    }

    private void callStateApi(String countryCode, String code) {

        StateData stateData = new StateData();
        stateData.setCountry(countryCode);
        Call<StateRespose> call = NewApiClient.getInstance().getApiService().getStateList(stateData);
        call.enqueue(new Callback<StateRespose>() {
            @Override
            public void onResponse(Call<StateRespose> call, Response<StateRespose> response) {

                if(response.code()==200 ) {
                    if (code.equalsIgnoreCase("billto")) {


                        stateList.clear();
                        if (response.body().getData().size() > 0)
                          {
                         stateList.addAll(response.body().getData());
                          }
                        else {
                         StateData sta = new StateData();
                         sta.setName("Select State");
                         stateList.add(sta);
                           }
                        stateAdapter = new StateAdapter(act, stateList);
                        state_value.setAdapter(stateAdapter);
                        stateAdapter.notifyDataSetChanged();

                    // Commented Below by Bhupi date on 15 March,2023 due toi crashes in Address Section

                   /* if (customerItem.getBPAddresses().get(getAddres_Bill_Po(customerItem.getBPAddresses(), "bo_BillTo")).getState() != null)
                   state_value.setSelection(Globals.getStatePo(stateList, customerItem.getBPAddresses().get(getAddres_Bill_Po(customerItem.getBPAddresses(), "bo_BillTo")).getUState()));
                   */
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
                        Toast.makeText(getContext(), mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<StateRespose> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setDisable() {

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(name_value.getApplicationWindowToken(), 0);

        add.setOnClickListener(this);
        name_value.setClickable(false);
        name_value.setFocusable(false);

        company_id_value.setClickable(false);
        company_id_value.setFocusable(false);

        contact_owner_value.setFocusable(false);
        contact_owner_value.setClickable(false);
        done_button.setVisibility(View.GONE);
        sales_employee_spinner.setEnabled(false);
        industry_spinner.setEnabled(false);
        type_spinner.setEnabled(false);

        annual_revenue_value.setClickable(false);
        annual_revenue_value.setFocusable(false);

        account_balance_value.setFocusable(false);
        account_balance_value.setClickable(false);

        invoice_no_value.setClickable(false);
        invoice_no_value.setFocusable(false);

        credit_limit_value.setFocusable(false);
        credit_limit_left_value.setFocusable(false);
        credit_limit_value.setClickable(false);
        credit_limit_left_value.setClickable(false);

        payment_term_value.setFocusable(false);
        payment_term_value.setClickable(false);

        parent_account_value.setFocusable(false);
        parent_account_value.setClickable(false);

        mobile_value.setClickable(false);
        mobile_value.setFocusable(false);

        email_value.setClickable(false);
        email_value.setFocusable(false);

        website_value.setClickable(false);
        website_value.setFocusable(false);


        shipping_address_value.setFocusableInTouchMode(false);
        shipping_address_value.setFocusable(false);
        shipping_address_value.setClickable(false);

        billing_address_value.setClickable(false);
        billing_address_value.setFocusable(false);
        billing_address_value.setFocusableInTouchMode(false);

        billing_name_value.setFocusableInTouchMode(false);
        billing_name_value.setFocusable(false);
        billing_name_value.setClickable(false);

        zip_code_value.setClickable(false);
        zip_code_value.setFocusable(false);
        zip_code_value.setFocusableInTouchMode(false);


        shipping_spinner.setEnabled(false);

        remarks_value.setClickable(false);
        remarks_value.setFocusable(false);

        create_button.setVisibility(View.GONE);
        create_button.setClickable(false);
        create_button.setEnabled(false);

        cardCode_value.setClickable(false);
        cardCode_value.setFocusable(false);


        shipping_address_value.setFocusable(false);
        shipping_address_value.setClickable(false);

        billing_address_value.setClickable(false);
        billing_address_value.setFocusable(false);

        billing_name_value.setFocusable(false);
        billing_name_value.setClickable(false);

        shipping_name_value.setClickable(false);
        shipping_name_value.setFocusable(false);

        zipcode_value2.setClickable(false);
        zipcode_value2.setFocusable(false);

        zip_code_value.setClickable(false);
        zip_code_value.setFocusable(false);

        country_value.setClickable(false);
        ship_state_value.setClickable(false);
        ship_country_value.setClickable(false);
        state_value.setClickable(false);
        shipping_spinner2.setEnabled(false);
        ESCAPED = true;
        add.setVisibility(View.VISIBLE);
        ok.setVisibility(View.GONE);

    }

    private void setDefaults() {

        shippinngType = getActivity().getResources().getStringArray(R.array.shippingType);
        head_title.setText(getResources().getString(R.string.update_bussiness));
        create_button.setText(getResources().getString(R.string.update));
        back_press.setOnClickListener(this);
        create_button.setOnClickListener(this);
        ok.setOnClickListener(this);
        checkboxManager.setOnClickListener(this);
        cardCode_value.setClickable(false);
        cardCode_value.setFocusable(false);

    }


    private void setData (BusinessPartnerData customerItem)
             {

                 if(customerItem.getContactEmployees().size()>0){
                     contactpersonName = customerItem.getContactEmployees().get(0).getFirstName();
                     contactpersonCode = customerItem.getContactEmployees().get(0).getInternalCode();
                     contactpersonmobile = customerItem.getContactEmployees().get(0).getMobilePhone();
                     contactpersonemail = customerItem.getContactEmployees().get(0).getE_Mail();
                 }
                 if(customerItem.getBPAddresses().size()>0) {
                     billtoCountrycode = customerItem.getBPAddresses().get(getAddres_Bill_Po(customerItem.getBPAddresses(), "bo_BillTo")).getCountry();
                     billtoStateCode = customerItem.getBPAddresses().get(getAddres_Bill_Po(customerItem.getBPAddresses(), "bo_BillTo")).getState();
                     billtoCountryName = customerItem.getBPAddresses().get(getAddres_Bill_Po(customerItem.getBPAddresses(), "bo_BillTo")).getUCountry();
                     billtoState = customerItem.getBPAddresses().get(getAddres_Bill_Po(customerItem.getBPAddresses(), "bo_BillTo")).getUState();
                     shiptoCountrycode = customerItem.getBPAddresses().get(getAddres_Ship_Po(customerItem.getBPAddresses(), "bo_ShipTo")).getCountry();
                     shiptoStateCode = customerItem.getBPAddresses().get(getAddres_Ship_Po(customerItem.getBPAddresses(), "bo_ShipTo")).getState();
                     shiptoCountryName = customerItem.getBPAddresses().get(getAddres_Ship_Po(customerItem.getBPAddresses(), "bo_ShipTo")).getUCountry();
                     shiptoState = customerItem.getBPAddresses().get(getAddres_Ship_Po(customerItem.getBPAddresses(), "bo_ShipTo")).getUState();
                 }
                 callStateApi(billtoCountrycode,"billto");

                 countryAdapter = new CountryAdapter(getContext(),Dashboard.countrylist);
                 country_value.setAdapter(countryAdapter);
                 if(customerItem.getSalesPersonCode().size()>0) {
                     salesPersonCode = customerItem.getSalesPersonCode().get(0).getSalesEmployeeCode();
                     salesEmployeeCode = customerItem.getSalesPersonCode().get(0).getSalesEmployeeCode();
                 }
                 name_value.setText(""+customerItem.getCardName());
                /* if(customerItem.getContactEmployees().size() > 0)
                        internalCode = customerItem.getContactEmployees().get(0).getInternalCode();*/
                 cardCode_value.setText(""+customerItem.getCardCode());
//                 contact_owner_value.setText(""+customerItem.getContactPerson());
                if(customerItem.getUType().size()>0)
                 TYPE   = customerItem.getUType().get(0).getId().toString();
                 industryCode = customerItem.getIndustry();
                 //shiptype = customerItem.get();


                 if(customerItem.getUAccnt() != null)
                     account_balance_value.setText("" + customerItem.getUCurbal());
                 if(customerItem.getUInvno() != null)
                     invoice_no_value.setText("" + customerItem.getUInvno());
                 if(customerItem.getUParentacc() != null)
                     parent_account_value.setText("" + customerItem.getUParentacc());
                 /*if(customerItem.getURating() != null)
                     ratingBar.setRating(Float.valueOf(customerItem.getURating()));*/
                 if(customerItem.getPayTermsGrpCode() != null&&customerItem.getPayTermsGrpCode().size()>0)
                     payment_term=customerItem.getPayTermsGrpCode().get(0).getGroupNumber();
                 if(customerItem.getCreditLimit() != null)
                     credit_limit_value.setText("" + customerItem.getCreditLimit());
                 if(customerItem.getCreditLimitLeft()!=null)
                     credit_limit_left_value.setText("" + customerItem.getCreditLimitLeft());
                 if(customerItem.getUType() != null&&customerItem.getUType().size()>0)
                     type_spinner.setText(customerItem.getUType().get(0).getType());
                 if (customerItem.getUAnlrvn() != null)
                     annual_revenue_value.setText(customerItem.getUAnlrvn());
                 if (customerItem.getWebsite() != null)
                     website_value.setText(customerItem.getWebsite());
                 if(customerItem.getEmailAddress() != null)
                     company_email_value.setText(customerItem.getEmailAddress());

                 /*mobile_value.setText(customerItem.getContactEmployees().get(0).getMobilePhone());
                 email_value.setText(customerItem.getContactEmployees().get(0).getE_Mail());*/
                 if(customerItem.getPhone1() != null)
                     company_no_value.setText(customerItem.getPhone1());
                 if(customerItem.getNotes() != null)
                     remarks_value.setText(customerItem.getNotes());


        //eventManager();

        /******************* Set Address Data ********************/

         int pos_bo_BillTo =getAddres_Bill_Po(customerItem.getBPAddresses(),"bo_BillTo");
         int pos_bo_ShipTo =getAddres_Ship_Po(customerItem.getBPAddresses(),"bo_ShipTo");

          if(pos_bo_BillTo!=-1&&customerItem.getBPAddresses().size()>0)
               {
          billing_name_value.setText(customerItem.getBPAddresses().get(pos_bo_BillTo).getAddressName());
          zip_code_value.setText(customerItem.getBPAddresses().get(pos_bo_BillTo).getZipCode());
          billing_address_value.setText(customerItem.getBPAddresses().get(pos_bo_BillTo).getStreet());
          billrownum = customerItem.getBPAddresses().get(pos_bo_BillTo).getRowNum();

          if(billtoCountryName != null) {
              country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist,billtoCountryName));
          }
           billshiptype = customerItem.getBPAddresses().get(pos_bo_BillTo).getUShptyp();
           if(billshiptype !=null)
               shipping_spinner.setSelection(Globals.getShipTypePo(shippinngType,billshiptype));
                 }
          if (pos_bo_ShipTo !=-1&&customerItem.getBPAddresses().size()>0)
                   {


                           shipping_name_value.setText(customerItem.getBPAddresses().get(pos_bo_ShipTo).getAddressName());
                           zipcode_value2.setText(customerItem.getBPAddresses().get(pos_bo_ShipTo).getZipCode());
                           shipping_address_value.setText(customerItem.getBPAddresses().get(pos_bo_ShipTo).getStreet());
                           shiprownum = customerItem.getBPAddresses().get(pos_bo_ShipTo).getRowNum();

                           ship_shiptype = customerItem.getBPAddresses().get(pos_bo_ShipTo).getUShptyp();
                           if (ship_shiptype != null)
                               shipping_spinner2.setSelection(Globals.getShipTypePo(shippinngType, ship_shiptype));

                   }
        sales_employee_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
               {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(salesEmployeeItemList.size()>0&&position>0)
                {
                    salesPersonCode = salesEmployeeItemList.get(position).getSalesEmployeeCode();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

       shipping_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
               {
                     @Override
                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                         billshiptype = shippinngType[position];
                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> parent) {
                         if(billshiptype !=null)
                             shipping_spinner.setSelection(Globals.getShipTypePo(shippinngType,billshiptype));
                     }
                 });

       shipping_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
               {
                     @Override
                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                         ship_shiptype = shippinngType[position];
                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> parent) {
                         if(ship_shiptype !=null)
                             shipping_spinner2.setSelection(Globals.getShipTypePo(shippinngType,ship_shiptype));
                     }
                 });

       payment_term_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
               {
                     @Override
                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                     {
                         if(getPaymenterm.size()>0)
                         {
                             payment_term = getPaymenterm.get(position).getGroupNumber();

                         }

                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> parent) {


                     }
                 });
       callPaymentApi();
        loader.setVisibility(View.GONE);
         }

    private static int getAddres_Bill_Po(List<BPAddress> bpAddresses, String bo_billTo)
           {
        int po = 0;
        for (BPAddress obj:bpAddresses) {
            if(obj.getAddressType().trim().equalsIgnoreCase(bo_billTo.trim())) {
                po = bpAddresses.indexOf(obj);
                break;
            }
        }
        return po;

    }
    private static int getAddres_Ship_Po(List<BPAddress> bpAddresses, String bo_billTo)
          {
        int po = 0;
        for (BPAddress obj:bpAddresses) {
            if(obj.getAddressType().trim().equalsIgnoreCase(bo_billTo.trim())) {
                if(obj.getDefault()==1) {
                    po = bpAddresses.indexOf(obj);
                    break;
                }
            }
        }
        return po;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_press:
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                Globals.hideKeybaord(v,getContext());
                getActivity().onBackPressed();
                break;
            case R.id.add:
//                if(ESCAPED){
//                    Globals.openKeyboard(getContext());
//                    setEnable();
//                    setDefaults();
//                }else {
//                  //  setDisable();
//                }
                break;


            case R.id. create_button:
            case R.id.ok:

                String balance = account_balance_value.getText().toString().trim();
                String credit_limit = credit_limit_value.getText().toString().trim();
                String credit_limit_left = credit_limit_left_value.getText().toString().trim();
                String parenT_account = parent_account_value.getText().toString().trim();
                String rating = String.valueOf(ratingBar.getRating());
                String invoice_n0 = invoice_no_value.getText().toString().trim();
                String anlrvnue = annual_revenue_value.getText().toString().trim();
                String name      = name_value.getText().toString().trim();
                String cardCode  = cardCode_value.getText().toString().trim();
                String mobile    = mobile_value.getText().toString().trim();
                String email     = email_value.getText().toString().trim();
                String website = website_value.getText().toString().trim();
                String comp_email = company_email_value.getText().toString().trim();
                String comp_no = company_no_value.getText().toString().trim();
                if(validation(cardCode ,"cowner",comp_no,comp_email,industryCode))
                {

              //      setDisable();
                    /************************ BP Address *****************************/
                    ArrayList<BPAddress>  postbpAddresses  = new ArrayList<>();

                    BPAddress bp = new BPAddress();
                    bp.setBPCode(cardCode);
                    if(!billrownum.isEmpty())
                    bp.setRowNum(billrownum);
                    if(customerItem.getBPAddresses().size()>0)
                    {
                        bp.setId(customerItem.getBPAddresses().get(0).getId());
                        bp.setBpid(customerItem.getBPAddresses().get(0).getBpid());
                    }

                    bp.setAddressName(billing_name_value.getText().toString());
                    bp.setStreet(billing_address_value.getText().toString());
                    bp.setBlock("B");
                    bp.setZipCode(zip_code_value.getText().toString());
                    bp.setCity("");
                    bp.setCountry(billtoCountrycode);
                    bp.setState(billtoStateCode);
                    bp.setUCountry(billtoCountryName);
                    bp.setUState(billtoState);
                    bp.setAddressType("bo_BillTo");
                    bp.setUShptyp(billshiptype);
                    postbpAddresses.add(bp);

                    BPAddress bp1 = new BPAddress();
                    int branchId;
                    if(Globals.branchData.size()>0){
                        branchId = Globals.branchData.get(0).getId();
                    }else
                     branchId= customerItem.getBPAddresses().get(1).getId();

                    bp1.setId(branchId);
                    bp1.setBpid(customerItem.getBPAddresses().get(1).getBpid());
                    bp1.setBPCode(cardCode);
                    if(!shiprownum.isEmpty())
                    bp1.setRowNum(shiprownum);
                    bp1.setAddressName(shipping_name_value.getText().toString());
                    bp1.setStreet(shipping_address_value.getText().toString());
                    bp1.setBlock("B");
                    bp1.setZipCode(zipcode_value2.getText().toString());
                    bp1.setCity("");
                    bp1.setState(shiptoStateCode);
                    bp1.setCountry(shiptoCountrycode);
                    bp1.setUState(shiptoState);
                    bp1.setUCountry(shiptoCountryName);
                    bp1.setAddressType("bo_ShipTo");
                    bp1.setUShptyp(ship_shiptype);
                    postbpAddresses.add(bp1);

                   /********************* Con Employee ************************/
                    List<ContactEmployees>   postcontactEmployees  = new ArrayList<>();
                    ContactEmployees postemp = new ContactEmployees();

                        postemp.setFirstName(contactpersonName);
                        postemp.setCardCode(customerItem.getCardCode());
                        postemp.setMobilePhone(contactpersonmobile);
                        postemp.setE_Mail(contactpersonemail);
                    //  if(!internalCode.isEmpty())
                    postemp.setInternalCode(contactpersonCode);
                    postemp.setName(contactpersonName);
                    postcontactEmployees.add(postemp);
                     /*postemp.setCardCode("Code");
                     postemp.setAddress("Cinntra");
                     postemp.setPosition("1");

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




                    AddBusinessPartnerData contactExtension = new AddBusinessPartnerData();
                    contactExtension.setId(customerItem.getId());
                    contactExtension.setUAccnt(customerItem.getUAccnt());
                    contactExtension.setUpdateDate(Globals.getTodaysDate());
                    contactExtension.setUpdateTime(Globals.getTCurrentTime());
                    contactExtension.setCreateTime(customerItem.getCreateTime());
                    contactExtension.setCreateDate(customerItem.getCreateDate());
                    contactExtension.setCardCode(cardCode);
                    contactExtension.setUCurbal(balance);
                    contactExtension.setPayTermsGrpCode(payment_term);
                    contactExtension.setCreditLimit(credit_limit);
                    contactExtension.setCreditLimitLeft(credit_limit);
                    contactExtension.setURating(customerItem.getURating());
                    contactExtension.setUInvno(invoice_n0);
                    contactExtension.setuLat(String.valueOf(Globals.currentlattitude));
                    contactExtension.setuLong(String.valueOf(Globals.currentlongitude));
                    contactExtension.setCommissionPercent(customerItem.getCommissionPercent());
                    contactExtension.setAttachmentEntry(customerItem.getAttachmentEntry());
                    contactExtension.setIntrestRatePercent(customerItem.getIntrestRatePercent());
                    contactExtension.setCurrency(customerItem.getCurrency());
                    contactExtension.setDiscountPercent(customerItem.getDiscountPercent());
                    contactExtension.setUParentacc(parenT_account);
                    contactExtension.setCardName(name);
                    contactExtension.setCardType("cCustomer"); //select value from spinner
                    contactExtension.setSalesPersonCode(String.valueOf(salesEmployeeCode));
                    contactExtension.setEmailAddress(comp_email);
                    contactExtension.setPhone1(comp_no);
                    contactExtension.setUType(TYPE);
                    contactExtension.setIndustry(industryCode);
                    contactExtension.setUAnlrvn(anlrvnue);
                    contactExtension.setWebsite(website);
                    contactExtension.setUBpgrp(customerItem.getUBpgrp());
                    contactExtension.setNotes(remarks_value.getText().toString());
                    contactExtension.setUContownr(contactpersonName);
                    contactExtension.setBPAddresses(postbpAddresses);
                   contactExtension.setContactEmployees(postcontactEmployees);
                    if(Globals.checkInternet(getActivity()))
                        updateBP(contactExtension);
                }


                break;
            case R.id.checkboxManager:
                Globals.branchData.clear();
                Prefs.putString(Globals.SelectAddress,"UpdateAddress");
                dbClick.onClick(2);
                break;

        }
    }



    private void eventManager()
                  {


                      country_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                          @Override
                          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                              billtoCountrycode = Dashboard.countrylist.get(position).getCode();
                              billtoCountryName = Dashboard.countrylist.get(position).getName();

                              callStateApi(billtoCountrycode,"billto");

                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> parent) {


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

                          }
                      });

                      contact_owner_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                          @Override
                          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                              contactpersonName   = contactPersonDataList.get(position).getFirstName();
                              contactpersonCode   = contactPersonDataList.get(position).getInternalCode();
                              contactpersonmobile = contactPersonDataList.get(position).getMobilePhone();
                              contactpersonemail  = contactPersonDataList.get(position).getEMail();


                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> parent) {


                          }
                      });


        sales_employee_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          if(salesEmployeeItemList.size()>0)
             salesEmployeeCode = salesEmployeeItemList.get(position).getSalesEmployeeCode();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // salesEmployeeCode = 0;
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


    }

    private boolean validation
            (String fname, String cowner,
             String mobile, String email,
             String industryCode )
    {

        if(fname.isEmpty()){
            Globals.showMessage(getContext(),"Enter First Name");
            return false;
        }else if (cowner.isEmpty()){
            Globals.showMessage(getContext(),"Contact owner cannot be empty");
            return false;
        }
        else if (mobile.isEmpty()){
            Globals.showMessage(getContext(),"Mobile number cannot be empty");
            return false;
        }
//        else if (industryCode.trim().equalsIgnoreCase("-1")) {
//            Globals.showMessage(getContext(), "Select Industry.");
//            return false;
//        }
        else if (email.isEmpty()) {
            Globals.showMessage(getContext(), "Email cannot be empty");
            return false;
        }
        return true;
    }

    /********************* APIs ***************************/
    List<IndustryItem> IndustryItemItemList = new ArrayList<>();
    private void callStagesApi()
       {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getIndustryList().observe(this, new Observer<List<IndustryItem>>()
        {
            @Override
            public void onChanged(@Nullable List<IndustryItem> itemsList) {
                if(itemsList == null || itemsList.size() == 0){
                    Globals.setmessage( getActivity());
                }else {
                    IndustryItemItemList = itemsList;
                    industry_spinner.setAdapter(new IndustrySpinnerAdapter(getActivity(),itemsList));
                    if(industryCode!=null)
                       industry_spinner.setSelection(Globals.getIndustryPo(IndustryItemItemList,industryCode)); ;

                }
            }
        });
    }

    SalesEmployeeAdapter salesadapter;
    public List<SalesEmployeeItem> salesEmployeeItemList = new ArrayList<>();
    private void callSalessApi()
        {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getSalesEmployeeList().observe(getActivity(), new Observer<List<SalesEmployeeItem>>()
          {
            @Override
            public void onChanged(@Nullable List<SalesEmployeeItem> itemsList) {
                if(itemsList == null || itemsList.size() == 0){
                    Globals.setmessage(getActivity());
                }else if(itemsList != null || itemsList.size() > 0){

                    salesEmployeeItemList = itemsList;
                    salesadapter = new SalesEmployeeAdapter(getContext(),itemsList);
                    sales_employee_spinner.setAdapter(salesadapter);
                    if(!itemsList.isEmpty()&&!salesPersonCode.isEmpty())
                        sales_employee_spinner.setSelection(Globals.getSelectedSalesP(itemsList,salesPersonCode));

                    callStagesApi();
                }
            }
        });
    }
    /********************* Add Contact API *************************/

    private void updateBP(AddBusinessPartnerData in)
         {
        loader.setVisibility(View.VISIBLE);
        in.setFreeDelivery("");
        in.setUnit("");
        in.setIfscCode("");
        in.setACNumber("");
        in.setBankName("");
        in.setBeneficiaryName("");
        in.setLink("");
        in.setTCS("");
        in.setTurnover("");
        in.setDeliveryMode("");
        in.setPaymantMode("");
        in.setGroupType("");
        in.setCustomerType("");
        in.setPriceCategory("");
        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().updatecustomer(in);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                loader.setVisibility(View.GONE);
                if(response.code()==200)
                {

                    if(response.body().getStatus() == 200){
                        Globals.branchData.clear();
                        Toasty.success(act, "Updated Successfully.", Toasty.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }else{
                        Toasty.warning(act, response.body().getMessage(), Toasty.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s     = response.errorBody().string();
                        mError = gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(act, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    }
                    catch (IOException e)
                    {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t)
            {
                loader.setVisibility(View.GONE);
                Toast.makeText(act, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    List<PayMentTerm> getPaymenterm = new ArrayList<>();
    PaymentAdapter paymentAdapter;
    private void callPaymentApi(){
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getPaymentList().observe(requireActivity(), new Observer<List<PayMentTerm>>() {
            @Override
            public void onChanged(List<PayMentTerm> payMentTermList) {
                if (payMentTermList == null || payMentTermList.size() == 0) {
                    Globals.setmessage(getContext());
                } else {
                    getPaymenterm.clear();
                    getPaymenterm = payMentTermList;
                    paymentAdapter = new PaymentAdapter(requireActivity(), getPaymenterm);
                    payment_term_value.setAdapter(paymentAdapter);

                    if(!getPaymenterm.isEmpty()&&payment_term!=null)
                        payment_term_value.setSelection(Globals.getPaymentTermPo(getPaymenterm,payment_term));
                }

            }
        });
    }


    private void crediLimit(String cardCode)
        {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String,String> hde = new HashMap<>();
                hde.put("CardCode",cardCode);

                Call<LedgerCustomerResponse> call = NewApiClient.getInstance().getApiService().updatebpcreditlimitbybp(hde);
                try {
                    Response<LedgerCustomerResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                // Update UI element here
                                loader.setVisibility(View.GONE);
                            }
                        });
                        // Handle successful response

                    } else {
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }

}
