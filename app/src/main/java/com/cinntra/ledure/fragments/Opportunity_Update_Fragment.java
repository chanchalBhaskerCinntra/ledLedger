package com.cinntra.ledure.fragments;


 import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.cinntra.ledure.activities.OwnerList;
import com.cinntra.ledure.adapters.ContactPersonAdapter;
import com.cinntra.ledure.adapters.SalesEmployeeAdapter;
import com.cinntra.ledure.adapters.StageSpinnerAdapter;
import com.cinntra.ledure.globals.Globals;
 import com.cinntra.ledure.model.ContactPersonData;
 import com.cinntra.ledure.model.NewOppResponse;
 import com.cinntra.ledure.model.OwnerItem;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.SalesOpportunitiesLines;
import com.cinntra.ledure.model.StagesItem;
 import com.cinntra.ledure.newapimodel.AddOpportunityModel;
 import com.cinntra.ledure.newapimodel.NewOpportunityRespose;
 import com.cinntra.ledure.viewModel.ItemViewModel;
 import com.cinntra.ledure.webservices.NewApiClient;
 import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class Opportunity_Update_Fragment extends Fragment implements View.OnClickListener {

    FragmentActivity act;
    int salesEmployeeCode = 0;
    String OppID = "";

    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.mainContainer)
    LinearLayout mainContainer;
    @BindView(R.id.opportunity_name_value)
    EditText opportunity_name_value;
    @BindView(R.id.business_partner_value)
    TextView business_partner_value;
    @BindView(R.id.contact_person_spinner)
    Spinner contact_person_spinner;
    @BindView(R.id.close_date_value)
    EditText close_date_value;
    @BindView(R.id.opportunity_owner_value)
    EditText opportunity_owner_value;
    @BindView(R.id.sales_employee_spinner)
    Spinner sales_employee_spinner;
    @BindView(R.id.type_value)
    TextView type_value;
    @BindView(R.id.probability_value)
    EditText probability_value;
    @BindView(R.id.potential_amount_value)
    EditText potential_amount_value;
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
    @BindView(R.id.lead_source_value)
    TextView lead_source_value;


    @BindView(R.id.startDate)
    RelativeLayout startDate;
    @BindView(R.id.start_date_value)
    EditText start_date_value;
    @BindView(R.id.lead_value)
    EditText lead_value;
    @BindView(R.id.startcalender)
    ImageView startcalender;
    @BindView(R.id.closeDate)
    RelativeLayout closeDate;
    @BindView(R.id.closeCalender)
    ImageView closeCalender;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.ok)
    ImageView ok;
    public static int OWNERCODE   = 1001;
    private static  boolean ESCAPED = true;

    NewOpportunityRespose opportunityItem;
    String salesEmployeename = "";
    String ContactPersonName     = "";
    String DataOwnershipfield = "";

    public Opportunity_Update_Fragment()
      {
    //Required empty public constructor
      }


    // TODO: Rename and change types and number of parameters
    public static Opportunity_Update_Fragment newInstance(String param1, String param2) {
        Opportunity_Update_Fragment fragment = new Opportunity_Update_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
     Bundle b      = getArguments();
     opportunityItem =(NewOpportunityRespose) b.getParcelable(Globals.OpportunityItem);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
           ViewGroup container,
            Bundle savedInstanceState) {

        act = getActivity();
        View v=inflater.inflate(R.layout.update_opportunity, container, false);
        ButterKnife.bind(this,v);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        head_title.setText(getString(R.string.update_opportunity));
       // getActivity().getActionBar().hide();
        back_press.setOnClickListener(this);
//        setDisable();
        setEnable();
        setDefaults();
        setData();
        return v;
    }

    private void setDisable() {
        add.setOnClickListener(this);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(opportunity_name_value.getApplicationWindowToken(), 0);

        opportunity_name_value.setFocusable(false);
        opportunity_name_value.setClickable(false);

        contact_person_spinner.setEnabled(false);

        opportunity_owner_value.setClickable(false);
        opportunity_owner_value.setFocusable(false);

        sales_employee_spinner.setEnabled(false);

        potential_amount_value.setFocusable(false);
        potential_amount_value.setClickable(false);

        stage_spinner.setEnabled(false);
        probability_value.setFocusable(false);
        probability_value.setClickable(false);

        description_value.setClickable(false);
        description_value.setFocusable(false);

        start_date_value.setClickable(false);
        start_date_value.setFocusable(false);
        startcalender.setEnabled(false);

        submit_button.setVisibility(View.GONE);
        submit_button.setEnabled(false);
        submit_button.setClickable(false);
        submit_button.setFocusable(false);

        add.setVisibility(View.VISIBLE);
        ok.setVisibility(View.GONE);
        ESCAPED = true;
    }

    String salesPersonCode = "";
    String ContactPersonCode = "";
    String CurrentStage = "";
    String Ownershipfield = "";
    private void setData()
      {
          lead_value.setText(opportunityItem.getU_LEADNM());
          salesEmployeename = opportunityItem.getSalesPersonName();
          ContactPersonName = opportunityItem.getContactPersonName();
    salesPersonCode   = opportunityItem.getSalesPerson();
    ContactPersonCode = opportunityItem.getContactPerson();
    CurrentStage      = opportunityItem.getCurrentStageNo();
    Ownershipfield    = opportunityItem.getDataOwnershipfield();
    OppID= opportunityItem.getSequentialNo();
    opportunity_name_value.setText(opportunityItem.getOpportunityName());
    business_partner_value.setText(opportunityItem.getCardCode());
    lead_source_value.setText(opportunityItem.getULsource());
    close_date_value.setText(opportunityItem.getPredictedClosingDate());
    start_date_value.setText(opportunityItem.getStartDate());
    description_value.setText(opportunityItem.getRemarks());
    if(opportunityItem.getUType().size()>0)
    type_value.setText(opportunityItem.getUType().get(0).getType());
    probability_value.setText(opportunityItem.getUProblty());
    potential_amount_value.setText(opportunityItem.getTotalAmountLocal());
    opportunity_owner_value.setText(opportunityItem.getDataOwnershipName());
    salesEmployeeCode = Integer.parseInt(opportunityItem.getSalesPerson());

//    CurrentStage = opportunityItem.getSalesOpportunitiesLines().get( opportunityItem.getSalesOpportunitiesLines().size()-1).getStageKey();
    if(Globals.checkInternet(getActivity()))
        callContactApi(opportunityItem.getCardCode());
    }


  private void setDefaults() {
   
   

   start_date_value.setOnClickListener(this);
   startDate.setOnClickListener(this);
   startcalender.setOnClickListener(this);
   closeCalender.setOnClickListener(this);
   ok.setOnClickListener(this);
   closeDate.setOnClickListener(this);
   close_date_value.setOnClickListener(this);
   submit_button.setOnClickListener(this);
   opportunity_owner_value.setOnClickListener(this);
    }
    ArrayList<SalesOpportunitiesLines> jsonlist = new ArrayList<>();

    @Override
    public void onClick(View v) {
    switch (v.getId())
        {
    case R.id.back_press:

     ((AppCompatActivity) getActivity()).getSupportActionBar().show();
      InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(opportunity_name_value.getApplicationWindowToken(), 0);
      getActivity().finish();

            break;
    case R.id.add:
         if(ESCAPED){
         Globals.openKeyboard(getContext());
         setEnable();
         setDefaults();
           }else {
         setDisable();
           }
         break;
            case R.id.startDate:
            case R.id.startcalender:
            case R.id.start_date_value:
                Globals.selectDate(act,start_date_value);
                break;
            case R.id.close_date:
            case R.id.closeDate:
            case R.id.close_date_value:
                Globals.selectDate(act,close_date_value);
                break;

            case R.id.opportunity_owner_value:
                Intent ii = new Intent(getActivity(), OwnerList.class);
                startActivityForResult(ii,OWNERCODE);
                break;
    case R.id.submit_button:
    case R.id.ok:
                setDisable();
                String cardValue    = business_partner_value.getText().toString().trim();
                String       remark = description_value.getText().toString().trim();
                if(validation(cardValue,salesEmployeeCode,CurrentStage,potential_amount_value.getText().toString().trim(),remark)) {
                    SalesOpportunitiesLines dc = new SalesOpportunitiesLines();
                    dc.setSalesPerson(salesEmployeeCode);
                    dc.setDocumentType("bodt_MinusOne");
                    dc.setMaxSystemTotal(Float.valueOf(potential_amount_value.getText().toString().trim()));
                    dc.setMaxLocalTotal(Float.valueOf(potential_amount_value.getText().toString().trim()));
                    dc.setStageKey(CurrentStage);
                    jsonlist.add(dc);


                    AddOpportunityModel obj = new AddOpportunityModel();
                    obj.setMaxSystemTotal(potential_amount_value.getText().toString().trim());
                    obj.setOpportunityName(opportunity_name_value.getText().toString().trim());
                    obj.setClosingDate(close_date_value.getText().toString().trim());
                    obj.setStartDate(start_date_value.getText().toString().trim());
                    obj.setDataOwnershipfield(opportunityItem.getDataOwnershipfield());
                    obj.setUProblty(probability_value.getText().toString().trim());
                     obj.setCardCode(cardValue); //cardcode
                    obj.setSalesPerson(String.valueOf(salesEmployeeCode)); //salesEmployeeCode
                    obj.setContactPerson(ContactPersonCode);
                    obj.setMaxLocalTotal(potential_amount_value.getText().toString().trim());//Potential Ammount
                    obj.setRemarks(remark);
                    obj.setId(opportunityItem.getId());
                    obj.setSequentialNo(opportunityItem.getSequentialNo());
                    obj.setSource("None");
                    obj.setStatus("sos_Open");
                    obj.setReasonForClosing("None");
                    obj.setTotalAmounSystem(opportunityItem.getTotalAmounSystem());
                    obj.setTotalAmountLocal(opportunityItem.getTotalAmountLocal());
                    obj.setCurrentStageNo(opportunityItem.getCurrentStageNo());
                    obj.setIndustry("None");
                    obj.setLinkedDocumentType("None");
                    obj.setStatusRemarks("None");
                    obj.setProjectCode("None");
                    obj.setCustomerName(opportunityItem.getCustomerName());
                    obj.setClosingType("sos_Days");
                    obj.setOpportunityType("boOpSales");
                    obj.setUpdateDate(Globals.getTodaysDate());
                    obj.setUpdateTime(Globals.getTCurrentTime());
                    obj.setUType(opportunityItem.getUType().get(0).getId().toString());
                    obj.setULsource(opportunityItem.getULsource());
                    obj.setUFav(opportunityItem.getUFav());
                    obj.setSalesPersonName(salesEmployeename);
                    obj.setContactPersonName(ContactPersonName);
                    obj.setDataOwnershipName(salesEmployeename);
                    obj.setPredictedClosingDate(close_date_value.getText().toString().trim());
                    obj.setU_LEADID(opportunityItem.getU_LEADID());
                    obj.setU_LEADNM(lead_value.getText().toString());

                    obj.setSalesOpportunitiesLines(jsonlist);
                    if(Globals.checkInternet(getActivity()))
                        updateOpportunity(obj);
                }
                break;
        }



    }

    private void setEnable() {


        opportunity_name_value.setFocusable(true);
        opportunity_name_value.setClickable(true);
        opportunity_name_value.setFocusableInTouchMode(true);

        contact_person_spinner.setEnabled(true);

        opportunity_owner_value.setClickable(true);
        opportunity_owner_value.setFocusable(true);

        sales_employee_spinner.setEnabled(true);

        potential_amount_value.setFocusable(true);
        potential_amount_value.setClickable(true);
        potential_amount_value.setFocusableInTouchMode(true);

        stage_spinner.setEnabled(true);
        probability_value.setFocusable(true);
        probability_value.setClickable(true);
        probability_value.setFocusableInTouchMode(true);

        description_value.setClickable(true);
        description_value.setFocusable(true);
        description_value.setFocusableInTouchMode(true);

        start_date_value.setClickable(true);
        start_date_value.setFocusable(true);
        startcalender.setEnabled(true);
        submit_button.setVisibility(View.VISIBLE);
        submit_button.setEnabled(true);
        submit_button.setClickable(true);
        submit_button.setFocusable(true);
        add.setVisibility(View.GONE);
        ok.setVisibility(View.GONE);

        ESCAPED = false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == OWNERCODE&&resultCode == RESULT_OK)
        {
            OwnerItem ownerItem = (OwnerItem) data.getSerializableExtra(Globals.OwnerItemData);
            opportunity_owner_value.setText(ownerItem.getFirstName()+" "+ownerItem.getMiddleName()+" "+ownerItem.getLastName());
            DataOwnershipfield = ownerItem.getEmployeeID();
        }

    }

    private void eventManager()
       {
        sales_employee_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
           {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(salesEmployeeItemList.size()>0&&position>0)
                {
                    salesEmployeename = salesEmployeeItemList.get(position).getSalesEmployeeName();
                    salesPersonCode = salesEmployeeItemList.get(position).getSalesEmployeeCode();
                    salesEmployeeCode = Integer.valueOf(salesPersonCode);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                salesEmployeeCode =Integer.valueOf(salesPersonCode);

            }
        });
        stage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
           {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if(stagesItemList.size()>0&&position>0)
                CurrentStage = stagesItemList.get(position).getStageno();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // CurrentStage = stagesItemList.get(0).getStageno();
            }
        });

         contact_person_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
           {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //   ContactPersonName = ContactEmployeesList.get(position).getName();
                 ContactPersonCode = ContactEmployeesList.get(position).getInternalCode();
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

          /*if(Globals.OwnerList.size()>0&&Ownershipfield!=null)
           {
              int pos = Globals.getOwenerPo(Globals.OwnerList,Ownershipfield);
             opportunity_owner_value.setText(Globals.OwnerList.get(pos).getFirstName()+" "+Globals.OwnerList.get(pos).getLastName());
               DataOwnershipfield = Globals.OwnerList.get(pos).getEmployeeID();
             }
          else {
              if(Globals.checkInternet(getActivity()))
                callOwenerApi();

            }*/
    }



    List<StagesItem> stagesItemList = new ArrayList<>();
    private void callStagesApi()
         {
    ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
    model.getStagesList().observe(this, new Observer<List<StagesItem>>() {
            @Override
            public void onChanged(@Nullable List<StagesItem> itemsList) {
                if(itemsList == null || itemsList.size() == 0){
                    Globals.setmessage(getActivity());
                }else {
                    stagesItemList = itemsList;
                    stage_spinner.setAdapter(new StageSpinnerAdapter(getActivity(),itemsList));
                    if(!itemsList.isEmpty()&&CurrentStage!=null)
                        stage_spinner.setSelection(Globals.getSelectedStage(itemsList,CurrentStage));
                    eventManager();
                }
            }
        });
         }
    SalesEmployeeAdapter salesadapter;
    public List<SalesEmployeeItem> salesEmployeeItemList = new ArrayList<>();
    private void callSalessApi()
       {
    ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
    model.getSalesEmployeeList().observe(this, new Observer<List<SalesEmployeeItem>>() {
    @Override
    public void onChanged(@Nullable List<SalesEmployeeItem> itemsList)
     {
         if(itemsList == null || itemsList.size() == 0){
             Globals.setmessage(getActivity());
         }else {
             salesEmployeeItemList = itemsList;
             salesadapter = new SalesEmployeeAdapter(getActivity(),itemsList);
             sales_employee_spinner.setAdapter(salesadapter);
            /* if(Globals.checkInternet(getActivity()))
                 callStagesApi();*/
             if(!itemsList.isEmpty()&&!salesPersonCode.isEmpty())
                 sales_employee_spinner.setSelection(Globals.getSelectedSalesP(itemsList,salesPersonCode));
         }
     }
     });
    }

    private List<ContactPersonData>  ContactEmployeesList;
    ContactPersonAdapter contactPersonAdapter;

    private void callContactApi(String cardCode)
       {
    ContactEmployeesList = new ArrayList<>();
    ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
    model.getContactEmployeeList(cardCode).observe(getActivity(), new Observer<List<ContactPersonData>>() {
    @Override
    public void onChanged(@Nullable List<ContactPersonData> itemsList)
         {
             if(itemsList == null || itemsList.size() == 0){
                 Globals.setmessage(getActivity());
             }else {
               ContactEmployeesList = itemsList;
                 contactPersonAdapter =new ContactPersonAdapter(getActivity(),ContactEmployeesList);
                 contact_person_spinner.setAdapter(contactPersonAdapter);
                 //int index = ContactEmployeesList.get
                 if(Globals.checkInternet(getActivity()))
                     callSalessApi();
                 if(!itemsList.isEmpty()&&ContactPersonCode!=null)
                     contact_person_spinner.setSelection(Globals.getSelectedContact(itemsList,ContactPersonCode));
             }
        }
        });
    }


    private void callOwenerApi()
       {
    ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
    model.getEmployeesList(null).observe(this, new Observer<List<OwnerItem>>() {
            @Override
     public void onChanged(@Nullable List<OwnerItem> itemsList) {
                if(itemsList == null || itemsList.size() == 0){
                    Globals.setmessage(getActivity());
                }else {
                    Globals.OwnerList.clear();
                    Globals.OwnerList.addAll(itemsList);

                if(Ownershipfield!=null)
                    {
                 int pos = Globals.getOwenerPo(Globals.OwnerList, Ownershipfield);
                 opportunity_owner_value.setText(itemsList.get(pos).getFirstName() + " " + itemsList.get(pos).getLastName());
                 DataOwnershipfield = Globals.OwnerList.get(pos).getEmployeeID();
                    }


                }
         }
      });
    }


    private void updateOpportunity(AddOpportunityModel in)
       {
    Call<NewOppResponse> call = NewApiClient.getInstance().getApiService(getActivity()).newUpdateOpportunity(in);
    call.enqueue(new Callback<NewOppResponse>() {
    @Override
    public void onResponse(Call<NewOppResponse> call, Response<NewOppResponse> response) {
     if(response.code()==200)
      {
      Globals.SelectedItems.clear();
         Toast.makeText(getActivity(), "Posted Successfully.", Toast.LENGTH_SHORT).show();
      getActivity().onBackPressed();
      }
      else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(getActivity(), mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<NewOppResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    private boolean validation(
            String cardCode,int
            salesEmployeeCode,String stagesCode,
            String potentialAmount,String remark)
       {
        if(cardCode.isEmpty())
        {

            Globals.showMessage(act,getString(R.string.can_not_empty));
            return false;
        }
        else if(salesEmployeeCode==0){
            Globals.showMessage(act,getString(R.string.can_not_empty));
            return false;
        }

        else if(remark.isEmpty()){
            description_value.requestFocus();
            description_value.setError(getString(R.string.description_error));
            Globals.showMessage(act,getString(R.string.description_error));
            return false;
        }

        return true;
    }


}