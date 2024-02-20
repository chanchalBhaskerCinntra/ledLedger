package com.cinntra.ledure.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.DepartMentAdapter;
import com.cinntra.ledure.adapters.RoleAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CreateContactData;
import com.cinntra.ledure.model.DepartMent;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.Role;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddContact extends MainBaseActivity implements View.OnClickListener {
    public static int PARTNERCODE = 111;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.name_value)
    EditText name_value ;
    @BindView(R.id.first_name)
    TextView first_name;
    @BindView(R.id.first_name_value)
    EditText first_name_value;
    @BindView(R.id.last_name_value)
    EditText last_name_value;
    @BindView(R.id.business_partner)
    TextView business_partner;
   @BindView(R.id.business_partner_value)
   EditText business_partner_value;
   @BindView(R.id.reports_to)
   TextView reports_to;
   @BindView(R.id.position_value)
   EditText position_value;
   @BindView(R.id.department_spinner)
   Spinner department_spinner;
   @BindView(R.id.role_spinner)
   Spinner role_spinner;
   @BindView(R.id.contact_owner_value)
   EditText contact_owner_value;
   @BindView(R.id.mobile_value)
   EditText mobile_value;
   @BindView(R.id.email_value)
   EditText email_value;
   @BindView(R.id.website_value)
   EditText website_value;
    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.address_value)
    EditText address_value;
    @BindView(R.id.create_button)
    Button create_button;
    @BindView(R.id.bussinessPartner)
    RelativeLayout bussinessPartner;
    String departmentName = "";
    String rolename = "";
    public List<DepartMent> getDepartMent = new ArrayList<>();
    public List<Role> getRole = new ArrayList<>();

    AppCompatActivity act;
    BusinessPartnerData value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        ButterKnife.bind(this);
        act = AddContact.this;
        if(!Prefs.getString(Globals.AddContactPerson,"").equalsIgnoreCase("Dashboard")){
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                value = (BusinessPartnerData) bundle.getSerializable(Globals.AddContactPerson);
                business_partner_value.setClickable(false);
                business_partner_value.setFocusable(false);
                bussinessPartner.setClickable(false);
                bussinessPartner.setFocusable(false);

                setData(value);
            }
        }else{
            bussinessPartner.setOnClickListener(this);
            business_partner_value.setOnClickListener(this);
        }


        callDepartMentApi();
        callRoleApi();
        head_title.setText(getString(R.string.new_contact));
        back_press.setOnClickListener(this);
        create_button.setOnClickListener(this);
        eventManager();


    }

    private void eventManager() {
        department_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                departmentName = getDepartMent.get(position).getName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                departmentName = getDepartMent.get(0).getName();
            }
        });


        role_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(getRole.size() > 0 && position > 0){
                    rolename = getRole.get(position).getName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                rolename = getRole.get(0).getName();
            }
        });

    }


    private void callDepartMentApi() {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getDepartMent().observe(this, new Observer<List<DepartMent>>() {
            @Override
            public void onChanged(List<DepartMent> departMentList) {
                if (departMentList == null || departMentList.size() == 0) {
                    Globals.setmessage(getApplicationContext());
                } else {
                    getDepartMent.clear();
                    getDepartMent.addAll(departMentList);
                    department_spinner.setAdapter(new DepartMentAdapter(getApplicationContext(), getDepartMent));
                    departmentName = getDepartMent.get(0).getName();

                }

            }
        });
    }

    private void callRoleApi() {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getRoleList().observe(this, new Observer<List<Role>>() {
            @Override
            public void onChanged(List<Role> rolelist) {
                if (rolelist == null || rolelist.size() == 0) {
                    Globals.setmessage(getApplicationContext());
                } else {
                    getRole.clear();
                    getRole.addAll(rolelist);
                    role_spinner.setAdapter(new RoleAdapter(getApplicationContext(), getRole));
                    rolename = getRole.get(0).getName();
                }

            }
        });
    }
    public static String CardValue="";
    public static String CardName="";
    public static String salePCode;
    public static Integer BPid;
    private void setData(BusinessPartnerData customerItem)
       {

        // PriceListNum = customerItem.getPriceListNum();
        CardValue = customerItem.getCardCode();
        CardName  = customerItem.getCardName();
        salePCode = customerItem.getContactPerson();
        BPid = customerItem.getId();
        business_partner_value.setText(CardValue);


         }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK&&requestCode == PARTNERCODE) {
            BusinessPartnerData customerItem = (BusinessPartnerData) data.getSerializableExtra(Globals.CustomerItemData);
            setData(customerItem);
        }



    }

    @Override
    public void onClick(View v)
      {
    switch (v.getId())
        {
            case R.id.back_press:
                onBackPressed();
                break;
            case R.id.create_button:


                String fname   = first_name_value.getText().toString().trim();
                String lname   = last_name_value.getText().toString().trim();
                String bpvalue = business_partner_value.getText().toString().trim();
                String position = position_value.getText().toString().trim();
                String dvalue  = "Dapartment";
                String rvalue  = "Sales Manager";
//                String covalue = contact_owner_value.getText().toString().trim();
                String mobile  = mobile_value.getText().toString().trim();
                String email   = email_value.getText().toString().trim();
//                String website = website_value.getText().toString().trim();
                String address = address_value.getText().toString().trim();

                if(validation(fname,lname,bpvalue,position,dvalue,rvalue,
                               mobile,email,address))
                      {


                          CreateContactData obj = new CreateContactData();
                          obj.setCardCode(CardValue);
                          obj.setTitle("");
                          obj.setPosition(rolename);
                          obj.setAddress(address);
                          obj.setMobilePhone(mobile);
                          obj.setEMail(email);
                          obj.setProfession(departmentName);
                          obj.setFirstName(fname);
                          obj.setMiddleName("");
                          obj.setLastName(lname);
                          obj.setFax("");
                          obj.setRemarks1("");
                          obj.setDateOfBirth("");
                          obj.setGender("");
                          obj.setUBpid(BPid.toString());
                          obj.setUBranchid("1");
                          obj.setUNationalty("Indian");
                          obj.setUpdateDate(Globals.getTodaysDate());
                          obj.setUpdateTime(Globals.getTCurrentTime());
                          obj.setCreateDate(Globals.getTodaysDate());
                          obj.setCreateTime(Globals.getTCurrentTime());

                 if(Globals.checkInternet(getApplicationContext())){
                     loader.setVisibility(View.VISIBLE);
                     addContact(obj);}
                 }
                break;


            case R.id.bussinessPartner:
            case R.id.business_partner_value:
                Prefs.putString(Globals.BussinessPageType,"AddContact");
                Intent i = new Intent(act, BussinessPartners.class);
                startActivityForResult(i,PARTNERCODE);
                break;
        }
      }

    private boolean validation( String fname, String lname, String bpvalue, String rtvalue,
                               String dvalue, String rvalue, String mobile,
                               String email,  String address)
       {

        if(fname.isEmpty()){
            Globals.showMessage(getApplicationContext(),"Enter First Name");
            return false;
        }
        else if (lname.isEmpty()){
            Globals.showMessage(getApplicationContext(),"Enter Last Name");
            return false;
        }else if (bpvalue.isEmpty()){
            Globals.showMessage(getApplicationContext(),"Select Business Partner");
            return false;
        }else if (rtvalue.isEmpty()){
            Globals.showMessage(getApplicationContext(),"Enter Position");
            return false;
        }else if (dvalue.isEmpty()){
            Globals.showMessage(getApplicationContext(),"Select Department");
            return false;
        }else if (rvalue.isEmpty()){
            Globals.showMessage(getApplicationContext(),"Select Role");
            return false;
        }else if (mobile.isEmpty()){
            Globals.showMessage(getApplicationContext(),"Enter Mobile Number");
            return false;
        }else if (!email.isEmpty()){
            if(isvalidateemail()){
                email_value.setError("This email address is not valid");
                return false;
            }
        }else if (address.isEmpty()){
            Globals.showMessage(getApplicationContext(),"Enter Address");
            return false;
        }
        return true;
    }



    private boolean isvalidateemail(){
        String checkEmail = email_value.getText().toString();
        boolean hasSpecialEmail = Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches();
        if(!hasSpecialEmail){
            email_value.setError("This email address is not valid");
            return true;
        }
        return false;
    }



    /********************* Add Contact API *************************/

    private void addContact(CreateContactData contactData)
         {

        Call<QuotationResponse> call = NewApiClient.getInstance().getApiService().createcontact(contactData);
        call.enqueue(new Callback<QuotationResponse>() {
            @Override
            public void onResponse(Call<QuotationResponse> call, Response<QuotationResponse> response) {
                loader.setVisibility(View.GONE);
                if(response.code()==200)
                {
                    onBackPressed();
                    Toast.makeText(act, "Added Successfully.", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<QuotationResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(act, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        if(setAlertDataDiscard(AddContact.this))
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