package com.cinntra.ledure.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.LeadTypeAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.CreateLead;
import com.cinntra.ledure.model.LeadTypeData;
import com.cinntra.ledure.model.LeadTypeResponse;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.webservices.NewApiClient;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadDetail extends Fragment implements View.OnClickListener {
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.companyname)
    EditText companyname;
    @BindView(R.id.full_name)
    EditText full_name;
    @BindView(R.id.contact_no)
    EditText contact_no;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.source)
    EditText source;
    @BindView(R.id.location)
    EditText location;
    @BindView(R.id.product_interest)
    EditText product_interest;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.update)
    Button update;
     @BindView(R.id.loader)
     SpinKitView loader;
     @BindView(R.id.phone)
     ImageView phone;
    @BindView(R.id.turnover)
    EditText turnover;
    @BindView(R.id.designation)
    EditText designation;
    @BindView(R.id.status_spinner)
    Spinner status_spinner;
    @BindView(R.id.leadType_spinner)
    Spinner leadType_spinner;



    LeadValue leadValue;
    Context leadsActivity;
    List<LeadValue> leadValues = new ArrayList<>();
    String[] leadstatus = new String[4];
    String status = "";
    String leadtype = "";
    Integer id;
    ArrayList<LeadTypeData> leadTypeData = new ArrayList<>();
    Context context;
    public LeadDetail(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            if(b.getString("From").equalsIgnoreCase("Lead")) {
                leadValue = (LeadValue) b.getParcelable(Globals.LeadDetails);
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                id = leadValue.getId();
            }
            else {
                id = Integer.parseInt(b.getString("From","2"));
                }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.lead_detail, container, false);
        ButterKnife.bind(this,v);
        head_title.setText("Lead Detail");
        back_press.setOnClickListener(this);
        leadstatus = getResources().getStringArray(R.array.lead_status);
        loader.setVisibility(View.VISIBLE);
        eventManager();
        if(Globals.checkInternet(getContext())) {
            callApi(id);
        }
        else{
            Toast.makeText(getContext(),getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
        }



        return v;
    }


    private void callleadTypeApi(String type) {

        Call<LeadTypeResponse> call = NewApiClient.getInstance().getApiService().getLeadType();
        call.enqueue(new Callback<LeadTypeResponse>() {
            @Override
            public void onResponse(Call<LeadTypeResponse> call, Response<LeadTypeResponse> response) {

                if(response.code()==200)
                {
                    leadTypeData.clear();
                    leadTypeData.addAll(response.body().getData());
                    leadType_spinner.setAdapter(new LeadTypeAdapter(getContext(),leadTypeData));
                    leadType_spinner.setSelection(Globals.getleadType(leadTypeData,type));
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, LeadResponse.class);
                        Toast.makeText(getContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<LeadTypeResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void eventManager() {


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation(companyname, full_name, contact_no, location, comment, email)) {

                    CreateLead lv = new CreateLead();
                    lv.setId(id);
                    lv.setCompanyName(companyname.getText().toString());
                    lv.setContactPerson(full_name.getText().toString());
                    lv.setPhoneNumber(contact_no.getText().toString());
                    lv.setEmail(email.getText().toString());
                    lv.setLocation(location.getText().toString());
                    lv.setSource(source.getText().toString());
                    lv.setProductInterest(product_interest.getText().toString());
                    lv.setAssignedTo(leadValues.get(0).getAssignedTo().getId().toString());
                    lv.setNumOfEmployee(10);
                    lv.setTurnover(turnover.getText().toString());
                    lv.setDesignation(designation.getText().toString());
                    lv.setEmployeeId(leadValues.get(0).getEmployeeId().getId());
                    lv.setMessage(comment.getText().toString());
                    lv.setDate(leadValues.get(0).getDate());
                    lv.setTimestamp(Globals.getTimestamp());
                    lv.setStatus(status);
                    lv.setLeadType(leadtype);
                    lv.setProjectAmount("");
                    lv.setCustomerType("");
                    lv.setStages("");
                    if (Globals.checkInternet(getContext())) {
                        loader.setVisibility(View.VISIBLE);
                        callUpdateApi(lv);
                    }
                }
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!contact_no.getText().toString().trim().isEmpty()) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + contact_no.getText().toString()));


                    Dexter.withActivity(getActivity())
                            .withPermission(Manifest.permission.CALL_PHONE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    startActivity(intent);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                    // check for permanent denial of permission
                                    if (response.isPermanentlyDenied()) {
                                        // navigate user to app settings

                                        Toast.makeText(getContext(),"Please Enable phone from setting",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                    // startActivity(intent);
                }
            }
        });


        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                status = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(parent.getSelectedItem()!=null)
                status = parent.getSelectedItem().toString();
            }
        });

        leadType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leadtype = leadTypeData.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                leadtype = leadTypeData.get(0).getName();
            }
        });


    }

    private boolean validation(EditText companyname, EditText full_name, EditText contact_no, EditText location, EditText product_interest, EditText email) {

        if(companyname.length()==0){
            companyname.requestFocus();
            companyname.setError("Enter Company Name");
            Toasty.warning(requireContext(),"Enter Company Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(full_name.length()==0){
            full_name.requestFocus();
            full_name.setError("Enter Full Name");
            Toasty.warning(requireContext(),"Enter Full Name",Toast.LENGTH_SHORT).show();

            return false;
        }
        else if(contact_no.length()==0){
            contact_no.setError("Enter Contact No");
            contact_no.requestFocus();
            Toasty.warning(requireContext(),"Enter Contact No",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!email.getText().toString().isEmpty()){
            if(!Globals.isvalidateemail(email)){
                email.requestFocus();
                email.setError("Email address is not valid");
                return false;
            }
        }
        return true;

    }


    private void callUpdateApi(CreateLead lv) {
        Call<LeadResponse> call = NewApiClient.getInstance().getApiService().updateLead(lv);
        call.enqueue(new Callback<LeadResponse>() {
            @Override
            public void onResponse(Call<LeadResponse> call, Response<LeadResponse> response) {

                if(response.code()==200)
                {
                    if(response.body().getMessage().equalsIgnoreCase("successful")){
                        getActivity().onBackPressed();
                        Toasty.success(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toasty.warning(getContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, LeadResponse.class);
                        Toast.makeText(getActivity(), mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<LeadResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toasty.error(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApi(int id) {
        LeadValue lv = new LeadValue();
        lv.setId(id);
        Call<LeadResponse> call = NewApiClient.getInstance().getApiService().particularlead(lv);
        call.enqueue(new Callback<LeadResponse>() {
            @Override
            public void onResponse(Call<LeadResponse> call, Response<LeadResponse> response) {
                if (response != null)
                {
                    if(response.body()!=null){

                        leadValues = response.body().getData();
                        setData(leadValues.get(0));

                    }


                }
            }
            @Override
            public void onFailure(Call<LeadResponse> call, Throwable t) {

            }
        });
    }

    private void setData(LeadValue lv) {

        leadtype = lv.getLeadType();
        status = lv.getStatus();
        status_spinner.setSelection(getStatuspos(lv.getStatus()));
        companyname.setText(lv.getCompanyName());
        full_name.setText(lv.getContactPerson());
        contact_no.setText(lv.getPhoneNumber());
        email.setText(lv.getEmail());
        location.setText(lv.getLocation());
        source.setText(lv.getSource());
        product_interest.setText(lv.getProductInterest());
        turnover.setText(lv.getTurnover());
        designation.setText(lv.getDesignation());
        comment.setText(lv.getMessage());
        loader.setVisibility(View.GONE);
        callleadTypeApi(lv.getLeadType());

    }

    private int getStatuspos(String status) {
        int pos = -1;
        for(int i =0;i<leadstatus.length;i++)
        {
            String data = leadstatus[i];
            if(data.equalsIgnoreCase(status)){
                pos = i;
            }
        }
        return pos;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_press:

                getActivity().onBackPressed();
                break;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    }
}
