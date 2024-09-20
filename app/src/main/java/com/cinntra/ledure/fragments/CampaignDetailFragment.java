package com.cinntra.ledure.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.CampaignModel;
import com.cinntra.ledure.model.CampaignResponse;
import com.cinntra.ledure.model.MemberList;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignDetailFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.campaign_setname)
    TextView campaign_setname;
    @BindView(R.id.campaign_access)
    TextView campaign_access;
    @BindView(R.id.description)
    TextView description;


    /******************      Leads    *********************/
    @BindView(R.id.from_date)
    TextView from_date;
    @BindView(R.id.to_date)
    TextView to_date;
    @BindView(R.id.source)
    TextView source;
    @BindView(R.id.priority)
    TextView priority;
    @BindView(R.id.status)
    TextView status;

    /******************      Opportunity    *********************/
    @BindView(R.id.oppfrom_date)
    TextView oppfrom_date;
    @BindView(R.id.oppto_date)
    TextView oppto_date;
    @BindView(R.id.sales_employee)
    TextView sales_employee;
    @BindView(R.id.type)
    TextView type;


    /******************      Customer    *********************/

    @BindView(R.id.bpfrom_date)
    TextView customerfrom_date;
    @BindView(R.id.bpto_date)
    TextView customerto_date;
    @BindView(R.id.bpsales_employee)
    TextView bpsales_employee;
    @BindView(R.id.bptype)
    TextView bptype;
    @BindView(R.id.industry)
    TextView industry;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.country)
    TextView country;


    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.campaign_list)
    Button campaign_list;
    @BindView(R.id.member_list)
    Button member_list;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back)
    ImageView back_press;


    CampaignModel campaignData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            campaignData =(CampaignModel) b.getSerializable(Globals.CampaignData);

        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.campaign_detail_screen, container, false);
        ButterKnife.bind(this,v);

        loader.setVisibility(View.VISIBLE);
        member_list.setOnClickListener(this);
        back_press.setOnClickListener(this);
        head_title.setText(campaignData.getCampaignSetName());
        if(Globals.checkInternet(getContext()))
        {

            callApi(campaignData.getId());
        }


        return v;
    }

    private void callApi(Integer id) {

        CampaignModel cm = new CampaignModel();
        cm.setId(id);

        Call<CampaignResponse> call = NewApiClient.getInstance().getApiService(getActivity()).getCampsetDetails(cm);
        call.enqueue(new Callback<CampaignResponse>() {
            @Override
            public void onResponse(Call<CampaignResponse> call, Response<CampaignResponse> response) {

                if(response.code()==200)
                {
                   setData(response.body().getData().get(0));
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

            }
            @Override
            public void onFailure(Call<CampaignResponse> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<MemberList> memberLists = new ArrayList();

    private void setData(CampaignModel cmp) {
        loader.setVisibility(View.GONE);
        campaign_setname.setText(cmp.getCampaignSetName());
        campaign_access.setText(cmp.getCampaignAccess());
        description.setText(cmp.getDescription());
        source.setText(cmp.getLeadSource());
        priority.setText(cmp.getLeadPriority());
        from_date.setText(cmp.getLeadFromDate());
        to_date.setText(cmp.getLeadToDate());
        status.setText(cmp.getLeadStatus());

        if(cmp.getOppSalePerson().size()>0)
        sales_employee.setText(cmp.getOppSalePerson().get(0).getSalesEmployeeName());
        type.setText(cmp.getOppType());
        oppfrom_date.setText(cmp.getOppFromDate());
        oppto_date.setText(cmp.getOppToDate());

        if(cmp.getBPSalePerson().size()>0)
        bpsales_employee.setText(cmp.getBPSalePerson().get(0).getSalesEmployeeName());
        bptype.setText(cmp.getOppType());
        customerfrom_date.setText(cmp.getBPFromDate());
        customerto_date.setText(cmp.getBPToDate());
        country.setText(cmp.getBPCountry());
        state.setText(cmp.getBPState());
        if(cmp.getBPIndustry().size()>0)
        industry.setText(cmp.getBPIndustry().get(0).getIndustryName());

        memberLists.addAll(cmp.getMemberList());


    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.member_list:
                Bundle b = new Bundle();
                b.putSerializable(Globals.CampaignData, campaignData);
                MemberListFragment fragment = new  MemberListFragment();
                fragment.setArguments(b);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.container, fragment);
                transaction.addToBackStack("Back");
                transaction.commit();

                break;
            case R.id.back:
                requireActivity().onBackPressed();
                break;

        }

    }
}
