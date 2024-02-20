package com.cinntra.ledure.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.AddBPCustomer;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.LeadTypeData;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadInformation extends Fragment implements View.OnClickListener {
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.contact_person_value)
    TextView contact_person_value;
    @BindView(R.id.phone_number)
    TextView phone_number;
    @BindView(R.id.email_value)
    TextView email_value;
    @BindView(R.id.designation)
    TextView designation;
    @BindView(R.id.product_interest)
    TextView product_interest;
    @BindView(R.id.date_value)
    TextView date_value;
    @BindView(R.id.back_press)
    RelativeLayout back_press;

    @BindView(R.id.create_bp)
    Button create_bp;
    @BindView(R.id.history)
    Button history;




    LeadValue leadValue;
    Context leadsActivity;
    List<LeadValue> leadValues = new ArrayList<>();
    String[] leadstatus = new String[4];
    String status = "";
    String leadtype = "";
    Integer id;
    ArrayList<LeadTypeData> leadTypeData = new ArrayList<>();
    Context context;
    public LeadInformation(Context context) {
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
        View v=inflater.inflate(R.layout.lead_info, container, false);
        ButterKnife.bind(this,v);
        head_title.setText("Lead Detail");
        back_press.setOnClickListener(this);
        create_bp.setOnClickListener(this);
        history.setOnClickListener(this);
        leadstatus = getResources().getStringArray(R.array.lead_status);

       // eventManager();
        if(Globals.checkInternet(getContext())) {
            callApi(id);
        }
        else{
            Toast.makeText(getContext(),getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
        }



        return v;
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
     //   status_spinner.setSelection(getStatuspos(lv.getStatus()));
        company_name.setText(lv.getCompanyName());
        contact_person_value.setText(lv.getContactPerson());
        phone_number.setText(lv.getPhoneNumber());
        email_value.setText(lv.getEmail());
        product_interest.setText(lv.getProductInterest());
        date_value.setText(lv.getDate());
        designation.setText(lv.getDesignation());



    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_press:

                getActivity().onBackPressed();
                break;
            case R.id.create_bp:

                Prefs.putString(Globals.AddBp,"Lead");
                Intent intent = new Intent(context, AddBPCustomer.class);
                intent.putExtra(Globals.AddBp,leadValue);
                context.startActivity(intent);

                break;
            case R.id.history:

                Bundle bundle = new Bundle();
                bundle.putParcelable(Globals.Lead_Data,leadValue);
                LeadFollowUpFragment chatterFragment = new LeadFollowUpFragment();
                chatterFragment.setArguments(bundle);
                FragmentTransaction chattransaction =  ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                chattransaction.add(R.id.customer_lead, chatterFragment).addToBackStack(null);
                chattransaction.commit();
                break;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
