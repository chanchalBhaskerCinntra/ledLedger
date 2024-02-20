package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.QuotationDocumentLines;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Content_Detail_Fragment extends Fragment implements View.OnClickListener {

     @BindView(R.id.head_title)
    TextView head_title;
     @BindView(R.id.back_press)
    RelativeLayout back_press;
     @BindView(R.id.save)
     TextView save;

     @BindView(R.id.item_code_value)
     TextView item_code_value;
     @BindView(R.id.description_value)
     TextView description_value;
     @BindView(R.id.warehouse_value)
     TextView warehouse_value;
     @BindView(R.id.delievry_date_value)
     TextView delievry_date_value;
     @BindView(R.id.quanity_value)
     TextView quanity_value;
     @BindView(R.id.item_per_unit_value)
     TextView item_per_unit_value;
     @BindView(R.id.measure_unit_value)
     TextView measure_unit_value;
     @BindView(R.id.project_value)
     TextView project_value;
     @BindView(R.id.distribution_rule_value)
     TextView distribution_rule_value;
     @BindView(R.id.unit_price_value)
     TextView unit_price_value;
     @BindView(R.id.gross_price_value)
     TextView gross_price_value;
     @BindView(R.id.discount_value_per)
     TextView discount_value_per;
     @BindView(R.id.tax_code_value)
     TextView tax_code_value;
     @BindView(R.id.total_value)
     TextView total_value;
     @BindView(R.id.open_inv_qty_value)
     TextView open_inv_qty_value;

    QuotationDocumentLines quotationLineItem;

    public Content_Detail_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Content_Detail_Fragment newInstance(String param1, String param2) {
        Content_Detail_Fragment fragment = new Content_Detail_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            quotationLineItem =(QuotationDocumentLines) b.getSerializable(Globals.QuotationLine);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_content_detail, container, false);
        ButterKnife.bind(this,v);
        setDefaults();
        setData();
        return v;
    }

    private void setData()
    {
        item_code_value.setText(quotationLineItem.getItemCode());
        description_value.setText(quotationLineItem.getItemDescription());
        warehouse_value.setText(quotationLineItem.getWarehouseCode());
        delievry_date_value.setText(quotationLineItem.getShipDate());
        quanity_value.setText(quotationLineItem.getQuantity());
        item_per_unit_value.setText("Not Set");
        measure_unit_value.setText(quotationLineItem.getUnitsOfMeasurment());
        project_value.setText(quotationLineItem.getProjectCode());
        distribution_rule_value.setText(quotationLineItem.getDistributeExpense());
        unit_price_value.setText(quotationLineItem.getUnitPrice());
        gross_price_value.setText(quotationLineItem.getGrossPrice());
        discount_value_per.setText(quotationLineItem.getDiscountPercent());
        tax_code_value.setText(quotationLineItem.getTaxCode());
        total_value.setText(quotationLineItem.getLineTotal());
        open_inv_qty_value.setText(quotationLineItem.getInventoryQuantity());
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