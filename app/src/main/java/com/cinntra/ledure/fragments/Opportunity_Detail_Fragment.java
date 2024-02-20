package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.OpportunityItem;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Opportunity_Detail_Fragment extends Fragment implements View.OnClickListener {

     @BindView(R.id.head_title)
     TextView head_title;
     @BindView(R.id.back_press)
     RelativeLayout back_press;
     @BindView(R.id.save)
     TextView save;
     @BindView(R.id.opportunity_no_value)
     TextView opportunity_no_value;
     @BindView(R.id.opportunity_name_value)
     TextView opportunity_name_value;
     @BindView(R.id.bussiness_partner_value)
     EditText bussiness_partner_value;
     @BindView(R.id.businessPartners)
     LinearLayout businessPartners;
     @BindView(R.id.contact)
     LinearLayout contact;
     @BindView(R.id.contact_value)
     EditText contact_value;
     @BindView(R.id.predicted_closing_value)
     TextView predicted_closing_value;
      @BindView(R.id.remark_value)
     TextView remark_value;
     @BindView(R.id.stage_remark_value)
     TextView stage_remark_value;
     @BindView(R.id.stage_value)
     TextView stage_value;
     @BindView(R.id.potential_value)
     TextView potential_value;
     @BindView(R.id.closing_rate_value)
     TextView closing_rate_value;
     @BindView(R.id.stage_start_date_value)
     TextView stage_start_date_value;
     @BindView(R.id.stage_end_date_value)
     TextView stage_end_date_value;
     @BindView(R.id.sales_employee_value)
     TextView sales_employee_value;
     @BindView(R.id.all_stages)
     LinearLayout all_stages;

    OpportunityItem opportunityItem;

    public Opportunity_Detail_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Opportunity_Detail_Fragment newInstance(String param1, String param2) {
        Opportunity_Detail_Fragment fragment = new Opportunity_Detail_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            opportunityItem =(OpportunityItem) b.getSerializable(Globals.OpportunityItem);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_opportunity_detail, container, false);
        ButterKnife.bind(this,v);
        setDefaults();
        setData();
        return v;
    }

    private void setData()
      {
    opportunity_no_value.setText(opportunityItem.getSequentialNo());
    opportunity_name_value.setText(opportunityItem.getOpportunityName());
    bussiness_partner_value.setText(opportunityItem.getBPChanelName());
    contact_value.setText(opportunityItem.getContactPerson());
    predicted_closing_value.setText(opportunityItem.getPredictedClosingDate());
    sales_employee_value.setText(opportunityItem.getSalesPerson());
    remark_value.setText(opportunityItem.getRemarks());
    stage_value.setText(opportunityItem.getCurrentStageNo());
    potential_value.setText(opportunityItem.getTotalAmountLocal());
    closing_rate_value.setText(opportunityItem.getClosingPercentage() +"%");
    stage_start_date_value.setText(opportunityItem.getStartDate());
    stage_end_date_value.setText(opportunityItem.getClosingDate());

       }

    private void setDefaults() {
        save.setVisibility(View.GONE);
        head_title.setText(getString(R.string.quotation));
        back_press.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_press:
                getActivity().onBackPressed();
                break;
        }
    }
}